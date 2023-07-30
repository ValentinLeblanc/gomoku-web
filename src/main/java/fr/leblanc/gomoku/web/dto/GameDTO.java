package fr.leblanc.gomoku.web.dto;

import java.util.Set;
import java.util.stream.Collectors;

import fr.leblanc.gomoku.model.Game;
import fr.leblanc.gomoku.model.UserSettings;

public record GameDTO(Long id, int boardSize, Set<MoveDTO> moves, UserSettingsDTO settings) {

	public GameDTO(Game game) {
		this(game.getId(), game.getBoardSize(), game.getMoves().stream().map(MoveDTO::new).collect(Collectors.toSet()), null);
	}
	
	public GameDTO(Game game, UserSettings settings) {
		this(game.getId(), game.getBoardSize(), game.getMoves().stream().map(MoveDTO::new).collect(Collectors.toSet()), new UserSettingsDTO(settings));
	}

}
