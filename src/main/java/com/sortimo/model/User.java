package com.sortimo.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="users")
public class User {

	@Id
	private String username;

	private String firstname;

	private String password;

	private String lastname;

	private String email;
	
	protected User() {}

	public User(String username, String firstname, String password, String lastname, String email) {
		this.setUsername(username);
		this.setFirstname(firstname);
		this.setPassword(password);
		this.setLastname(lastname);
		this.setEmail(email);
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "User [username=" + username + ", firstname=" + firstname + ", password=" + password + ", lastname="
				+ lastname + ", email=" + email + "]";
	}
}