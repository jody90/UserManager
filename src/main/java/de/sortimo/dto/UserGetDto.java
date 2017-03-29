package de.sortimo.dto;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.sortimo.model.Right;
import de.sortimo.model.Role;
import de.sortimo.model.User;

public class UserGetDto extends SimpleUserDto {
	
	private Set<Right> rights = new HashSet<Right>(0);
	
	private Set<Role> roles = new HashSet<Role>(0);
	
	private List<Right> authorities;
	
	public UserGetDto(String username, String firstname, String lastname, String email, Set<Right> rights, Set<Role> roles, List<Right> authorities) {
		super(username, firstname, lastname, email);
		this.rights = rights;
		this.roles = roles;
		setAuthorities(authorities);
	}

	public UserGetDto(User user) {
		super(user.getUsername(), user.getFirstname(), user.getLastname(), user.getEmail());
		this.rights = user.getRights();
		this.roles = user.getRoles();
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

	public List<Right> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(List<Right> autohorities) {
		this.authorities = autohorities;
	}

	@Override
	public String toString() {
		return "UserGetDto [rights=" + rights + ", roles=" + roles + ", authorities=" + authorities + ", toString()="
				+ super.toString() + "]";
	}
	
}
