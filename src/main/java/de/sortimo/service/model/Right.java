package de.sortimo.service.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import de.sortimo.base.persistence.AbstractEntity;

@Entity
@Table(name="rights")
public class Right extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	@Column(name = "name", unique = true, nullable = false)
	private String name;
	
	@Column(name = "description", nullable = true)	
	private String description;
		
	protected Right() {}
	
	public Right(String name, String description) {
		this.name = name;
		this.description = description;
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
		return "Right [name=" + name + ", description=" + description + ", toString()=" + super.toString() + "]";
	}

}
