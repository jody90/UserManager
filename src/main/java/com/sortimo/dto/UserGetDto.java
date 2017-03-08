package com.sortimo.dto;

import java.util.HashSet;
import java.util.Set;

import com.sortimo.model.Right;
import com.sortimo.model.Role;

public class UserGetDto extends SimpleUserDto {
	
	private Set<Right> rights = new HashSet<Right>(0);
	
	private Set<Role> roles = new HashSet<Role>(0);
	
	public UserGetDto(String username, String firstname, String lastname, String email, Set<Right> rights, Set<Role> roles) {
		super(username, firstname, lastname, email);
		this.rights = rights;
		this.roles = roles;
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
		return "UserDto [rights=" + rights + ", roles=" + roles + "]";
	}
	
}
