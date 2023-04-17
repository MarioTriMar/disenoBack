package edu.uclm.esi.ds.dao;


import org.springframework.data.jpa.repository.JpaRepository;

import edu.uclm.esi.ds.entities.User;

public interface UserDAO extends JpaRepository<User, String>{
	User findByName(String name);
}
