package de.sortimo.service.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import de.sortimo.service.model.Role;

public interface RoleRepository extends CrudRepository<Role, Long> {

	Optional<Role> findByName(String name);
	
	@Query("SELECT r FROM Role r")
	Optional<Iterable<Role>> findAllRoles();
	
}
