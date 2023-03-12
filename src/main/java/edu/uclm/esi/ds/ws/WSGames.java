package edu.uclm.esi.ds.ws;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class WSGames extends TextWebSocketHandler {
	private ArrayList<WebSocketSession> sessions = new ArrayList<>();
	//Cuando establezca la conexion
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		this.sessions.add(session);
	}

	//Cuando envie un mensaje desde el cliente
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		String payload=message.getPayload();
		JSONObject jso = new JSONObject(payload);
		String type=jso.getString("type");
		if (type.equals("MOVEMENT")) {
			this.move(jso);
		}else if(type.equals("CHAT")) {
			this.chat(jso);
		}else if(type.equals("BROADCAST")) {
			this.broadcast(jso);
		}else {
			this.send(session,"type", "ERROR", "Mensaje no reconocido");
		}
	}

	

	private void send(WebSocketSession session, String... tv) {
		JSONObject jso=new JSONObject();
		for(int i=0;i<tv.length;i=i+2) {
			jso.put(tv[i], tv[i+1]);
		}
		TextMessage message=new TextMessage(jso.toString());
		try {
			session.sendMessage(message);
		} catch (IOException e) {
			this.sessions.remove(session);
		}
	}

	private void chat(JSONObject jso) {
		
		
	}

	private void move(JSONObject jso) {
		
		
	}

	private void broadcast(JSONObject jso) {
		TextMessage message=new TextMessage(jso.getString("message"));
		for(WebSocketSession cliente : this.sessions) {
			Runnable r = new Runnable() {
				@Override
				public void run() {
					try {
						cliente.sendMessage(message);
					} catch (IOException e) {
						WSGames.this.sessions.remove(cliente);
					}				
				}
			};
			new Thread(r).start();
		}
	}

	//Cuando envie un binario desde el cliente
	@Override
	protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) {
	}
	//Si ocurre algun error
	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
	}
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		this.sessions.remove(session);
		JSONObject jso=new JSONObject();
		jso.put("type", "BYE");
		jso.put("message", "Un usuario se ha ido");
		this.broadcast(jso);
	}
}
