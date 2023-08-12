package fr.leblanc.gomoku.service;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.Stack;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import fr.leblanc.gomoku.controller.WebSocketController;
import fr.leblanc.gomoku.exception.EngineException;
import fr.leblanc.gomoku.model.Game;
import fr.leblanc.gomoku.model.GameType;
import fr.leblanc.gomoku.model.GomokuColor;
import fr.leblanc.gomoku.model.MessageType;
import fr.leblanc.gomoku.model.Move;
import fr.leblanc.gomoku.model.User;
import fr.leblanc.gomoku.model.WebSocketMessage;
import fr.leblanc.gomoku.repository.GameRepository;
import fr.leblanc.gomoku.web.dto.GameDTO;
import fr.leblanc.gomoku.web.dto.MoveDTO;
import fr.leblanc.gomoku.web.dto.UserSettingsDTO;
import lombok.extern.apachecommons.CommonsLog;

@Service
@CommonsLog
public class GameService {

	private static final String NOT_SUPPORTED_FOR_ONLINE_GAME = "Not supported for online game";

	private static final String GAME_NOT_FOUND = "Game not found";

	@Autowired
	private GameRepository gameRepository;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private EngineService engineService;
	
	@Autowired
	private WebSocketController webSocketController;
	
	private Map<Long, Stack<Move>> undoMoveStack = new HashMap<>();
	
	public Game getOrCreateGame(GameType gameType) {
		Game currentGame = getCurrentGame(gameType);
		if (currentGame == null) {
			currentGame = createGame(userService.getCurrentUser(), gameType);
			gameRepository.save(currentGame);
		}
		return currentGame;
	}
	
	public void createOnlineGame(String player1Username, String player2Username) {
		User player1 = userService.findUserByUsername(player1Username);
		User player2 = userService.findUserByUsername(player2Username);
		boolean isBlackPlayer = Math.random() > 0.5;
		Game onlineGame = createGame(isBlackPlayer ? player1 : player2, isBlackPlayer ? player2 : player1, GameType.ONLINE);
		gameRepository.save(onlineGame);
		userService.save(player1);
		userService.save(player2);
	}

	public void resetGame(GameType gameType) {
		
		Game currentGame = getCurrentGame(gameType);
		if (currentGame == null) {
			throw new IllegalStateException(GAME_NOT_FOUND);
		}
		
		deleteGame(currentGame);
		
		if (gameType == GameType.ONLINE) {
			currentGame.getBlackPlayer().setCurrentOnlineGame(null);
			userService.save(currentGame.getBlackPlayer());
			currentGame.getWhitePlayer().setCurrentOnlineGame(null);
			userService.save(currentGame.getWhitePlayer());
			webSocketController.sendMessage(WebSocketMessage.builder().gameId(currentGame.getId()).type(MessageType.ONLINE_GAME_ABORTED).build());
		} else {
			Game newGame = createGame(userService.getCurrentUser(), gameType);
			gameRepository.save(newGame);
		}
	}
	
	public void deleteGame(GameType gameType) {
		Game game = getCurrentGame(gameType);
		
		if (game == null) {
			throw new IllegalStateException(GAME_NOT_FOUND);
		}
		
		deleteGame(game);
	}
	
	private void deleteGame(Game game) {
		engineService.clearGame(game.getId());
		gameRepository.delete(game);
	}
	
	private boolean checkNewMoveAllowed(Game currentGame) {
		if (!currentGame.getWinCombination().isEmpty()) {
			return false;
		}
		int color = extractPlayingColor(currentGame);
		if (color == GomokuColor.BLACK.toNumber() && !userService.getCurrentUser().equals(currentGame.getBlackPlayer())) {
			return false;
		}
		if (color == GomokuColor.WHITE.toNumber() && !userService.getCurrentUser().equals(currentGame.getWhitePlayer())) {
			return false;
		}
		return true;
	}

	public Move addMove(GameType gameType, int columnIndex, int rowIndex) {
		
		Game game = getCurrentGame(gameType);
		
		if (game == null) {
			throw new IllegalStateException(GAME_NOT_FOUND);
		}
		
		boolean computeNextMove = gameType == GameType.AI;
		if (!checkNewMoveAllowed(game)) {
			return null;
		}
		
		return addMoveInternal(game, columnIndex, rowIndex, computeNextMove);
	}

