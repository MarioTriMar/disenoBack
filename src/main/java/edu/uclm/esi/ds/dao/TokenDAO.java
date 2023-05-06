package edu.uclm.esi.ds.dao;



import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.uclm.esi.ds.entities.Token;
import edu.uclm.esi.ds.entities.User;


public interface TokenDAO extends JpaRepository<Token, String>{
	Optional<Token> findByUser(User user);
}
