package fr.leblanc.gomoku.model;

import lombok.Data;

@Data
public class WebSocketMessage
{
	private Long gameId;
    private MessageType type;
    private Object content;
    
}