package fr.leblanc.gomoku.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.leblanc.gomoku.model.Game;
import fr.leblanc.gomoku.model.User;
import fr.leblanc.gomoku.service.UserService;
import fr.leblanc.gomoku.web.dto.HistoryGameDTO;
import fr.leblanc.gomoku.web.dto.PlayerDTO;

@Service
public class GameHistoryService {
	
	@Autowired
    private UserService userService;

	public List<HistoryGameDTO> getHistoryGames(User user) {
		List<Game> dbGames = user.getGames();
		dbGames.sort((g1, g2) -> g1.getDate() != null ? g1.getDate().compareTo(g2.getDate()) : -1);
		return dbGames.stream().map(this::createHistoryGameDTO).toList();
	}
	
	public void saveHistoryGame(Game game, User user) {
		List<Game> games = user.getGames();
		if (!games.contains(game)) {
			games.add(game);
			userService.save(user);
		}
	}

	private HistoryGameDTO createHistoryGameDTO(Game game) {
		return new HistoryGameDTO(game.getId(), game.getType(), createPlayerDTO(game.getBlackPlayer()),
				createPlayerDTO(game.getWhitePlayer()), createPlayerDTO(game.getWinner()), game.getDate());
	}
	
	private PlayerDTO createPlayerDTO(User user) {
		if (user != null) {
			return new PlayerDTO(user.getUsername(), user.getFirstName(), user.getLastName());
		}
		return null;
	}

}
