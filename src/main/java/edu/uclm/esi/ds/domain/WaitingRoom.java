package edu.uclm.esi.ds.domain;

import java.util.concurrent.ConcurrentHashMap;

import edu.uclm.esi.ds.domain.Match;

public class WaitingRoom {

	private ConcurrentHashMap<String, Match> matches;
	
	public WaitingRoom() {
		this.matches=new ConcurrentHashMap<>();
	}
	
	public Match findMatch(String juego, String player) {
		
		Match match = this.matches.get(juego);
		if (match==null) {
			match = new Match();
			match.addPlayer(player);
			this.matches.put(juego, match);
		}else {
			match.addPlayer(player);
			match=this.matches.get(juego);
		}
		
		//quizas borrar la partida del diccionario
		return match;
	}

}
