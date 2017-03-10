package com.sortimo.services.database;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sortimo.dto.UserGetDto;
import com.sortimo.dto.UserPostDto;
import com.sortimo.model.User;
import com.sortimo.repositories.UserRepository;

@Service
public class UserService {

	private static final Logger LOG = LoggerFactory.getLogger(UserService.class);
	
	@Autowired
	private UserRepository userRepo;
	
	@Transactional(propagation = Propagation.REQUIRED)
	public UserPostDto save(UserPostDto user) {
		
		user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
		
		User finalUser = new User(user.getUsername(), user.getFirstname(), user.getPassword(), user.getLastname(), user.getEmail());

		userRepo.save(finalUser);
		
		return user;

	}
	
	public boolean userExists(UserPostDto user) {
		
		User existingUser = userRepo.findByUsername(user.getUsername());
		
		return existingUser == null ? false : true;
		
	}

	public List<UserGetDto> findAll() {
		Iterable<User> usersCollection = userRepo.findAll();
		List<UserGetDto> userGetCollection = new ArrayList<>();
		
		for (User user : usersCollection) {
			userGetCollection.add(new UserGetDto(user));
		}
		
		return userGetCollection;
	}

	public UserGetDto findByUsername(String username) throws NullPointerException {
		
		User dbUser = userRepo.findByUsername(username);
		
		if (dbUser == null) {
			LOG.error("Es wurde ein User angefragt, der nicht in der Datenbank existiert. Angefragter User: {}", username);
			throw new NullPointerException();
		}
		
		return new UserGetDto(dbUser);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void delete(UserGetDto user) {
		userRepo.delete(userRepo.findByUsername(user.getUsername()));
	}

}
