package de.sortimo.service;

import java.util.Optional;
import java.util.UUID;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.lang3.StringUtils;
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
	public Optional<Role> update(String roleName, Role role) {
		
		Validate.notNull(role.getName());
		
		Optional<Role> tRole = this.findByName(roleName);
		
		if (tRole.isPresent()) {
			
			Role updateRole = tRole.get();
			
			updateRole.setName(role.getName());
			
			if (StringUtils.isNotEmpty(role.getDescription())) {
				updateRole.setDescription(role.getDescription());
			}
		}
		
		return tRole;
		
	}

	@Timelog
	@Transactional(propagation = Propagation.REQUIRED)
	public void delete(Role role) {
		roleRepo.delete(role);
		LOGGER.info("Rolle {} gelöscht.", role.getName());
	}

	@Timelog
	@Transactional(propagation = Propagation.REQUIRED)
	public Role save(String name, String description) {
		Role finalRole = new Role(name.toLowerCase(), description);
		roleRepo.save(finalRole);
		LOGGER.info("Rolle {} gespeichert.", name);
		return finalRole;
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
