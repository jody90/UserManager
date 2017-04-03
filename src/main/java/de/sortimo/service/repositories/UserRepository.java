package de.sortimo.service.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import de.sortimo.service.model.User;

public interface UserRepository extends CrudRepository<User, Long> {
	
	@EntityGraph(value = "userFull" , type = EntityGraphType.FETCH)
	@Query(value="select u from User u WHERE u.username = :username")
	Optional<User> findByUsernameWithGraphInitialized(@Param("username") String username);
	
	@Query(value="select u from User u")
	Optional<Iterable<User>> findAllWithoutGraph();
	
	Optional<User> findByUsername(String username);

	Optional<User> findById(UUID pUser);

}
