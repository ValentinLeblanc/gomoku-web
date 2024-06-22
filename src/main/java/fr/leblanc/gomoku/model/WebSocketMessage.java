package fr.leblanc.gomoku.model;

import java.util.Objects;

public class WebSocketMessage
{
	private Long gameId;
    private MessageType type;
    private Object content;
    
	public Long getGameId() {
		return gameId;
	}
	public void setGameId(Long gameId) {
		this.gameId = gameId;
	}
	public MessageType getType() {
		return type;
	}
	public void setType(MessageType type) {
		this.type = type;
	}
	public Object getContent() {
		return content;
	}
	public void setContent(Object content) {
		this.content = content;
	}
	@Override
	public int hashCode() {
		return Objects.hash(content, gameId, type);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WebSocketMessage other = (WebSocketMessage) obj;
		return Objects.equals(content, other.content) && Objects.equals(gameId, other.gameId) && type == other.type;
	}
	@Override
	public String toString() {
		return "WebSocketMessage [gameId=" + gameId + ", type=" + type + ", content=" + content + "]";
	}
	
	public static WebSocketMessage build() {
		return new WebSocketMessage();
	}
	public WebSocketMessage gameId(Long gameId) {
		this.gameId = gameId;
		return this;
	}
	public WebSocketMessage type(MessageType type) {
		this.type = type;
		return this;
	}
	public WebSocketMessage content(Object content) {
		this.content = content;
		return this;
	}
    
}