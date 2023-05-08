package edu.uclm.esi.ds.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;



import edu.uclm.esi.ds.controller.Manager;
import edu.uclm.esi.ds.dao.ResultadoDAO;
import edu.uclm.esi.ds.dao.UserDAO;
import edu.uclm.esi.ds.domain.Board;
import edu.uclm.esi.ds.domain.Match;
import edu.uclm.esi.ds.domain.WaitingRoom;
import edu.uclm.esi.ds.entities.Resultado;
import edu.uclm.esi.ds.entities.User;


@Service
public class GameService {

	private ConcurrentHashMap<String, Match> matches;

	private WaitingRoom waitingRoom;
	
	@Autowired
	private UserDAO userDAO;
	@Autowired
	private ResultadoDAO resultadoDAO;
	
	public GameService() {
		this.waitingRoom=new WaitingRoom();
		this.matches=new ConcurrentHashMap<>();
	}
	
	public Match requestGame(String juego, String player, String idPlayer) {
		Match match=this.waitingRoom.findMatch(juego, player, idPlayer);
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
        match.guardarMovimiento(info.get("idJugador").toString(), i1, j1, i2, j2);
        match.setBoard(board, info.get("idJugador").toString());

        Map<String, Object> matchMove = new HashMap<>();
        matchMove.put("boards", match.getBoard(info.get("idJugador").toString()));

        actualizarWebSocket(httpSessionRival, match.getBoard(info.get("idJugador").toString()), "movement");

        return matchMove;

    }
	private void actualizarWebSocket(String httpSessionRival, Board board, String type) {
        WebSocketSession session=Manager.get().findWrapperSessionByHttp(httpSessionRival).getWebSocketSession();

        JSONObject jso = new JSONObject();
        jso.put("type", type);
        jso.put("boards", board.getDigits());

        TextMessage message = new TextMessage(jso.toString());
        try {
            session.sendMessage(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	public Map<String, Object> addRow(Map<String, Object> info) {
		Match match=this.matches.get(info.get("idPartida"));
        String httpSessionRival=match.getHttpSessionRival(info.get("idJugador").toString());
        Board board = match.getBoard(info.get("idJugador").toString());
        board.addRow();
        
        Map<String, Object> matchMove = new HashMap<>();
        matchMove.put("boards", board);
        
        actualizarWebSocket(httpSessionRival, board, "addRow");
        
		return matchMove;
	}

	public void win(Map<String, Object> info) {
		Match match=this.matches.get(info.get("idPartida"));
        String httpSessionRival=match.getHttpSessionRival(info.get("idJugador").toString());
        avisarResultado(httpSessionRival, "perdido");
        Resultado resultado=new Resultado();
        resultado.setIdPartida(match.getId());
        resultado.setIdGanador(match.getIdPlayer(info.get("idJugador").toString()));
        resultado.setIdPerdedor(match.getIdPlayer(httpSessionRival));
        resultado.setMovimientos(match.getMovimientos());
        this.resultadoDAO.save(resultado); 
	}

	private void avisarResultado(String httpSessionRival, String resultado) {
		WebSocketSession session=Manager.get().findWrapperSessionByHttp(httpSessionRival).getWebSocketSession();
        JSONObject jso = new JSONObject();
        jso.put("type", resultado);
        TextMessage message = new TextMessage(jso.toString());
        try {
            session.sendMessage(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
		
	}

	public void quitarFichas(String idPartida) {
		Match match=this.matches.get(idPartida);
		
		for(int i=0; i<match.getIdsPlayers().size();i++) {
			String id=match.getIdsPlayers().get(i);
			Optional<User> user=this.userDAO.findById(id);
			int fichas = user.get().getFichas();
			user.get().setFichas(--fichas);
			this.userDAO.save(user.get());
		}	
	}

	public void rendirse(Map<String, Object> info) {
		Match match=this.matches.get(info.get("idPartida"));
        String httpSessionRival=match.getHttpSessionRival(info.get("idJugador").toString());
        avisarResultado(httpSessionRival, "ganado");
        Resultado resultado=new Resultado();
        resultado.setIdPartida(match.getId());
        resultado.setIdGanador(match.getIdPlayer(httpSessionRival));
        resultado.setIdPerdedor(match.getIdPlayer(info.get("idJugador").toString()));
        resultado.setMovimientos(match.getMovimientos());
        this.resultadoDAO.save(resultado);   
	}
	
}
