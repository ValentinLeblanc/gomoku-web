package fr.leblanc.gomoku.model;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "move")
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public int getColumnIndex() {
		return columnIndex;
	}

	public void setColumnIndex(int columnIndex) {
		this.columnIndex = columnIndex;
	}

	public int getRowIndex() {
		return rowIndex;
	}

	public void setRowIndex(int rowIndex) {
		this.rowIndex = rowIndex;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	@Override
	public int hashCode() {
		return Objects.hash(color, columnIndex, id, number, rowIndex);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Move other = (Move) obj;
		return color == other.color && columnIndex == other.columnIndex && Objects.equals(id, other.id)
				&& number == other.number && rowIndex == other.rowIndex;
	}

	@Override
	public String toString() {
		return "Move [id=" + id + ", number=" + number + ", columnIndex=" + columnIndex + ", rowIndex=" + rowIndex
				+ ", color=" + color + "]";
	}

	public static Move build() {
		return new Move();
	}

	public Move color(int color) {
		this.color = color;
		return this;
	}

	public Move columnIndex(int columnIndex) {
		this.columnIndex = columnIndex;
		return this;
	}
	
	public Move rowIndex(int rowIndex) {
		this.rowIndex = rowIndex;
		return this;
	}

	public Move number(int number) {
		this.number = number;
		return this;
	}
	
	
}