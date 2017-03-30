package de.sortimo.service.security;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.sortimo.service.model.Right;
import de.sortimo.service.model.Role;

public class JwtUser {
	
	private String username;
	
	private String firstname;
	
	private String lastname;
	
	private String email;
	
	private String password;
	
	private Set<Right> rights = new HashSet<Right>(0);
	
	private Set<Role> roles = new HashSet<Role>(0);
	
	private List<Right> authorities;
	
	public JwtUser() { }
	
	public JwtUser(String username, String firstname, String lastname, String email, String password, Set<Right> rights, Set<Role> roles, List<Right> authorities) {
		this.username = username;
		this.firstname = firstname;
		this.lastname = lastname;
		this.email = email;
		this.password = password;
		this.rights = rights;
		this.roles = roles;
		setAuthorities(authorities);
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<Right> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(List<Right> autohorities) {
		this.authorities = autohorities;
	}

	@Override
	public String toString() {
		return "JwtUser [username=" + username + ", firstname=" + firstname + ", lastname=" + lastname + ", email="
				+ email + ", password=" + password + ", rights=" + rights + ", roles=" + roles + ", authorities="
				+ authorities + ", toString()=" + super.toString() + "]";
	}
	
}
