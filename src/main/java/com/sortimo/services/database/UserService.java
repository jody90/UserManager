package com.sortimo.services.database;

import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sortimo.dto.UserPostDto;
import com.sortimo.model.User;
import com.sortimo.repositories.UserRepository;
import com.sortimo.services.HelperFunctions;

@Service
public class UserService {

	private static final Logger LOG = LoggerFactory.getLogger(User.class);
	
	@Autowired
	private HelperFunctions helper;
	
	@Autowired
	private UserRepository userRepo;
	
	@Transactional(propagation = Propagation.REQUIRED)
	public UserPostDto create(UserPostDto user) {
		
		try {
			user.setPassword(helper.md5Hash(user.getPassword()));
		} catch (NoSuchAlgorithmException e) {
			LOG.error("Beim Hashen des Passwords lief was schief!");
		}
		
		User finalUser = new User(user.getUsername(), user.getFirstname(), user.getPassword(), user.getLastname(), user.getEmail());

		userRepo.save(finalUser);
		
		return user;

	}
	
	public boolean userExists(UserPostDto user) {
		
		User existingUser = userRepo.findByUsername(user.getUsername());
		
		return existingUser == null ? false : true;
		
	}
	
	
//	try {
//		helper.md5Hash(password);
//	} catch (NoSuchAlgorithmException e) {
//		LOG.error("Beim Password Hashen lief was schief!");
//	}
	
}
