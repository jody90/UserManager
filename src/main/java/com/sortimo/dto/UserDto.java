package com.sortimo.dto;

import java.util.HashSet;
import java.util.Set;

import com.sortimo.model.Right;
import com.sortimo.model.Role;

public class UserDto {
	
	private String username;
	
	private String firstname;
	
	private String lastname;
	
	private String email;
	
	private Set<Right> rights = new HashSet<Right>(0);
	
	private Set<Role> roles = new HashSet<Role>(0);
	
	public UserDto() { }
	
	public UserDto(String username, String firstname, String lastname, String email, Set<Right> rights, Set<Role> roles) {
		this.username = username;
		this.firstname = firstname;
		this.lastname = lastname;
		this.email = email;
		this.rights = rights;
		this.roles = roles;
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

	public Set<Right> getRights() {
		return rights;
	}

	public void setRights(Set<Right> rights) {
		this.rights = rights;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	@Override
	public String toString() {
		return "UserDto [username=" + username + ", firstname=" + firstname + ", lastname=" + lastname + ", email="
				+ email + ", rights=" + rights + ", roles=" + roles + "]";
	}
	
}
