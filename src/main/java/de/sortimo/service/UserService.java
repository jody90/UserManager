package de.sortimo.service;

import java.util.Optional;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import de.sortimo.base.aspects.Timelog;
import de.sortimo.service.model.Right;
import de.sortimo.service.model.Role;
import de.sortimo.service.model.User;
import de.sortimo.service.repositories.UserRepository;

@Service
public class UserService {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private RightService rightService;
	
	@Timelog
	@Transactional(propagation = Propagation.REQUIRED)
	public Optional<Iterable<User>> findAllWithoutGraph() {
		return userRepo.findAllWithoutGraph();
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Timelog
	public User save(String username, String password, String firstname, String lastname, String email) {
		
		User finalUser = new User(username, new BCryptPasswordEncoder().encode(password), firstname, lastname, email);

		userRepo.save(finalUser);
		
		LOGGER.info("Benutzer [{}] gespeichert.", finalUser.getUsername());
		
		return finalUser;

	}
	
	@Timelog
	@Transactional(propagation = Propagation.REQUIRED)
	public Optional<User> findByUsername(String username) {
		return userRepo.findByUsername(username);
	}
	
	@Timelog
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean delete(UUID id) {
		Optional<User> tOptionalUser = userRepo.findById(id);
		if(tOptionalUser.isPresent()) {
			userRepo.delete(tOptionalUser.get());
			LOGGER.info("User [{}] gelöscht.", tOptionalUser.get().getUsername());
			return true;
		}
		
		return false;
	}
	
	@Timelog
	@Transactional(propagation = Propagation.REQUIRED)
	public Optional<User> update(String userName, User user) {
		
		Validate.notNull(user.getUsername());
		
		Optional<User> tUser = this.findByUsername(userName);
		
		if (tUser.isPresent()) {
			
			User updateUser = tUser.get();
			
			updateUser.setUsername(user.getUsername());
			
			if (StringUtils.isNotEmpty(user.getEmail())) {
				updateUser.setFirstname(user.getEmail());
			}
			
			if (StringUtils.isNotEmpty(user.getLastname())) {
				updateUser.setLastname(user.getLastname());
			}
			
			if (StringUtils.isNotEmpty(user.getEmail())) {
				updateUser.setEmail(user.getEmail());
			}
		}
		
		return tUser;
		
	}
	
	@Timelog
	@Transactional(propagation = Propagation.REQUIRED)
	public Optional<User> userAddRight(String username, Right right) {
		Optional<User> tUser = userRepo.findByUsername(username);
		tUser.get().getRights().add(right);
		LOGGER.info("User [{}] wurde Recht [{}] hinzugefügt.", username, right.getName());
		return tUser;
	}
	
	@Timelog
	@Transactional(propagation = Propagation.REQUIRED)
	public Optional<User> removeRightFromUser(String username, Right right) {
		 Optional<User> tUser = userRepo.findByUsername(username);
		 tUser.get().getRights().remove(right);
		 LOGGER.info("Recht [{}] wurde von User [{}] entfernt.", right.getName(), username);
		 return tUser;
	}
	
	@Timelog
	@Transactional(propagation = Propagation.REQUIRED)
	public Optional<User> userAddRole(String username, Role role) {
		Optional<User> tUser = userRepo.findByUsername(username);
		tUser.get().getRoles().add(role);
		LOGGER.info("User [{}] wurde Rolle [{}] hinzugefügt.", username, role.getName());
		return tUser;
	}
	
	@Timelog
	@Transactional(propagation = Propagation.REQUIRED)
	public Optional<User> removeRoleFromUser(String username, Role role) {
		 Optional<User> tUser = userRepo.findByUsername(username);
		 tUser.get().getRoles().remove(role);
		 LOGGER.info("Rolle [{}] wurde von User [{}] entfernt.", role.getName(), username);
		 return tUser;
	}
	
	@Timelog
	@Transactional(propagation = Propagation.REQUIRED)
	public void createAdminUser() {
		
		final String SUPERADMIN = "superadmin";
		
		Optional<User> dbUser = userRepo.findByUsername(SUPERADMIN);

		if (!dbUser.isPresent()) {
			
			LOGGER.info("User: superadmin not Found! Create it.");
			
			Optional<Right> tRight = rightService.findByName("superRight");
			
			Right right = null;
			
			if (!tRight.isPresent()) {
				
				LOGGER.info("Right: superRight not Found! Create it.");
				
				right = new Right("superRight", "Wer dieses Recht hat, ist der König der Welt.");
				rightService.save(right);
			 }

			Optional<Role> tRole = roleService.findByName("superAdmin");
			
			Role role = null;
			
			if (!tRole.isPresent()) {
				
				LOGGER.info("User: superAdmin not Found! Create it.");				
				
				role = new Role("superAdmin", "Gottgleiches Wesen");
				roleService.save(role);
				role.getRights().add(right);
			 }			
			
			User user = new User(SUPERADMIN, "$2a$10$o.HCljm8GNpG2cA.bFuhkuppOmZB9OcPyILs5FIthhuB.vEgSFzJK", "super", "admin", "superadmin@sortimo.de");
			userRepo.save(user);
			user.getRoles().add(role);
			
		}
		
	}

}
