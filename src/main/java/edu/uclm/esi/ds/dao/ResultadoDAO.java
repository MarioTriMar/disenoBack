package edu.uclm.esi.ds.dao;



import org.springframework.data.jpa.repository.JpaRepository;

import edu.uclm.esi.ds.entities.Resultado;


public interface ResultadoDAO extends JpaRepository<Resultado, String>{
	
}
