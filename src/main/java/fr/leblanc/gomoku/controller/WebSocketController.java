package fr.leblanc.gomoku.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import fr.leblanc.gomoku.model.WebSocketMessage;

@Controller
public class WebSocketController {
	
    private SimpMessagingTemplate template;
    
    public WebSocketController(SimpMessagingTemplate template) {
		super();
		this.template = template;
	}

	@MessageMapping({ "/refreshBoard" })
    @SendTo({ "/web/public" })
    public WebSocketMessage refreshBoard(@Payload final WebSocketMessage boardMessage) {
        return boardMessage;
    }
    
    public void sendMessage(final WebSocketMessage message) {
    	template.convertAndSend("/web/public", message);
    }
}