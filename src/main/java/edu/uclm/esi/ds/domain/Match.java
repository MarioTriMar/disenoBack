package edu.uclm.esi.ds.domain;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import edu.uclm.esi.ds.controller.Manager;

public class Match {

	private String id;
	private boolean ready;
	private List<String> players;
	private HashMap<String, Board> boards;
	
	//key:httpSession value:idBBDD
	private HashMap<String, String> idsPlayers;
	private HashMap<String, ArrayList<String>> movimientos;
	
	
	public Match() {
		this.id=UUID.randomUUID().toString();
		this.players=new ArrayList<>();
		this.boards = new HashMap<>();
		this.idsPlayers=new HashMap<>();
		this.movimientos=new HashMap<>();
	}
	
	public String getId() {
		return this.id;
	}

	public boolean isReady() {
		return this.ready;
	}

	public void setReady(boolean ready) {
		this.ready=ready;
		this.buildBoards();
		
	}

	private void buildBoards() {
		Board board = new Board();
		this.boards.put(this.players.get(0), board);
		Board boards = board.copy();
		this.boards.put(this.players.get(1), boards);
		
		
		
	}

	public void addPlayer(String player, String idPlayer) {
		this.players.add(player);
		this.idsPlayers.put(player, idPlayer);
		if (this.players.size()==2) {
			this.setReady(true);
			
			WebSocketSession session=Manager.get().findWrapperSessionByHttp(this.players.get(0)).getWebSocketSession();
			JSONObject jso = new JSONObject();
			jso.put("type", "matchReady");
			jso.put("boards", this.getBoards());
			jso.put("id", id);
			jso.put("player", this.players);
			jso.put("ready", this.ready);
			TextMessage message = new TextMessage(jso.toString());
			try {
				session.sendMessage(message);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public List<String> getPlayer(){
		return this.players;
	}
	
	public List<Board> getBoards(){
		return boards.values().stream().toList();
	}
	public Board getBoard(String httpSession){
		return boards.get(httpSession);
	}

	public String getHttpSessionRival(String httpSession) {
		if(players.get(0).equals(httpSession)) {
			return players.get(1);
		}else {
			return players.get(0);
		}
		
	}

	public void setBoard(int[][] board, String httpSession) {
		Board boardAux=this.boards.get(httpSession);
		boardAux.setDigits(board);
		this.boards.replace(httpSession, boardAux);
		
	}

	public List<String> getIdsPlayers() {
		return idsPlayers.values().stream().toList();
	}

	public void guardarMovimiento(String httpJugador, int i1, int j1, int i2, int j2) {
		String idJugador=this.idsPlayers.get(httpJugador);
		String movimientos = "{"+i1+","+j1+":"+i2+","+j2+"}";
		ArrayList<String> movimientosHechos=this.movimientos.get(idJugador);
		if(movimientosHechos==null) {
			ArrayList<String> movimiento=new ArrayList<>();
			movimiento.add(movimientos);
			this.movimientos.put(idJugador, movimiento);
		}else {
			movimientosHechos.add(movimientos);
			this.movimientos.put(idJugador, movimientosHechos);
		}
		
		
	}
	public String getIdPlayer(String httpSession) {
		return this.idsPlayers.get(httpSession);
	}
	
	public String getMovimientos() {
		return this.movimientos.toString();
	}
	
}
