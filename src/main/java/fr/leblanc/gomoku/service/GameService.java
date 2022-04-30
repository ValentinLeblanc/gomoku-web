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
	
	public Game localGame() {
		
		Game currentGame = userService.getCurrentUser().getCurrentGame();
		
		if (currentGame == null) {
			currentGame = createLocalGame(userService.getCurrentUser());
			gameRepository.save(currentGame);
		}
		
		return currentGame;
		
	}
	
	public Game resetGame() {
		
		Game currentGame = userService.getCurrentUser().getCurrentGame();
		
		if (currentGame.getType() == GameType.LOCAL) {
			gameRepository.delete(currentGame);
			return gameRepository.save(createLocalGame(userService.getCurrentUser()));
		}
		
		return null;
	}

	public Set<Move> addMove(int columnIndex, int rowIndex) {
		
		Set<Move> result = new HashSet<>();	
		
		Game currentGame = userService.getCurrentUser().getCurrentGame();

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
		
		if (winningMoves != null && !winningMoves.isEmpty()) {
			currentGame.setWinCombination(winningMoves);
			result.addAll(winningMoves);
		} else {
			result.add(newMove);
		}
		
		gameRepository.save(currentGame);
		
		return result;
	}
	
	public Set<Move> undoMove() {
		
		Game currentGame = userService.getCurrentUser().getCurrentGame();
		
		if (currentGame.getType() == GameType.LOCAL) {
			
			Set<Move> result = new HashSet<>();	
			
			Move removedMove = removeLastMove(currentGame);
			
			if (!currentGame.getWinCombination().isEmpty()) {
				currentGame.getWinCombination().clear();
			}
			
			gameRepository.save(currentGame);
			
			result.add(removedMove);
			
			return result;
		} else {
			throw new IllegalStateException("Only supported for local game");
		}
		
	}

	private Game createLocalGame(User user) {
		Game localGame =  Game.builder().blackPlayer(user).whitePlayer(user).boardSize(user.getSettings().getBoardSize()).type(GameType.LOCAL).build();
		user.setCurrentGame(localGame);
		return localGame;
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
