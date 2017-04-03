package de.sortimo.rest.dto;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class JwtUserDto {
	
	private String username;
	
	private String firstname;
	
	private String lastname;
	
	private String email;
	
	private String password;
	
	private Set<JwtRightDto> rights = new HashSet<JwtRightDto>(0);
	
	private Set<JwtRoleDto> roles = new HashSet<JwtRoleDto>(0);
	
	private List<JwtRightDto> authorities;
	
	public JwtUserDto() { }	
	
	public JwtUserDto(String username, String firstname, String lastname, String email, String password, Set<JwtRightDto> rights, Set<JwtRoleDto> roles, List<JwtRightDto> authorities) {
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

	public Set<JwtRightDto> getRights() {
		return rights;
	}

	public void setRights(Set<JwtRightDto> rights) {
		this.rights = rights;
	}

	public Set<JwtRoleDto> getRoles() {
		return roles;
	}

	public void setRoles(Set<JwtRoleDto> roles) {
		this.roles = roles;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<JwtRightDto> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(List<JwtRightDto> autohorities) {
		this.authorities = autohorities;
	}

	@Override
	public String toString() {
		return "JwtUserDto [username=" + username + ", firstname=" + firstname + ", lastname=" + lastname + ", email="
				+ email + ", password=" + password + ", rights=" + rights + ", roles=" + roles + ", authorities="
				+ authorities + "]";
	}
	
}
