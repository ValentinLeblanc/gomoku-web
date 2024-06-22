package fr.leblanc.gomoku.web.dto;

import java.util.Set;
import java.util.stream.Collectors;

import fr.leblanc.gomoku.model.Game;
import fr.leblanc.gomoku.model.UserSettings;

public record GameDTO(Long id, Integer boardSize, Set<MoveDTO> moves, Boolean strikeEnabled, Boolean displayAnalysis,
		Integer minMaxDepth, Integer strikeDepth, Integer minMaxExtent, Integer strikeTimeout) {

	public GameDTO(Game game) {
		this(game.getId(), game.getBoardSize(), game.getMoves().stream().map(MoveDTO::new).collect(Collectors.toSet()),
				null, null, null, null, null, null);
	}

	public GameDTO(Game game, UserSettings settings) {
		this(game.getId(), game.getBoardSize(), game.getMoves().stream().map(MoveDTO::new).collect(Collectors.toSet()),
				settings.isStrikeEnabled(), settings.isDisplayAnalysis(), settings.getMinMaxDepth(),
				settings.getStrikeDepth(), settings.getMinMaxExtent(), settings.getStrikeTimeout());
	}

}
