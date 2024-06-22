package fr.leblanc.gomoku.web.dto;

import java.util.Set;
import java.util.stream.Collectors;

import fr.leblanc.gomoku.model.Game;

public record GameDTO(Long id, int boardSize, Set<MoveDTO> moves, UserSettingsDTO userSettingsDTO) {

	public GameDTO(Game game) {
		this(game.getId(), game.getBoardSize(), game.getMoves().stream().map(MoveDTO::new).collect(Collectors.toSet()), null);
	}

	public GameDTO(Game game, UserSettingsDTO userSettingsDTO) {
		this(game.getId(), game.getBoardSize(), game.getMoves().stream().map(MoveDTO::new).collect(Collectors.toSet()), userSettingsDTO);
	}

}
