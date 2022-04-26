package fr.leblanc.gomoku.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketMessageConfig implements WebSocketMessageBrokerConfigurer
{
    public void configureMessageBroker(final MessageBrokerRegistry config) {
        config.setApplicationDestinationPrefixes(new String[] { "/app" });
        config.enableSimpleBroker(new String[] { "/topic" });
    }
    
    public void registerStompEndpoints(final StompEndpointRegistry registry) {
        registry.addEndpoint(new String[] { "/board-moves" }).withSockJS();
        registry.addEndpoint(new String[] { "/online-boards" }).withSockJS();
    }
}