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
	
	private Set<Right> rights = new HashSet<Right>(0);
	
	private Set<Role> roles = new HashSet<Role>(0);

	protected User() {}
	
	public User(String username, String firstname, String password, String lastname, String email) {
		this.setUsername(username);
		this.setFirstname(firstname);
		this.setPassword(password);
		this.setLastname(lastname);
		this.setEmail(email);
	}
    
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "users_rights", joinColumns = @JoinColumn(name = "username"), inverseJoinColumns = @JoinColumn(name = "right_id"))   
    public Set<Right> getRights() {
        return rights;
    }
    
    public void setRights(Set<Right> rights) {
    	this.rights = rights;
    }
    
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "username"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    public Set<Role> getRoles() {
        return roles;
    }
    
    public void setRoles(Set<Role> roles) {
    	this.roles = roles;
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