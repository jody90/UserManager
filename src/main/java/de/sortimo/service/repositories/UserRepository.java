package de.sortimo.service.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import de.sortimo.service.model.User;

public interface UserRepository extends CrudRepository<User, Long>, JpaRepository<User,Long>, UserCustomOperations {
	
//	Iterable<User> findAll();
	
	@EntityGraph(value = "userFull" , type=EntityGraphType.FETCH)
	@Query(value="select u from User u")
	Iterable<User> findAllWithGraphInitialized();
	
	@Query(value="select u from User u")
	@EntityGraph(value = "userPreview" , type=EntityGraphType.FETCH)
	Optional<Iterable<User>> findAllWithoutGraph();
	
	Optional<User> findByUsername(String username);
	
	@EntityGraph(value = "userFull" , type=EntityGraphType.FETCH)
	@Query(value="select u from User u where u.username=:nameFromUser")
	Optional<User> findByUsernameWithGraphInitialized(@Param("nameFromUser") String username);

	Optional<User> findById(UUID pUser);

}
