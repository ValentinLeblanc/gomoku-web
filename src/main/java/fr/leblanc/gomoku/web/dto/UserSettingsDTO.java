package fr.leblanc.gomoku.web.dto;

import fr.leblanc.gomoku.model.UserSettings;

public record UserSettingsDTO(int boardSize, boolean strikeEnabled, boolean displayAnalysis, int minMaxDepth,
		int strikeDepth, int minMaxExtent, int strikeTimeout) {

	public UserSettingsDTO(UserSettings settings) {
		this(settings.getBoardSize(), settings.isStrikeEnabled(), settings.isDisplayAnalysis(),
				settings.getMinMaxDepth(), settings.getMinMaxExtent(), settings.getStrikeDepth(),
				settings.getStrikeTimeout());
	}

}
