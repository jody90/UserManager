package com.sortimo.repositories;

import org.springframework.data.repository.CrudRepository;
import com.sortimo.dao.User;

public interface UserRepository extends CrudRepository<User, Long> {
	
	User findByUsername(String username);
	
}
