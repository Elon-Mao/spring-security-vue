package com.elonmao.springmodule;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Person {

	@Id
	private String username;
	private String password;
	private String roles;

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRoles() {
		return this.roles;
	}

	public void setRoles(String roles) {
		this.roles = roles;
	}

	public UserDetails buildUser() {
		return User.builder()
				.username(username)
				.password(password)
				.roles(roles.split(","))
				.build();
	}
}
