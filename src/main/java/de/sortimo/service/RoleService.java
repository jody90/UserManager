package de.sortimo.service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import de.sortimo.base.aspects.Timelog;
import de.sortimo.service.model.Right;
import de.sortimo.service.model.Role;
import de.sortimo.service.repositories.RoleRepository;

@Service
public class RoleService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RoleService.class);
	
	@Autowired
	private RoleRepository roleRepo;
	
	@Autowired
	private RightService rightService;
	
	@Autowired
	private UserService userService;

	@Timelog
	@Transactional(propagation = Propagation.REQUIRED)
	public Optional<Iterable<Role>> findAll() {
		Optional<Iterable<Role>> tRoles = roleRepo.findAllRoles();
		return tRoles;
	}

	@Timelog
	@Transactional(propagation = Propagation.REQUIRED)
	public Optional<Role> findByName(String name) {
		return roleRepo.findByName(name);
	}
	
	@Timelog
	@Transactional(propagation = Propagation.REQUIRED)
	public Optional<Role> findByRoleNameWithGraphInitialized(String name) {
		return roleRepo.findByRoleNameWithGraphInitialized(name);
	}
	
	@Timelog
	@Transactional(propagation = Propagation.REQUIRED)
	public Role saveNew(String name, String description, Set<Right> rights) {
		
		Validate.notNull(name);
		
		Role role = new Role(name.toLowerCase(), description);

		roleRepo.save(role);
		
		if (rights != null) {
			for (Right right : rights) {
				role.getRights().add(rightService.findByName(right.getName()).get());	
			}
		}
		
		LOGGER.info("Rolle [{}] gespeichert.", role.getName());
		
		return role;
	}

	@Timelog
	@Transactional(propagation = Propagation.REQUIRED)
	public Optional<Role> update(String rolename, Role role) {
		
		Validate.notNull(role.getName());
		
		Optional<Role> tRole = this.findByName(rolename);
		
		if (tRole.isPresent()) {
			
			Role updateRole = tRole.get();
			
			updateRole.setName(role.getName());
			
			if (StringUtils.isNotEmpty(role.getDescription())) {
				updateRole.setDescription(role.getDescription());
			}

			if (role.getRights() != null) {
				
				Set<Right> tRemoveRights = new HashSet<>();
				
				for (Right right : role.getRights()) {
					if (!updateRole.getRights().contains(right)) {
						this.roleAddRight(updateRole.getName(), right.getName());
					}
				}
				
				for (Right right : updateRole.getRights()) {
					if (!role.getRights().contains(right)) {
						tRemoveRights.add(right);
					}
				}
				
				updateRole.getRights().removeAll(tRemoveRights);
			}
			
		}
		
		return tRole;
	}

	@Timelog
	@Transactional(propagation = Propagation.REQUIRED)
	public void delete(Role role) {
		roleRepo.delete(role);
		userService.removeRoleFromUsers(role);
		LOGGER.info("Rolle {} gelöscht.", role.getName());
	}

	@Timelog
	@Transactional(propagation = Propagation.REQUIRED)
	public Role roleAddRight(String roleName, String rightName) {
		Role tRole = roleRepo.findByName(roleName).get();
		Right tRight = rightService.findByName(rightName).get();
		tRole.getRights().add(tRight);
		LOGGER.info("Rolle {} wurde Recht {} hinzugefügt.", roleName, rightName);
		return tRole;
	}

	@Timelog
	@Transactional(propagation = Propagation.REQUIRED)
	public Role removeRightFromRole(String roleName, String rightName) {
		 Role tRole = roleRepo.findByName(roleName).get();
		 Right tRight = rightService.findByName(rightName).get();
		 tRole.getRights().remove(tRight);
		 LOGGER.info("Recht {} wurde von Rolle {} entfernt.", rightName, roleName);
		 return tRole;
	}
	
	@Timelog
	@Transactional(propagation = Propagation.REQUIRED)
	public void removeRightFromRoles(Right right) {
		
		Optional<Iterable<Role>> tRoles = roleRepo.findAllRoles();
		
		if (tRoles.isPresent()) {
			for (Role role : tRoles.get()) {
				if (role.getRights().contains(right)) {
					role.getRights().remove(right);
				}
			}
		}
	}
	
	@Timelog
	@Transactional(propagation = Propagation.REQUIRED)
	public void superAdminAddRight(String rightName) {

		final String SUPERADMIN = "superAdmin";
		
		Role tRole = roleRepo.findByName(SUPERADMIN).get();
		Right tRight = rightService.findByName(rightName).get();
		tRole.getRights().add(tRight);
		LOGGER.info("Rolle {} wurde Recht {} hinzugefügt.", SUPERADMIN, rightName);
	}

}
