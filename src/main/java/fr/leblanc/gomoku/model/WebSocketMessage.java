package fr.leblanc.gomoku.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WebSocketMessage
{
	private Long gameId;
    private MessageType type;
    private Object content;
    
}