	private Move addMoveInternal(Game currentGame, int columnIndex, int rowIndex, boolean computeNextMove) {

		if (currentGame.getMove(columnIndex, rowIndex) != null) {
			throw new IllegalStateException("Move already set");
		}
		
		int color = extractPlayingColor(currentGame);
		
		if (color == GomokuColor.BLACK.toNumber() && !userService.getCurrentUser().equals(currentGame.getBlackPlayer())) {
			return null;
		}
		
		if (color == GomokuColor.WHITE.toNumber() && !userService.getCurrentUser().equals(currentGame.getWhitePlayer())) {
			return null;
		}
		
		undoMoveStack.computeIfAbsent(currentGame.getId(), k -> new Stack<>()).clear();
		
		Move newMove = Move.builder().number(currentGame.getMoves().size()).columnIndex(columnIndex).rowIndex(rowIndex).color(color).build();
		currentGame.getMoves().add(newMove);
		
		gameRepository.save(currentGame);
		
		webSocketController.sendMessage(WebSocketMessage.builder().gameId(currentGame.getId()).type(MessageType.MOVE).content(newMove).build());

		Double newEvaluation = engineService.computeEvaluation(new GameDTO(currentGame));
		
		webSocketController.sendMessage(WebSocketMessage.builder().gameId(currentGame.getId()).type(MessageType.EVALUATION).content(newEvaluation).build());
		
		if (!checkWin(currentGame) && computeNextMove) {
			computeMove(currentGame.getType());
		}
		
		return newMove;
	}

	private boolean checkWin(Game currentGame) {
		try {
			Set<Move> winningMoves = engineService.checkWin(new GameDTO(currentGame));
			if (winningMoves != null && !winningMoves.isEmpty()) {
				webSocketController.sendMessage(WebSocketMessage.builder().gameId(currentGame.getId()).type(MessageType.IS_WIN).content(winningMoves).build());
				return true;
			}
		} catch (ResourceAccessException e) {
			log.error("Could not access Gomoku Engine : " + e.getMessage());
		}
		return false;
	}

	private int extractPlayingColor(Game currentGame) {
		return currentGame.getMoves().size() % 2 == 0 ? GomokuColor.BLACK.toNumber() : GomokuColor.WHITE.toNumber();
	}

	public Set<Move> undoMove(GameType gameType) {
		
		Game currentGame = getCurrentGame(gameType);

		if (currentGame == null) {
			throw new IllegalStateException(GAME_NOT_FOUND);
		}
		
		if (currentGame.getType() == GameType.ONLINE) {
			throw new IllegalStateException(NOT_SUPPORTED_FOR_ONLINE_GAME);
		} 
		
		Move lastMove = removeLastMove(currentGame);
		
		undoMoveStack.computeIfAbsent(currentGame.getId(), k -> new Stack<>()).push(lastMove);
		
		if (!currentGame.getWinCombination().isEmpty()) {
			currentGame.getWinCombination().clear();
		}
		
		gameRepository.save(currentGame);
		
		return currentGame.getMoves();
		
	}

	public Set<Move> redoMove(GameType gameType) {
		
		Game currentGame = getCurrentGame(gameType);

		if (currentGame == null) {
			throw new IllegalStateException(GAME_NOT_FOUND);
		}
		
		if (currentGame.getType() == GameType.ONLINE) {
			throw new IllegalStateException(NOT_SUPPORTED_FOR_ONLINE_GAME);
		}
		
		Move lastMove = undoMoveStack.computeIfAbsent(currentGame.getId(), k -> new Stack<>()).pop();
		
		if (lastMove != null) {
			currentGame.getMoves().add(lastMove);
			checkWin(currentGame);
			gameRepository.save(currentGame);
		}
		
		return currentGame.getMoves();
	}

	public Move computeMove(GameType gameType) {
		
		Game currentGame = getCurrentGame(gameType);
		if (currentGame == null) {
			throw new IllegalStateException(GAME_NOT_FOUND);
		}
		
		if (!checkNewMoveAllowed(currentGame)) {
			return null;
		}
		
		GameDTO gameDto = new GameDTO(currentGame);
		
		gameDto.setSettings(new UserSettingsDTO(userService.getCurrentUser().getSettings()));
		
		webSocketController.sendMessage(WebSocketMessage.builder().gameId(currentGame.getId()).type(MessageType.IS_COMPUTING).content(true).build());
		Move computedMove = engineService.computeMove(gameDto);
		webSocketController.sendMessage(WebSocketMessage.builder().gameId(currentGame.getId()).type(MessageType.IS_COMPUTING).content(false).build());
		
		if (computedMove == null) {
			throw new EngineException("Move could not be computed");
		}
		
		boolean computeNextMove = gameType == GameType.AI_VS_AI;
		
		return addMoveInternal(currentGame, computedMove.getColumnIndex(), computedMove.getRowIndex(), computeNextMove);
	}

	public Double computeEvaluation(GameType gameType) {
		
		Game currentGame = getCurrentGame(gameType);
	
		if (currentGame == null) {
			throw new IllegalStateException(GAME_NOT_FOUND);
		}
		
		GameDTO gameDto = new GameDTO(currentGame);
		
		gameDto.setSettings(new UserSettingsDTO(userService.getCurrentUser().getSettings()));
		
		return engineService.computeEvaluation(gameDto);
	}

	public void stopComputation(Long gameId) {
		engineService.stopComputation(gameId);
	}

