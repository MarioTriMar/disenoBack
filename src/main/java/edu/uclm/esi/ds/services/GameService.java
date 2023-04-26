package edu.uclm.esi.ds.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import edu.uclm.esi.ds.controller.Manager;
import edu.uclm.esi.ds.domain.Board;
import edu.uclm.esi.ds.domain.Match;
import edu.uclm.esi.ds.domain.WaitingRoom;


@Service
public class GameService {

	private ConcurrentHashMap<String, Match> matches;

	private WaitingRoom waitingRoom;
	
	public GameService() {
		this.waitingRoom=new WaitingRoom();
		this.matches=new ConcurrentHashMap<>();
	}
	
	public Match requestGame(String juego, String player) {
		Match match=this.waitingRoom.findMatch(juego, player);
		if(match.isReady())
			this.matches.put(match.getId(), match);
		return match;
	}

	public Map<String, Object> makeMovement(Map<String, ?> info) {
        Match match=this.matches.get(info.get("idPartida"));
        String httpSessionRival=match.getHttpSessionRival(info.get("idJugador").toString());
        Object boarde=info.get("board");
        ArrayList<ArrayList<Integer>> boardList = (ArrayList<ArrayList<Integer>>) info.get("board");
        // Convertir el ArrayList en una matriz de enteros de 2 dimensiones
        int[][] board = new int[boardList.size()][boardList.get(0).size()];
        for (int i = 0; i < boardList.size(); i++) {
            ArrayList<Integer> row = boardList.get(i);
            for (int j = 0; j < row.size(); j++) {
                board[i][j] = row.get(j);
            }
        }

        List<Map<String, Integer>> movement = (List<Map<String, Integer>>) info.get("movement");
        int i1=movement.get(0).get("i1");
        int j1=movement.get(0).get("j1");
        int i2=movement.get(1).get("i2");
        int j2=movement.get(1).get("j2");
        board[i1][j1]=0;
        board[i2][j2]=0;
        match.setBoard(board, info.get("idJugador").toString());

        Map<String, Object> matchMove = new HashMap<>();
        matchMove.put("boards", match.getBoard(info.get("idJugador").toString()));

        actualizarWebSocket(httpSessionRival, match.getBoard(info.get("idJugador").toString()));

        return matchMove;

    }
	private void actualizarWebSocket(String httpSessionRival, Board board) {
        WebSocketSession session=Manager.get().findWrapperSessionByHttp(httpSessionRival).getWebSocketSession();

        JSONObject jso = new JSONObject();
        jso.put("type", "movement");
        jso.put("boards", board.getDigits());

        TextMessage message = new TextMessage(jso.toString());
        try {
            session.sendMessage(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
}
