package de.sortimo.services.database;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import de.sortimo.converter.UserConverter;
import de.sortimo.dto.UserGetDto;
import de.sortimo.dto.UserPostDto;
import de.sortimo.model.Right;
import de.sortimo.model.Role;
import de.sortimo.model.User;
import de.sortimo.repositories.RightRepository;
import de.sortimo.repositories.RoleRepository;
import de.sortimo.repositories.UserRepository;
import de.sortimo.services.Timelog;

@Service
public class UserService {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private RoleRepository roleRepo;
	
	@Autowired
	private RightRepository rightRepo;
	
	@Autowired
	private UserConverter userConverter;
	
	@Deprecated
	@Transactional(propagation = Propagation.REQUIRED)
	@Timelog
	public UserPostDto save(UserPostDto user) {
		
		user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
		
		User finalUser = new User(user.getUsername(), user.getPassword(), user.getFirstname(), user.getLastname(), user.getEmail());

		userRepo.save(finalUser);
		
		return user;

	}
	
	@Deprecated
	@Timelog
	public boolean userExists(UserPostDto user) {
		
		Optional<User> existingUser = userRepo.findByUsername(user.getUsername());
		
		return existingUser == null ? false : true;
		
	}
	
	@Deprecated
	@Timelog
	@Transactional(propagation = Propagation.REQUIRED)
	public List<UserGetDto> findAll() {
		Iterable<User> usersCollection = userRepo.findAll();
		List<UserGetDto> userGetCollection = new ArrayList<>();
		
		for (User user : usersCollection) {
			userGetCollection.add(userConverter.getUserGetDto(user));
		}
		
		return userGetCollection;
	}

	@Deprecated
	public UserGetDto findByUsername(String username) throws NullPointerException {
		
		Optional<User> dbUser = userRepo.findByUsername(username);
		
		if (!dbUser.isPresent()) {
			LOGGER.error("Es wurde ein User angefragt, der nicht in der Datenbank existiert. Angefragter User: {}", username);
			throw new NullPointerException();
		}
		
		return userConverter.getUserGetDto(dbUser.get());
	}

	@Deprecated
	@Transactional(propagation = Propagation.REQUIRED)
	public void delete(UserGetDto user) {
		userRepo.delete(userRepo.findByUsername(user.getUsername()).get());
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@Transactional(propagation = Propagation.REQUIRED)
	@Timelog
	public User save(String password, String username, String firstname, String lastname, String email) {
		
		User finalUser = new User(username, new BCryptPasswordEncoder().encode(password), firstname, lastname, email);

		userRepo.save(finalUser);
		
		return finalUser;

	}
	
	@Timelog
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean userExists2(String username) {
		
		Optional<User> existingUser = userRepo.findByUsername(username);
		
		return existingUser.isPresent();
		
	}
	
	@Timelog
	@Transactional(propagation = Propagation.REQUIRED)
	public List<UserGetDto> findAll2() {
		Iterable<User> usersCollection = userRepo.findAll();
		List<UserGetDto> userGetCollection = new ArrayList<>();
		
		for (User user : usersCollection) {
			userGetCollection.add(userConverter.getUserGetDto(user));
		}
		
		return userGetCollection;
	}
	
	@Timelog
	@Transactional(propagation = Propagation.REQUIRED)
	public Optional<User> findByUsername2(String username) throws NullPointerException {
		return userRepo.findByUsername(username);
	}
	
	@Timelog
	@Transactional(propagation = Propagation.REQUIRED)
	public void delete(UUID id) {
		Optional<User> tOptionalUser = userRepo.findById(id);
		if(tOptionalUser.isPresent()) {
			userRepo.delete(tOptionalUser.get());
		}
	}
	
	@Timelog
	@Transactional(propagation = Propagation.REQUIRED)
	public void createAdminUser() {
		
		final String SUPERADMIN = "superadmin";
		
		Optional<User> dbUser = userRepo.findByUsername(SUPERADMIN);

		if (!dbUser.isPresent()) {
			
			LOGGER.info("User: superadmin not Found! Create it.");
			
			Right right = rightRepo.findByName("superRight");
			
			if (right == null) {
				
				LOGGER.info("Right: superRight not Found! Create it.");
				
				right = new Right("superRight", "Wer dieses Recht hat, ist der König der Welt.");
				rightRepo.save(right);
			 }

			Role role = roleRepo.findByName("superAdmin");
			
			if (role == null) {
				
				LOGGER.info("User: superAdmin not Found! Create it.");				
				
				role = new Role("superAdmin", "Gottgleiches Wesen");
				roleRepo.save(role);
				role.getRights().add(right);
			 }			
			
			User user = new User(SUPERADMIN, "$2a$10$o.HCljm8GNpG2cA.bFuhkuppOmZB9OcPyILs5FIthhuB.vEgSFzJK", "super", "admin", "superadmin@sortimo.de");
			userRepo.save(user);
			user.getRoles().add(role);
			
		}
		
	}

}
