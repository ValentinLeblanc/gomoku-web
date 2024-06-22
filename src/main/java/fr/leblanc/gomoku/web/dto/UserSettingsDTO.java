package fr.leblanc.gomoku.web.dto;

public record UserSettingsDTO(int boardSize, boolean strikeEnabled, boolean displayAnalysis, int minMaxDepth,
		int strikeDepth, int minMaxExtent, int strikeTimeout) {
}
