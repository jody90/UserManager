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
		
		User finalUser = new User(username.toLowerCase(), new BCryptPasswordEncoder().encode(password), firstname, lastname, email);

		userRepo.save(finalUser);
		
		LOGGER.info("Benutzer [{}] gespeichert.", finalUser.getUsername());
		
		return finalUser;

	}
	
	@Timelog
	@Transactional(propagation = Propagation.REQUIRED)
	public Optional<User> findByUsernameWithGraphInitialized(String username) {
		return userRepo.findByUsernameWithGraphInitialized(username);
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
			
			if (StringUtils.isNotEmpty(user.getFirstname())) {
				updateUser.setFirstname(user.getFirstname());
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
	public Optional<User> userAddRight(String username, String rightName) {
		Optional<User> tUser = userRepo.findByUsername(username);
		Right tRight = rightService.findByName(rightName).get();
		
		tUser.get().getRights().add(tRight);
		LOGGER.info("User [{}] wurde Recht [{}] hinzugefügt.", username, rightName);
		return tUser;
	}
	
	@Timelog
	@Transactional(propagation = Propagation.REQUIRED)
	public Optional<User> removeRightFromUser(String username, String rightName) {
		 Optional<User> tUser = userRepo.findByUsername(username);
		 Right tRight = rightService.findByName(rightName).get();
		 tUser.get().getRights().remove(tRight);
		 LOGGER.info("Recht [{}] wurde von User [{}] entfernt.", rightName, username);
		 return tUser;
	}
	
	@Timelog
	@Transactional(propagation = Propagation.REQUIRED)
	public Optional<User> userAddRole(String username, String roleName) {
		Optional<User> tUser = userRepo.findByUsername(username);
		Role tRole = roleService.findByName(roleName).get();
		tUser.get().getRoles().add(tRole);
		LOGGER.info("User [{}] wurde Rolle [{}] hinzugefügt.", username, roleName);
		return tUser;
	}
	
	@Timelog
	@Transactional(propagation = Propagation.REQUIRED)
	public Optional<User> removeRoleFromUser(String username, String roleName) {
		 Optional<User> tUser = userRepo.findByUsername(username);
		 Role tRole = roleService.findByName(roleName).get();
		 tUser.get().getRoles().remove(tRole);
		 LOGGER.info("Rolle [{}] wurde von User [{}] entfernt.", roleName, username);
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

				right = rightService.save("superRight", "Wer dieses Recht hat, ist der König der Welt.");
			 }

			Optional<Role> tRole = roleService.findByName("superAdmin");
			
			Role role = null;
			
			if (!tRole.isPresent()) {
				
				LOGGER.info("User: superAdmin not Found! Create it.");				
				
				role = roleService.save("superAdmin", "Gottgleiches Wesen");
				role.getRights().add(right);
			 }			
			
			User user = new User(SUPERADMIN, "$2a$10$o.HCljm8GNpG2cA.bFuhkuppOmZB9OcPyILs5FIthhuB.vEgSFzJK", "super", "admin", "superadmin@sortimo.de");
			userRepo.save(user);
			user.getRoles().add(role);
			
		}
		
	}

}
