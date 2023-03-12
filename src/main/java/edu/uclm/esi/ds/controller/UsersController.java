package edu.uclm.esi.ds.controller;

import java.util.Map;
import java.util.Optional;

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

import edu.uclm.esi.ds.domain.Match;
import edu.uclm.esi.ds.entities.User;
import edu.uclm.esi.ds.services.GameService;
import edu.uclm.esi.ds.services.UsersService;

@RestController
@RequestMapping("users")
@CrossOrigin("*")
public class UsersController {
	
	@Autowired
	private UsersService usersService;
	
	@PostMapping("/register")
	public void register(@RequestBody Map<String, Object> info) {
		String pwd1 = info.get("pwd1").toString();
		String pwd2 = info.get("pwd2").toString();
		if (!pwd1.equals(pwd2))
			throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Las contraseñas no coinciden");
		
		String name= info.get("name").toString();
		String email = info.get("email").toString();
		try {
			this.usersService.register(name, email, pwd1);
		}catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.CONFLICT);
		}
		
	}
	
	@PutMapping("/login")
	public void login(@RequestBody Map<String, Object> creedenciales) {
		String name = creedenciales.get("name").toString();
		String pwd1 = creedenciales.get("pwd1").toString();
		try {
			this.usersService.login(name, pwd1);
		}catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN);
		}
		
	}
}
