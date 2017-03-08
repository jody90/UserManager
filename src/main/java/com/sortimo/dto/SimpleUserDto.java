package com.sortimo.dto;

public class SimpleUserDto {

	private String username;
	
	private String firstname;
	
	private String lastname;
	
	private String email;

	public SimpleUserDto() {}
	
	public SimpleUserDto(String username, String firstname, String lastname, String email) {
		this.username = username;
		this.firstname = firstname;
		this.lastname = lastname;
		this.email = email;
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
		return "SimpleUserDto [username=" + username + ", firstname=" + firstname + ", lastname=" + lastname
				+ ", email=" + email + "]";
	}
	
}
