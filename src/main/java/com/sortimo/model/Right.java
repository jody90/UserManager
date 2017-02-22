package com.sortimo.model;

import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name="rights")
public class Right {
	
	private Integer id;
	
	private String name;
	
	private String description;
	
	private Set<User> persons;
	
	protected Right() {}
	
	public Right(String name, String description) {
		this.name = name;
		this.description = description;
	}
	
	@ManyToMany(mappedBy = "rights")
	public Set<User> getPersons() {
		return persons;
	}
	
	public void setPersons(Set<User> persons) {
		this.persons = persons;
	}
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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
		return "Right [id=" + id + ", name=" + name + ", description=" + description + "]";
	}

}
