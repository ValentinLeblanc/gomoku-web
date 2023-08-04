package fr.leblanc.gomoku.web.dto;

import fr.leblanc.gomoku.model.Move;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MoveDTO {

    private int number;
    private int columnIndex;
    private int rowIndex;
    private int color;

    public MoveDTO(Move move) {
    	columnIndex = move.getColumnIndex();
    	rowIndex = move.getRowIndex();
    	color = move.getColor();
    	number = move.getNumber();
    }
}
	