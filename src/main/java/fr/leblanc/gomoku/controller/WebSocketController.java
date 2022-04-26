package fr.leblanc.gomoku.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import fr.leblanc.gomoku.model.WebSocketMessage;

@Controller
public class WebSocketController
{
    @MessageMapping({ "/refresh" })
    @SendTo({ "/topic/public" })
    public WebSocketMessage refreshBoard(@Payload final WebSocketMessage boardMessage) {
        return boardMessage;
    }
}