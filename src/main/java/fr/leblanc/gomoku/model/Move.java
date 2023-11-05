package fr.leblanc.gomoku.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Builder;
import lombok.Data;

@Entity
@Table(name = "move")
@Data
@Builder
public class Move
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int number;
    private int columnIndex;
    private int rowIndex;
    private int color;
    
    public Move() {
    }
    
    public Move(final Long id, final int number, final int columnIndex, final int rowIndex, final int color) {
        this.id = id;
        this.number = number;
        this.columnIndex = columnIndex;
        this.rowIndex = rowIndex;
        this.color = color;
    }

	public Move(Move move) {
		this.color = move.color;
		this.number = move.number;
		this.columnIndex = move.columnIndex;
		this.rowIndex = move.rowIndex;
	}
}