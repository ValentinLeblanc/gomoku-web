package fr.leblanc.gomoku.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.json.JSONObject;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "move")
@Builder
@Data
@EqualsAndHashCode
public class Move
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int number;
    private int columnIndex;
    private int rowIndex;
    private GomokuColor color;
    
    public JSONObject toJSON() {
        final JSONObject jsonCell = new JSONObject();
        jsonCell.put("number", this.number);
        jsonCell.put("column", this.columnIndex);
        jsonCell.put("row", this.rowIndex);
        jsonCell.put("color", this.color.toString());
        return jsonCell;
    }
    
    public Move() {
    }
    
    public Move(final Long id, final int number, final int columnIndex, final int rowIndex, final GomokuColor color) {
        this.id = id;
        this.number = number;
        this.columnIndex = columnIndex;
        this.rowIndex = rowIndex;
        this.color = color;
    }
}