package de.sortimo.rest.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class SimpleRightDto {
	
	private UUID id;
	
	private String created;
	
	private String modified;
	
	private String name;
	
	private String description;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(LocalDateTime localDateTime) {
		this.created = localDateTime != null ? localDateTime.toString() : "";
	}

	public String getModified() {
		return modified;
	}

	public void setModified(LocalDateTime localDateTime) {
		this.modified = localDateTime != null ? localDateTime.toString() : "";
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

	@Override
	public String toString() {
		return "SimpleRightDto [id=" + id + ", created=" + created + ", modified=" + modified + ", name=" + name
				+ ", description=" + description + "]";
	}

}
