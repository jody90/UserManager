package de.sortimo.rest.dto;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import de.sortimo.base.jackson.CustomLocalDateTimeDeserializer;
import de.sortimo.base.jackson.CustomLocalDateTimeSerializer;

public class ExtendedUserDto {
	
	private UUID id;
	
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime created;
	
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime modified;

	private String username;
	
	private String firstname;
	
	private String lastname;
	
	private String email;
	
	private Set<SimpleRightDto> rights = new HashSet<SimpleRightDto>(0);
	
	private Set<SimpleRoleDto> roles = new HashSet<SimpleRoleDto>(0);

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public LocalDateTime getCreated() {
		return created;
	}

	public void setCreated(LocalDateTime localDateTime) {
		this.created = localDateTime;
	}

	public LocalDateTime getModified() {
		return modified;
	}

	public void setModified(LocalDateTime modified) {
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

	public Set<SimpleRightDto> getRights() {
		return rights;
	}

	public void setRights(Set<SimpleRightDto> rights) {
		this.rights = rights;
	}

	public Set<SimpleRoleDto> getRoles() {
		return roles;
	}

	public void setRoles(Set<SimpleRoleDto> roles) {
		this.roles = roles;
	}

	@Override
	public String toString() {
		return "ExtendedUserDto [id=" + id + ", created=" + created + ", modified=" + modified + ", username="
				+ username + ", firstname=" + firstname + ", lastname=" + lastname + ", email=" + email + ", rights="
				+ rights + ", roles=" + roles + "]";
	}
	
}
