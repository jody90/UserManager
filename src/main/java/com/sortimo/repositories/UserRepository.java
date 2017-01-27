package com.sortimo.repositories;

import java.util.List;

import com.sortimo.dataObjects.User;

public interface UserRepository {
	
	void saveUser(User user);
	
	User getUserByUsername(String username);
	
	List<User> getAllUsers();

}
