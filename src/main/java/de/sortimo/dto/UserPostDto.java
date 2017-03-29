package de.sortimo.dto;

import java.util.Set;

import de.sortimo.model.Right;
import de.sortimo.model.Role;

public class UserPostDto extends SimpleUserDto {
	
	private String password;
	
	private Set<Right> rights;
	
	private Set<Role> roles;

	public UserPostDto() {
		super();
	}

	public UserPostDto(String username, String firstname, String lastname, String email, String password) {
		super(username, firstname, lastname, email);
		this.setPassword(password);
	}
	
	public UserPostDto(String username, String firstname, String lastname, String email, String password, Set<Right> rights, Set<Role> roles) {
		super(username, firstname, lastname, email);
		this.setPassword(password);
		this.setRights(rights);
		this.setRoles(roles);
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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
		return "UserPostDto [password=" + password + ", toString()=" + super.toString() + "]";
	}

}
