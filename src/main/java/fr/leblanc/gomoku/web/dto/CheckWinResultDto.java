package fr.leblanc.gomoku.web.dto;

import java.util.HashSet;
import java.util.Set;

import lombok.Data;

@Data
public class CheckWinResultDto {

	private boolean isWin = false;
	
	private Set<MoveDto> winMoves = new HashSet<>();
}
