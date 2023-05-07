package edu.uclm.esi.ds.domain;

import java.util.concurrent.ConcurrentHashMap;

import edu.uclm.esi.ds.domain.Match;

public class WaitingRoom {

	private ConcurrentHashMap<String, Match> matches;
	
	public WaitingRoom() {
		this.matches=new ConcurrentHashMap<>();
	}
	
	public Match findMatch(String juego, String player, String idPlayer) {
		
		Match match = this.matches.get(juego);
		if (match==null) {
			match = new Match();
			match.addPlayer(player, idPlayer);
			this.matches.put(juego, match);
		}else {
			match.addPlayer(player, idPlayer);
			match=this.matches.get(juego);
		}
		
		//quizas borrar la partida del diccionario
		return match;
	}

}
