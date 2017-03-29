package de.sortimo.repositories;

import org.springframework.data.repository.CrudRepository;

import de.sortimo.model.Role;

public interface RoleRepository extends CrudRepository<Role, Long> {

	Role findByName(String name);
	
}
