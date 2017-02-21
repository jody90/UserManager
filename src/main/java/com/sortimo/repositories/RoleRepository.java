package com.sortimo.repositories;

import org.springframework.data.repository.CrudRepository;

import com.sortimo.model.Role;

public interface RoleRepository extends CrudRepository<Role, Long> {

	Role findByName(String name);
	
}
