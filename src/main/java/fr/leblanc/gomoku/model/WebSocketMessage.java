package fr.leblanc.gomoku.model;

import lombok.Data;

@Data
public class WebSocketMessage
{
    private MessageType type;
    private String content;
    
}