	public Move getLastMove(GameType gameType, boolean propagate) {
		Game currentGame = getCurrentGame(gameType);
		
		if (currentGame == null) {
			throw new IllegalStateException(GAME_NOT_FOUND);
		}
		
		Move lastMove = getLastMove(currentGame);
		
		if (propagate) {
			webSocketController.sendMessage(WebSocketMessage.builder().gameId(currentGame.getId()).type(MessageType.LAST_MOVE).content(lastMove).build());
		}
		
		return lastMove;
	}

	public ResponseEntity<Resource> downloadGame(GameType gameType) {
		
		Game currentGame = getCurrentGame(gameType);
		
		byte[] buffer = new JSONObject(new GameDTO(currentGame)).toString().getBytes();
		
	    return ResponseEntity.ok()
	            .contentLength(buffer.length)
	            .contentType(MediaType.APPLICATION_OCTET_STREAM)
	            .body(new InputStreamResource(new ByteArrayInputStream(buffer)));
	}

	public void uploadGame(GameType gameType, GameDTO uploadedGame) {
		Game currentGame = getCurrentGame(gameType);
		
		if (currentGame != null) {
			gameRepository.delete(currentGame);
		}
		
		Game newGame = createGame(userService.getCurrentUser(), gameType);
		
		newGame = gameRepository.save(newGame);
		
		for (MoveDTO moveDto : uploadedGame.getMoves()) {
			
			Move move = new Move();
			
			move.setColor(moveDto.getColor());
			move.setColumnIndex(moveDto.getColumnIndex());
			move.setRowIndex(moveDto.getRowIndex());
			move.setNumber(moveDto.getNumber());
			
			newGame.getMoves().add(move);
		}
		
		gameRepository.save(newGame);
	}

	public Boolean isComputing(Long gameId) {
		return engineService.isComputing(gameId);
	}

	public Game getCurrentGame(GameType gameType) {
		Game currentGame = null;
		
		switch (gameType) {
		case LOCAL:
			currentGame = userService.getCurrentUser().getCurrentLocalGame();
			break;
		case AI:
			currentGame = userService.getCurrentUser().getCurrentAIGame();
			break;
		case AI_VS_AI:
			currentGame = userService.getCurrentUser().getCurrentAIvsAIGame();
			break;
		case ONLINE:
			currentGame = userService.getCurrentUser().getCurrentOnlineGame();
			break;
		default:
			throw new IllegalStateException("Unknown game type: " + gameType);
		}
		return currentGame;
	}

	private Game createGame(User user, GameType gameType) {
		Game newGame = Game.builder().blackPlayer(user).whitePlayer(user).boardSize(user.getSettings().getBoardSize()).type(gameType).build();
		
		newGame.setMoves(new HashSet<>());
		
		if (GameType.LOCAL.equals(gameType)) {
			user.setCurrentLocalGame(newGame);
		} else if (GameType.AI.equals(gameType)) {
			user.setCurrentAIGame(newGame);
		} else if (GameType.AI_VS_AI.equals(gameType)) {
			user.setCurrentAIvsAIGame(newGame);
		} else {
			throw new IllegalStateException("gameType not implemented:" + gameType);
		}
		
		return newGame;
	}
	
	private Game createGame(User blackPlayer, User whitePlayer, GameType gameType) {
		Game newGame = Game.builder().blackPlayer(blackPlayer).whitePlayer(whitePlayer).boardSize(blackPlayer.getSettings().getBoardSize()).type(gameType).build();
		
		newGame.setMoves(new HashSet<>());
		
		if (GameType.LOCAL.equals(gameType)) {
			blackPlayer.setCurrentLocalGame(newGame);
			whitePlayer.setCurrentLocalGame(newGame);
		} else if (GameType.AI.equals(gameType)) {
			blackPlayer.setCurrentAIGame(newGame);
			whitePlayer.setCurrentAIGame(newGame);
		} else if (GameType.AI_VS_AI.equals(gameType)) {
			blackPlayer.setCurrentAIvsAIGame(newGame);
			whitePlayer.setCurrentAIvsAIGame(newGame);
		} else if (GameType.ONLINE.equals(gameType)) {
			blackPlayer.setCurrentOnlineGame(newGame);
			whitePlayer.setCurrentOnlineGame(newGame);
		}
		
		return newGame;
	}
	
	private Move removeLastMove(Game game) {
		
		Move lastMove = getLastMove(game);
		
		if (lastMove != null) {
			game.getMoves().remove(lastMove);
			return lastMove;
		}
		
		return null;
	}

	private Move getLastMove(Game currentGame) {
		
		Optional<Move> lastMove = currentGame.getMoves().stream().sorted((m1, m2) -> - Integer.compare(m1.getNumber(), m2.getNumber())).findFirst();
		
		if (lastMove.isPresent()) {
			return lastMove.get();
		}

		return null;
	}

	public Set<Move> getWinningMoves(GameType gameType) {
		Game currentGame = getCurrentGame(gameType);
		return engineService.checkWin(new GameDTO(currentGame));
	}

}
