package de.sortimo.service.repositories;

import org.springframework.data.repository.CrudRepository;

import de.sortimo.service.model.Role;

public interface RoleRepository extends CrudRepository<Role, Long> {

	Role findByName(String name);
	
}
