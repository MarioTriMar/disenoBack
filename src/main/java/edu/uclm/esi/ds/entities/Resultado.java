package edu.uclm.esi.ds.entities;

import java.util.UUID;

import javax.validation.constraints.NotEmpty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "resultados")
public class Resultado {
	@Id 
	private String idPartida;
	
	private String idGanador;
	
	private String idPerdedor;
	
	private String movimientos;
	
	/*
	public Resultado(String idPartida, String idGanador, String idPerdedor, String movimientos) {
		this.idPartida=idPartida;
		this.idGanador=idGanador;
		this.idPerdedor=idPerdedor;
		this.movimientos=movimientos;
	}
	*/
	public Resultado() {
		
	}
	public String getIdPartida() {
		return idPartida;
	}


	public void setIdPartida(String idPartida) {
		this.idPartida = idPartida;
	}


	public String getIdGanador() {
		return idGanador;
	}


	public void setIdGanador(String idGanador) {
		this.idGanador = idGanador;
	}


	public String getIdPerdedor() {
		return idPerdedor;
	}


	public void setIdPerdedor(String idPerdedor) {
		this.idPerdedor = idPerdedor;
	}


	public String getMovimientos() {
		return movimientos;
	}


	public void setMovimientos(String movimientos) {
		this.movimientos = movimientos;
	}
	
}
