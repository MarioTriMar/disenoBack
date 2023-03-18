package edu.uclm.esi.ds.ws;

import org.springframework.web.socket.WebSocketSession;

public class Wrapper {
	private WebSocketSession webSocketSession;
	private String httpSessionId;
	
	public WebSocketSession getWebSocketSession() {
		return webSocketSession;
	}
	public void setWebSocketSession(WebSocketSession webSocketSession) {
		this.webSocketSession = webSocketSession;
	}
	public String getHttpSessionId() {
		return httpSessionId;
	}
	public void setUaSessionId(String httpSessionId) {
		this.httpSessionId = httpSessionId;
	}
	
	
}
