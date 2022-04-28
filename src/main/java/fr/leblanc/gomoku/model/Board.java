package fr.leblanc.gomoku.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class Board
{
    private Move[][] cells;
    
    public void addCell(final Move cell) {
        this.cells[cell.getColumnIndex()][cell.getRowIndex()] = cell;
    }
    
    public int getRowNumber() {
        return this.getCells().length;
    }
    
    public int getColumnNumber() {
        return (this.getCells().length == 0) ? 0 : this.getCells()[0].length;
    }
    
    public Move getCell(final int column, final int row) {
        return this.getCells()[row][column];
    }
    
}