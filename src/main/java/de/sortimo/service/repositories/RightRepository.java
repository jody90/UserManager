package de.sortimo.service.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import de.sortimo.service.model.Right;

public interface RightRepository extends CrudRepository<Right, Long> {
	
	Optional<Right> findByName(String name);
	
	@Query("SELECT r FROM Right r")
	Optional<Iterable<Right>> findAllRights();
	
}
