package edu.uclm.esi.ds.entities;

import java.util.UUID;

import javax.validation.constraints.NotEmpty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

@Entity 
@Table(
		name = "users", 
		indexes = {
				@Index(columnList = "name", unique=true),
				@Index(columnList = "email", unique=true)
		})
public class User {
	@Id @Column(length = 36)
	private String id;
	@Column(length=100) @NotEmpty
	private String name;
	@Column(length=140) @NotEmpty
	private String email;
	@NotEmpty
	private String pwd;
	
	private int fichas;
	
	private Long validationDate;
	
	public User() {
		this.id=UUID.randomUUID().toString();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = org.apache.commons.codec.digest.DigestUtils.sha512Hex(pwd);
	}

	public Long getValidationDate() {
		return validationDate;
	}

	public void setValidationDate(Long validationDate) {
		this.validationDate = validationDate;
	}

	public int getFichas() {
		return fichas;
	}

	public void setFichas(int fichas) {
		this.fichas = fichas;
	}

	
	

}
