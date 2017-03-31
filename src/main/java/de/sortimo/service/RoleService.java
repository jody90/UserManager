package de.sortimo.service;

import java.util.Optional;

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
	public void save(Role role) {
		roleRepo.save(role);
		LOGGER.info("Rolle {} gespeichert.", role.getName());
	}

	@Timelog
	@Transactional(propagation = Propagation.REQUIRED)
	public Optional<Role> roleAddRight(String roleName, Right right) {
		Optional<Role> tRole = roleRepo.findByName(roleName);
		tRole.get().getRights().add(right);
		LOGGER.info("Rolle {} wurde Recht {} hinzugefügt.", roleName, right.getName());
		return tRole;
	}

	@Timelog
	@Transactional(propagation = Propagation.REQUIRED)
	public Optional<Role> removeRightFromRole(String roleName, Right right) {
		 Optional<Role> tRole = roleRepo.findByName(roleName);
		 tRole.get().getRights().remove(right);
		 LOGGER.info("Recht {} wurde von Rolle {} entfernt.", right.getName(), roleName);
		 return tRole;
	}

}
