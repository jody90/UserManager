package com.sortimo.repositories;

import org.springframework.data.repository.CrudRepository;

import com.sortimo.model.Right;

public interface RightRepository extends CrudRepository<Right, Long> {
	
	Right findByName(String name);

//	void save(Right right);
//
//	Iterable<Right> findAll(Pageable pageable);
//
//	void delete(Right right);
		
}
