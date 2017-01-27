package com.sortimo.repositories;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.sortimo.dataObjects.User;

@Component
public class UserRepositoryImpl implements UserRepository {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public void saveUser(User user) {
		 
		jdbcTemplate.update("INSERT INTO users(username, password, lastname, firstname, email) VALUES (?,?,?,?,?)", new Object[] { 
				user.getUsername(),
				user.getPassword(),
				user.getLastname(),
				user.getFirstname(),
				user.getEmail()
		});
	    
	}

	@Override
	public User getUserByUsername(String username) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<User> getAllUsers() {
		// TODO Auto-generated method stub
		return null;
	}

}
