package fr.leblanc.gomoku.service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import fr.leblanc.gomoku.model.Game;
import fr.leblanc.gomoku.model.GameType;
import fr.leblanc.gomoku.model.GomokuColor;
import fr.leblanc.gomoku.model.Move;
import fr.leblanc.gomoku.model.User;
import fr.leblanc.gomoku.repository.GameRepository;
import fr.leblanc.gomoku.web.dto.GameDto;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class GameService {

	@Autowired
	private GameRepository gameRepository;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private MoveService moveService;
	
	@Autowired
	private EngineService engineService;
	
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
			throw new IllegalStateException("Current user game doesn't exist");
		}
		
		gameRepository.delete(currentGame);
		
		return gameRepository.save(createGame(userService.getCurrentUser(), gameType));
	}

	public Set<Move> addMove(GameType gameType, int columnIndex, int rowIndex) {
		
		Game currentGame = findCurrentGame(gameType);

		if (currentGame == null) {
			throw new IllegalStateException("Current user game doesn't exist");
		}
		
		if (currentGame.getMove(columnIndex, rowIndex) != null) {
			return Collections.emptySet();
		}
		
		if (!currentGame.getWinCombination().isEmpty()) {
			return Collections.emptySet();
		}
		
		GomokuColor color = currentGame.getMoves().size() % 2 == 0 ? GomokuColor.BLACK : GomokuColor.WHITE;
		
		Move newMove = Move.builder().number(currentGame.getMoves().size()).columnIndex(columnIndex).rowIndex(rowIndex).color(color).build();
		
		currentGame.getMoves().add(newMove);
		
		Set<Move> winningMoves = null;
		
		try {
			winningMoves = engineService.checkWin(currentGame);
		} catch (ResourceAccessException e) {
			log.error("Could not access Gomoku Engine : " + e.getMessage());
		}
		
		Set<Move> result = new HashSet<>();	
		
		result.addAll(currentGame.getMoves());
		
		if (winningMoves != null && !winningMoves.isEmpty()) {
			currentGame.setWinCombination(winningMoves);
			
			result.addAll(winningMoves);
		}
		
		gameRepository.save(currentGame);
		
		return result;
	}

	public Set<Move> undoMove(GameType gameType) {
		
		Game currentGame = findCurrentGame(gameType);

		if (currentGame == null) {
			throw new IllegalStateException("Current user game doesn't exist");
		}
		
		if (currentGame.getType() == GameType.ONLINE) {
			throw new IllegalStateException("Not supported for online game");
		} 
		
		removeLastMove(currentGame);
		
		if (!currentGame.getWinCombination().isEmpty()) {
			currentGame.getWinCombination().clear();
		}
		
		gameRepository.save(currentGame);
		
		return currentGame.getMoves();
		
	}

	public Set<Move> computeMove(GameType gameType, GameDto game) {
		Move computedMove = engineService.computeMove(game);
		
		if (computedMove != null && computedMove.getColumnIndex() != -1 && computedMove.getRowIndex() != -1) {
			return addMove(gameType, computedMove.getColumnIndex(), computedMove.getRowIndex());
		}
		
		return Collections.emptySet();
	}

	public Double computeEvaluation(GameType gameType) {
		
		Game currentGame = findCurrentGame(gameType);
	
		if (currentGame == null) {
			throw new IllegalStateException("Current user game doesn't exist");
		}
		
		return engineService.computeEvaluation(new GameDto(currentGame));
	}

	public void stopComputation() {
		engineService.stopComputation();
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
		default:
		}
		return currentGame;
	}

	private Game createGame(User user, GameType gameType) {
		Game newGame = Game.builder().blackPlayer(user).whitePlayer(user).boardSize(user.getSettings().getBoardSize()).type(gameType).build();
		
		if (GameType.LOCAL.equals(gameType)) {
			user.setCurrentLocalGame(newGame);
		} else if (GameType.AI.equals(gameType)) {
			user.setCurrentAIGame(newGame);
		}
		
		return newGame;
	}
	
	private Move removeLastMove(Game game) {
		
		Move lastMove = game.getMove(game.getMoves().size() - 1);
		
		if (lastMove != null) {
			game.getMoves().remove(lastMove);
			lastMove.setColor(GomokuColor.NONE);
			moveService.delete(lastMove);
			return lastMove;
		}
		
		return null;
	}

}
