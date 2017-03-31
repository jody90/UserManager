package de.sortimo.service.repositories;

import de.sortimo.service.model.User;

public interface UserCustomOperations{
	
    User findAllUser(String email, String entityGraph);
    
}
