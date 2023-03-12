package edu.uclm.esi.ds.services;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

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
	
}
