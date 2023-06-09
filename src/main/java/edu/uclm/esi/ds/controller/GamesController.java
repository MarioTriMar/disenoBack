package edu.uclm.esi.ds.controller;

import java.util.ArrayList;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import edu.uclm.esi.ds.domain.Board;
import edu.uclm.esi.ds.domain.Match;
import edu.uclm.esi.ds.services.GameService;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("games")
@CrossOrigin("*")
public class GamesController {
	
	@Autowired
	private GameService gamesService;
	
	@GetMapping("/requestGame")
	public Match requestGame(HttpSession httpSession, @RequestParam String juego, @RequestParam String player, @RequestParam String idPlayer) {
		if (!juego.equals("nm"))
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encuentra ese juego");
		return this.gamesService.requestGame(juego, player, idPlayer);
	}
	
	@PutMapping("/makeMovement")
	public Map<String, Object> makeMovement(@RequestBody Map<String, Object> info) {
        return this.gamesService.makeMovement(info);
    }
	
	@PutMapping("/addRow")
	public Map<String, Object> addRow(@RequestBody Map<String, Object> info){
		return this.gamesService.addRow(info);
	}
	
	
	@PutMapping("/win")
	public void win(@RequestBody Map<String, Object> info){
		this.gamesService.win(info);
	}
	
	@PostMapping("/rendirse")
	public void rendirse(@RequestBody Map<String, Object> info) {
		this.gamesService.rendirse(info);
	}
	
	@PutMapping("/quitarFichas")
	public void quitarFichas(@RequestBody String idPartida){
		this.gamesService.quitarFichas(idPartida);
	}
}
