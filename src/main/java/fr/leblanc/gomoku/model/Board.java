package fr.leblanc.gomoku.model;

import org.json.JSONArray;
import org.json.JSONObject;

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
    
    public String toJSONString() {
        final JSONObject jsonBoard = new JSONObject();
        final JSONArray jsonCells = new JSONArray();
        for (int row = 0; row < this.getRowNumber(); ++row) {
            for (int column = 0; column < this.getColumnNumber(); ++column) {}
        }
        jsonBoard.put("cells", (Object)jsonCells);
        return jsonBoard.toString();
    }
}