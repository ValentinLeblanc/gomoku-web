package fr.leblanc.gomoku.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Builder
@Data
@EqualsAndHashCode
public class WebSocketMessage
{
    private MessageType type;
    private int number;
    private int columnIndex;
    private int rowIndex;
    private GomokuColor color;
    
    WebSocketMessage(final MessageType type, final int number, final int columnIndex, final int rowIndex, final GomokuColor color) {
        this.type = type;
        this.number = number;
        this.columnIndex = columnIndex;
        this.rowIndex = rowIndex;
        this.color = color;
    }
}