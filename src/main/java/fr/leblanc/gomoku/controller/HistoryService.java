package fr.leblanc.gomoku.controller;

import java.util.List;

import org.springframework.stereotype.Service;

import fr.leblanc.gomoku.model.Game;
import fr.leblanc.gomoku.model.GameType;
import fr.leblanc.gomoku.model.User;
import fr.leblanc.gomoku.service.GameService;
import fr.leblanc.gomoku.service.UserService;
import fr.leblanc.gomoku.web.dto.HistoryGameDTO;
import fr.leblanc.gomoku.web.dto.PlayerDTO;

@Service
public class HistoryService {
	
    private UserService userService;
	
	private GameService gameService;
	
	public HistoryService(UserService userService, GameService gameService) {
		super();
		this.userService = userService;
		this.gameService = gameService;
	}

	public List<HistoryGameDTO> getUserHistory(String username) {
		User user = userService.findUserByUsername(username);
		List<Game> dbGames = user.getGames();
		dbGames.sort((g1, g2) -> g1.getDate() != null ? g1.getDate().compareTo(g2.getDate()) : -1);
		return dbGames.stream().map(this::createHistoryGameDTO).toList();
	}
	
	public void saveGameInHistory(Long gameId, String name) {
		User user = userService.getCurrentUser();
		List<Game> games = user.getGames();
		Game originalGame = gameService.findById(gameId);
		Game historyGame = new Game(originalGame);
		historyGame.setType(GameType.HISTORY);
		historyGame.setName(name);
		historyGame = gameService.save(historyGame);
		games.add(historyGame);
		userService.save(user);
	}
	
	public void viewGameFromHistory(Long gameId) {
		User user = userService.getCurrentUser();
		Game game = gameService.findById(gameId);
		user.setCurrentHistoryGame(game);
		userService.save(user);
	}

	private HistoryGameDTO createHistoryGameDTO(Game game) {
		return new HistoryGameDTO(game.getId(), game.getName(), game.getType(), createPlayerDTO(game.getBlackPlayer()),
				createPlayerDTO(game.getWhitePlayer()), createPlayerDTO(game.getWinner()), game.getDate());
	}
	
	private PlayerDTO createPlayerDTO(User user) {
		if (user != null) {
			return new PlayerDTO(user.getUsername(), user.getFirstName(), user.getLastName());
		}
		return null;
	}

	public void deleteGameFromHistory(Long id) {
		User user = userService.getCurrentUser();
		if (user.getGames().removeIf(g -> g.getId().equals(id))) {
			userService.save(user);
		}
	}

}
