package com.sortimo.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;


@Entity
@Table(name="users")
public class User {

	private String username;

	private String firstname;

	private String password;

	private String lastname;

	private String email;
	
	protected User() {}
	
	private Set<Right> rights = new HashSet<Right>(0);
	
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "users_rights", joinColumns = @JoinColumn(name = "username"), inverseJoinColumns = @JoinColumn(name = "right_id"))   
    public Set<Right> getRights() {
        return rights;
    }
    
    public void setRights(Set<Right> rights) {
    	this.rights = rights;
    }
	
	public User(String username, String firstname, String password, String lastname, String email) {
		this.setUsername(username);
		this.setFirstname(firstname);
		this.setPassword(password);
		this.setLastname(lastname);
		this.setEmail(email);
	}
    
	public User(String username, String firstname, String password, String lastname, String email, Set<Right> rights) {
		this.setUsername(username);
		this.setFirstname(firstname);
		this.setPassword(password);
		this.setLastname(lastname);
		this.setEmail(email);
		this.setRights(rights);
	}

	@Id
	@Column(name = "username", unique = true, nullable = false)
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
				+ lastname + ", email=" + email + ", rights=" + rights + "]";
	}
	
}