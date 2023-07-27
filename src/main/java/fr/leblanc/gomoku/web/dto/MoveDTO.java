package fr.leblanc.gomoku.web.dto;

import fr.leblanc.gomoku.model.Move;

public record MoveDTO(int number, int columnIndex, int rowIndex, int color) {

	public MoveDTO() {
		this(0, 0, 0, 0);
	}
	
    public MoveDTO(Move move) {
    	this(move.getNumber(), move.getColumnIndex(), move.getRowIndex(), move.getColor());
    }
}
	