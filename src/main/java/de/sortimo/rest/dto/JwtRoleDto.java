package de.sortimo.rest.dto;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import de.sortimo.base.jackson.CustomLocalDateTimeDeserializer;
import de.sortimo.base.jackson.CustomLocalDateTimeSerializer;

public class JwtRoleDto {

	private UUID id;

	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime created;

	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime modified;
	
	private String name;
	
	private String description;
	
	private Set<JwtRightDto> rights = new HashSet<JwtRightDto>(0);

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

	public void setModified(LocalDateTime localDateTime) {
		this.modified = localDateTime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<JwtRightDto> getRights() {
		return rights;
	}

	public void setRights(Set<JwtRightDto> rights) {
		this.rights = rights;
	}

	@Override
	public String toString() {
		return "JwtRoleDto [id=" + id + ", created=" + created + ", modified=" + modified + ", name=" + name
				+ ", description=" + description + ", rights=" + rights + "]";
	}
	
}
