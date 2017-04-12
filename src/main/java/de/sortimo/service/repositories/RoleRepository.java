package de.sortimo.service.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import de.sortimo.service.model.Role;

public interface RoleRepository extends CrudRepository<Role, Long> {
	
	@EntityGraph(value = "roleFull" , type = EntityGraphType.FETCH)
	@Query(value="select r from Role r WHERE r.name = :name")
	Optional<Role> findByRoleNameWithGraphInitialized(@Param("name") String name);

	Optional<Role> findByName(String name);
	
	@Query("SELECT r FROM Role r")
	Optional<Iterable<Role>> findAllRoles();

}
