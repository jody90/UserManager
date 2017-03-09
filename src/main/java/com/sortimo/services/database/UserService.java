package com.sortimo.services.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sortimo.dto.UserPostDto;
import com.sortimo.model.User;
import com.sortimo.repositories.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepo;
	
	@Transactional(propagation = Propagation.REQUIRED)
	public UserPostDto create(UserPostDto user) {
		
		user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
		
		User finalUser = new User(user.getUsername(), user.getFirstname(), user.getPassword(), user.getLastname(), user.getEmail());

		userRepo.save(finalUser);
		
		return user;

	}
	
	public boolean userExists(UserPostDto user) {
		
		User existingUser = userRepo.findByUsername(user.getUsername());
		
		return existingUser == null ? false : true;
		
	}

}
