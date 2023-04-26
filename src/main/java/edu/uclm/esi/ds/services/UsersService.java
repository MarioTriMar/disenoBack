package edu.uclm.esi.ds.services;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import edu.uclm.esi.ds.dao.TokenDAO;
import edu.uclm.esi.ds.dao.UserDAO;
import edu.uclm.esi.ds.entities.Token;
import edu.uclm.esi.ds.entities.User;

@Service
public class UsersService {
	
	@Autowired
	private UserDAO userDAO;
	@Autowired
	private EmailService emailService;
	@Autowired
	private TokenDAO tokenDAO;
	
	public void register(String name, String email, String pwd1) throws IOException {
		User user = new User();
		user.setName(name);
		user.setEmail(email);
		user.setPwd(pwd1);
		Token token= new Token();
		token.setUser(user);
		this.userDAO.save(user);
		this.tokenDAO.save(token);
		this.emailService.sendConfirmationEmail(user, token);
		
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

	public void validate(String tokenId) {
		Optional<Token> optToken=this.tokenDAO.findById(tokenId);
		if (!optToken.isPresent())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encuentra el token o ha caducado");
		Token token = optToken.get();
		long time = System.currentTimeMillis();
		if (time-token.getCreationTime()>24*60*60*1000)
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encuentra el token o ha caducado");
		User user = token.getUser();
		user.setValidationDate(time);
		this.userDAO.save(user);
		this.tokenDAO.delete(token);

	}

}
