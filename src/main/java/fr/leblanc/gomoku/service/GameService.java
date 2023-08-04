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
	private WebSocketController webSocketService;
	
	private Map<Long, Stack<Move>> undoMoveStack = new HashMap<>();
	
	public Game getCurrentGame(GameType gameType) {
		
		Game currentGame = findCurrentGame(gameType);
		
		if (currentGame == null) {
			currentGame = createGame(userService.getCurrentUser(), gameType);
			gameRepository.save(currentGame);
		}
		
		return currentGame;
	}

	public Game resetGame(GameType gameType) {
		
		Game currentGame = findCurrentGame(gameType);
		
		if (currentGame == null) {
			throw new IllegalStateException(GAME_NOT_FOUND);
		}
		
		deleteGame(currentGame);
		
		return gameRepository.save(createGame(userService.getCurrentUser(), gameType));
	}
	
	public void deleteGame(Game game) {
		engineService.deleteGame(game.getId());
		gameRepository.delete(game);
	}

	public Move addMove(GameType gameType, int columnIndex, int rowIndex) {
		
		Game currentGame = findCurrentGame(gameType);

		if (currentGame == null) {
			throw new IllegalStateException(GAME_NOT_FOUND);
		}
		
		if (!currentGame.getWinCombination().isEmpty()) {
			throw new IllegalStateException("Game is already over");
		}
		
		if (currentGame.getMove(columnIndex, rowIndex) != null) {
			throw new IllegalStateException("Move already set");
		}
		
		undoMoveStack.computeIfAbsent(currentGame.getId(), k -> new Stack<>()).clear();
		
		int color = extractPlayingColor(currentGame);
		Move newMove = Move.builder().number(currentGame.getMoves().size()).columnIndex(columnIndex).rowIndex(rowIndex).color(color).build();
		currentGame.getMoves().add(newMove);
		checkWin(currentGame);
		gameRepository.save(currentGame);
		return newMove;
	}

	private void checkWin(Game currentGame) {
		try {
			Set<Move> winningMoves = engineService.checkWin(new GameDTO(currentGame));
			if (winningMoves != null && !winningMoves.isEmpty()) {
				currentGame.setWinCombination(winningMoves);
				webSocketService.sendMessage(WebSocketMessage.builder().gameId(currentGame.getId()).type(MessageType.IS_WIN).content(winningMoves).build());
			}
		} catch (ResourceAccessException e) {
			log.error("Could not access Gomoku Engine : " + e.getMessage());
		}
	}

	private int extractPlayingColor(Game currentGame) {
		return currentGame.getMoves().size() % 2 == 0 ? GomokuColor.BLACK.toNumber() : GomokuColor.WHITE.toNumber();
	}

	public Set<Move> undoMove(GameType gameType) {
		
		Game currentGame = findCurrentGame(gameType);

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
		
		Game currentGame = findCurrentGame(gameType);

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
		
		Game currentGame = findCurrentGame(gameType);

		if (currentGame == null) {
			throw new IllegalStateException(GAME_NOT_FOUND);
		}
		
		if (currentGame.getType() == GameType.ONLINE) {
			throw new IllegalStateException(NOT_SUPPORTED_FOR_ONLINE_GAME);
		} 
		
		GameDTO gameDto = new GameDTO(currentGame);
		
		gameDto.setSettings(new UserSettingsDTO(userService.getCurrentUser().getSettings()));
		
		Move computedMove = engineService.computeMove(gameDto);
		
		if (computedMove != null && computedMove.getColumnIndex() != -1 && computedMove.getRowIndex() != -1) {
			return addMove(gameType, computedMove.getColumnIndex(), computedMove.getRowIndex());
		}
		
		throw new IllegalStateException("Move could not be computed");
	}

	public Double computeEvaluation(GameType gameType) {
		
		Game currentGame = findCurrentGame(gameType);
	
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

	public Move getLastMove(GameType gameType) {
		Game currentGame = findCurrentGame(gameType);
		
		if (currentGame == null) {
			throw new IllegalStateException(GAME_NOT_FOUND);
		}
		
		return getLastMove(currentGame);
		
	}

	public ResponseEntity<Resource> downloadGame(GameType gameType) {
		
		Game currentGame = findCurrentGame(gameType);
		
		byte[] buffer = new JSONObject(new GameDTO(currentGame)).toString().getBytes();
		
	    return ResponseEntity.ok()
	            .contentLength(buffer.length)
	            .contentType(MediaType.APPLICATION_OCTET_STREAM)
	            .body(new InputStreamResource(new ByteArrayInputStream(buffer)));
	}

	public void uploadGame(GameType gameType, GameDTO uploadedGame) {
		Game currentGame = findCurrentGame(gameType);
		
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

	private Game findCurrentGame(GameType gameType) {
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
		default:
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
		Game currentGame = findCurrentGame(gameType);
		return engineService.checkWin(new GameDTO(currentGame));
	}

}
