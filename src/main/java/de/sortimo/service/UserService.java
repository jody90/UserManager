package de.sortimo.service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
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
	
	@Timelog
	@Transactional(propagation = Propagation.REQUIRED)
	public User saveNew(String username, String password, String firstname, String lastname, String email, Set<Right> rights, Set<Role> roles) {
		
		Validate.notNull(username);
		Validate.notNull(firstname);
		Validate.notNull(lastname);
		Validate.notNull(email);
		
		String tPassword = "1";
		
		if (password != null && !password.trim().isEmpty()) {
			tPassword = password;
		}
		
		User user = new User(username.toLowerCase(), new BCryptPasswordEncoder().encode(tPassword), firstname, lastname, email);

		userRepo.save(user);
		
		if (rights != null) {
			for (Right right : rights) {
				user.getRights().add(rightService.findByName(right.getName()).get());	
			}
		}
		
		if (roles != null) {
			for (Role role : roles) {
				user.getRoles().add(roleService.findByName(role.getName()).get());	
			}
		}
		
		LOGGER.info("Benutzer [{}] gespeichert.", user.getUsername());
		
		return user;
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
		
		Optional<User> tUser = this.findByUsernameWithGraphInitialized(userName);
		
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
			
			if (user.getRoles() != null) {
				
				Set<Role> tRemoveRoles = new HashSet<>();
				
				for (Role role : user.getRoles()) {
					if (!updateUser.getRoles().contains(role)) {
						this.userAddRole(updateUser.getUsername(), role.getName());
					}
				}
				
				for (Role role : updateUser.getRoles()) {
					if (!user.getRoles().contains(role)) {
						tRemoveRoles.add(role);
					}
				}
				
				updateUser.getRights().removeAll(tRemoveRoles);
				
			}
			
			if (user.getRights() != null) {
				
				Set<Right> tRemoveRights = new HashSet<>();
				
				for (Right right : user.getRights()) {
					if (!updateUser.getRights().contains(right)) {
						this.userAddRight(updateUser.getUsername(), right.getName());
					}
				}
				
				for (Right right : updateUser.getRights()) {
					if (!user.getRights().contains(right)) {
						tRemoveRights.add(right);
					}
				}
				
				updateUser.getRights().removeAll(tRemoveRights);
			}
			
		}
		
		return tUser;
	}
	
	@Timelog
	@Transactional(propagation = Propagation.REQUIRED)
	public Optional<User> userAddRight(String username, String rightName) {
		Optional<User> tUser = userRepo.findByUsernameWithGraphInitialized(username);
		Right tRight = rightService.findByName(rightName).get();
		tUser.get().getRights().add(tRight);
		LOGGER.info("User [{}] wurde Recht [{}] hinzugefügt.", username, rightName);
		return tUser;
	}
	
	@Timelog
	@Transactional(propagation = Propagation.REQUIRED)
	public Optional<User> removeRightFromUser(String username, String rightName) {
		 Optional<User> tUser = userRepo.findByUsernameWithGraphInitialized(username);
		 Right tRight = rightService.findByName(rightName).get();
		 tUser.get().getRights().remove(tRight);
		 LOGGER.info("Recht [{}] wurde von User [{}] entfernt.", rightName, username);
		 return tUser;
	}
	
	@Timelog
	@Transactional(propagation = Propagation.REQUIRED)
	public Optional<User> userAddRole(String username, String roleName) {
		Optional<User> tUser = userRepo.findByUsernameWithGraphInitialized(username);
		Role tRole = roleService.findByName(roleName).get();
		tUser.get().getRoles().add(tRole);
		LOGGER.info("User [{}] wurde Rolle [{}] hinzugefügt.", username, roleName);
		return tUser;
	}
	
	@Timelog
	@Transactional(propagation = Propagation.REQUIRED)
	public Optional<User> removeRoleFromUser(String username, String roleName) {
		 Optional<User> tUser = userRepo.findByUsernameWithGraphInitialized(username);
		 Role tRole = roleService.findByName(roleName).get();
		 tUser.get().getRoles().remove(tRole);
		 LOGGER.info("Rolle [{}] wurde von User [{}] entfernt.", roleName, username);
		 return tUser;
	}
	
	@Timelog
	@Transactional(propagation = Propagation.REQUIRED)
	public void removeRightFromUsers(Right right) {
		
		Optional<Iterable<User>> tUsers = userRepo.findAllWithoutGraph();
		
		if (tUsers.isPresent()) {
			for (User user : tUsers.get()) {
				if (user.getRights().contains(right)) {
					user.getRights().remove(right);
				}
			}
		}
	}
	
	@Timelog
	@Transactional(propagation = Propagation.REQUIRED)
	public void removeRoleFromUsers(Role role) {
		
		Optional<Iterable<User>> tUsers = userRepo.findAllWithoutGraph();
		
		if (tUsers.isPresent()) {
			for (User user : tUsers.get()) {
				if (user.getRights().contains(role)) {
					user.getRights().remove(role);
				}
			}
		}
	}
	
	@Timelog
	@Transactional(propagation = Propagation.REQUIRED)
	public void createAdminUser() {
		
		final String SUPERADMIN = "superadmin";
		
		Optional<User> dbUser = userRepo.findByUsername(SUPERADMIN);

		if (!dbUser.isPresent()) {
			
			LOGGER.info("User: {} not Found! Create it.", SUPERADMIN);
			
			Set<Right> tRights = new HashSet<>();
			tRights.add(new Right("userManager_showUsers", "Darf alle User anzeigen"));
			tRights.add(new Right("userManager_showUser", "Darf einen User anzeigen"));
			tRights.add(new Right("userManager_createUser", "Darf einen User anlegen"));
			tRights.add(new Right("userManager_deleteUser", "Darf einen User löschen"));
			tRights.add(new Right("userManager_updateUser", "Darf einen User aktualisieren"));
			tRights.add(new Right("userManager_userAddRight", "Darf einem User ein Recht zuweisen"));
			tRights.add(new Right("userManager_userRemoveRight", "Darf einem User ein Recht entziehen"));
			tRights.add(new Right("userManager_userAddRole", "Darf einem User eine Rolle zuweisen"));
			tRights.add(new Right("userManager_userRemoveRole", "Darf einem User eine Rolle entziehen"));
			tRights.add(new Right("userManager_showRoles", "Darf alle Rollen anzeigen"));
			tRights.add(new Right("userManager_showRole", "Darf eine Rolle anzeigen"));
			tRights.add(new Right("userManager_showRole", "Darf eine Rolle anzeigen"));
			tRights.add(new Right("userManager_addRole", "Darf eine Rolle anlegen"));
			tRights.add(new Right("userManager_deleteRole", "Darf eine Rolle löschen"));
			tRights.add(new Right("userManager_updateRole", "Darf eine Rolle aktualisieren"));
			tRights.add(new Right("userManager_roleAddRight", "Darf einer Rolle ein Recht zuweisen"));
			tRights.add(new Right("userManager_roleRemoveRight", "Darf einer Rolle ein Recht entziehen"));
			tRights.add(new Right("userManager_showRights", "Darf alle Rechte anzeigen"));
			tRights.add(new Right("userManager_showRight", "Darf ein Recht anzeigen"));
			tRights.add(new Right("userManager_showRight", "Darf ein Recht anzeigen"));
			tRights.add(new Right("userManager_addRight", "Darf ein Recht anlegen"));
			tRights.add(new Right("userManager_deleteRight", "Darf ein Recht löschen"));
			tRights.add(new Right("userManager_updateRight", "Darf ein Recht aktualisieren"));
			
			Optional<Role> tRole = roleService.findByName("superAdmin");
			
			Role role = null;
			
			if (!tRole.isPresent()) {
				
				LOGGER.info("Role: superAdmin not Found! Create it.");		
				
				role = roleService.saveNew("superAdmin", "Gottgleiches Wesen", tRights);
			}	
			
			for (Right right : tRights) {
				if (!rightService.findByName(right.getName()).isPresent()) {
					LOGGER.info("Initial Right creation: Right [{}] not Found! Create it.", right.getName());
					rightService.save(right.getName(), right.getDescription());
				}
			}

			User user = new User(SUPERADMIN, "$2a$10$o.HCljm8GNpG2cA.bFuhkuppOmZB9OcPyILs5FIthhuB.vEgSFzJK", "super", "admin", "superadmin@sortimo.de");
			userRepo.save(user);
			user.getRoles().add(role);
			
		}
		
	}

}
