package fr.leblanc.gomoku.web.dto;

import java.util.Set;

public record CheckWinResultDTO (Set<MoveDTO> winMoves) {
	public boolean isWin() {
		return !winMoves.isEmpty();
	}
}
