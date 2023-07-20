package fr.leblanc.gomoku.web.dto;

import java.util.HashSet;
import java.util.Set;

import lombok.Data;

@Data
public class CheckWinResult {
	private Set<MoveDto> winMoves = new HashSet<>();
	
	public boolean isWin() {
		return !winMoves.isEmpty();
	}
}
