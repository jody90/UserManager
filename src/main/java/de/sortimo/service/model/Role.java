package de.sortimo.service.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.Table;

import de.sortimo.base.persistence.AbstractEntity;

@NamedEntityGraph(name = "roleFull", attributeNodes = {
		@NamedAttributeNode(value = "rights")
	}
)
@Entity
@Table(name="roles")
public class Role extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	@Column(name = "name", nullable = false, unique = true)	
	private String name;

	@Column(name = "description", nullable = true)
	private String description;
	
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@JoinTable(name = "roles_rights", joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "right_id", referencedColumnName = "id"))   
	private Set<Right> rights = new HashSet<Right>(0);
	
    public Role() {}
	
    public Role(String name, String description, Set<Right> rights) {
		super();
		this.name = name;
		this.description = description;
		this.rights = rights;
	}

	public Role(String name, String description) {
		this.setName(name);
		this.setDescription(description);
	}

	public Set<Right> getRights() {
        return rights;
    }
    
    public void setRights(Set<Right> rights) {
    	this.rights = rights;
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
		return "Role [name=" + name + ", description=" + description + ", rights=" + rights + ", toString()="
				+ super.toString() + "]";
	}

}
