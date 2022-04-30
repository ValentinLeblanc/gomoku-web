package fr.leblanc.gomoku.web.dto;

import java.util.HashSet;
import java.util.Set;

import fr.leblanc.gomoku.model.Game;
import fr.leblanc.gomoku.model.Move;
import lombok.Data;

@Data
public class GameDto {

	private int boardSize;
	
	private Set<MoveDto> moves = new HashSet<>();
	
	public GameDto(Game game) {
		boardSize = game.getBoardSize();
		
		for (Move move : game.getMoves()) {
			moves.add(new MoveDto(move));
		}
	}
}
