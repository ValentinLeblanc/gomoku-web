package fr.leblanc.gomoku.web.dto;

import java.util.HashSet;
import java.util.Set;

import fr.leblanc.gomoku.model.Game;
import fr.leblanc.gomoku.model.Move;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameDto {

	private int boardSize;
	
	private Set<MoveDto> moves = new HashSet<>();
	
	private SettingsDto settings;
	
	public GameDto(Game game) {
		boardSize = game.getBoardSize();
		
		for (Move move : game.getMoves()) {
			moves.add(new MoveDto(move));
		}
	}
}
