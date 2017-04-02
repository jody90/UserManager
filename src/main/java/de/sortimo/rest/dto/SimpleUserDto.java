package de.sortimo.rest.dto;

import java.util.UUID;

public class SimpleUserDto {
	
	private UUID id;
	
	private String created;
	
	private String modified;

	private String username;
	
	private String firstname;
	
	private String lastname;
	
	private String email;

//	public SimpleUserDto() {}
	
//	public SimpleUserDto(UUID id, String created, String modified, String username, String firstname, String lastname, String email) {
//		this.id = id;
//		this.created = created;
//		this.modified = modified;
//		this.username = username;
//		this.firstname = firstname;
//		this.lastname = lastname;
//		this.email = email;
//	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public String getModified() {
		return modified;
	}

	public void setModified(String modified) {
		this.modified = modified;
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
		return "SimpleUserDto [id=" + id + ", created=" + created + ", modified=" + modified + ", username=" + username
				+ ", firstname=" + firstname + ", lastname=" + lastname + ", email=" + email + "]";
	}
	
}
