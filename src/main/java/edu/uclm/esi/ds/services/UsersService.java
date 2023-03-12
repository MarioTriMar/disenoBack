package edu.uclm.esi.ds.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import edu.uclm.esi.ds.dao.UserDAO;
import edu.uclm.esi.ds.entities.User;

@Service
public class UsersService {
	
	@Autowired
	private UserDAO userDAO;
	
	public void register(String name, String email, String pwd1) {
		User user = new User();
		user.setName(name);
		user.setEmail(email);
		user.setPwd(pwd1);
		
		this.userDAO.save(user);
		
	}

	public void login(String name, String pwd) {
		User user = this.userDAO.findByName(name);
		if (user==null) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Credenciales inválidas");
		}
		String pwdEncripted = org.apache.commons.codec.digest.DigestUtils.sha512Hex(pwd);
		if (!user.getPwd().equals(pwdEncripted ))
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Credenciales inválidas");
	}

}
