package edu.uclm.esi.ds.dao;



import org.springframework.data.jpa.repository.JpaRepository;

import edu.uclm.esi.ds.entities.Token;


public interface TokenDAO extends JpaRepository<Token, String>{
	
}
