package de.sortimo.rest.controller;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import de.sortimo.base.rest.RestMessage;
import de.sortimo.rest.converter.RoleConverter;
import de.sortimo.rest.dto.ExtendedRoleDto;
import de.sortimo.rest.dto.SimpleRoleDto;
import de.sortimo.service.RightService;
import de.sortimo.service.RoleService;
import de.sortimo.service.model.Right;
import de.sortimo.service.model.Role;

@RestController
@RequestMapping(value="/api/role")
public class RoleController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RoleController.class);
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private RightService rightService;
	
	private RoleConverter roleConverter = new RoleConverter();

	/**
	 * Liest alle Rollen aus der Datenbank
	 * 
	 * @return Collection von Role Objekten
	 */
	@RequestMapping(method = RequestMethod.GET, produces="application/json")
	@PreAuthorize("hasAuthority('usermanager_showroles')")
	public @ResponseBody ResponseEntity<?> getAllRoles() {
		
		Optional<Iterable<Role>> rolesCollection = roleService.findAll();

		// pruefen ob Rechte vorhanden sind
		if (!rolesCollection.isPresent()) {
			LOGGER.info("GET Roles: Es wurden keine Rollen in der Datenbank gefunden.");
			RestMessage message = new RestMessage(HttpStatus.NOT_FOUND, "No Roles found");
			return new ResponseEntity<RestMessage>(message, message.getState());
		}
		
		Set<SimpleRoleDto> roles = roleConverter.createDtoList(rolesCollection.get());
	
		// response zurueck geben
		return new ResponseEntity<Set<SimpleRoleDto>>(roles, HttpStatus.OK);
		
	}
	
	/**
	 * Fuegt eine neue Rolle in die Datenbank ein
	 * 
	 * @param tRole
	 * @param request
	 * @return
	 * @throws MalformedURLException
	 */
	@RequestMapping(method = RequestMethod.POST, consumes="application/json", produces="application/json")
	@PreAuthorize("hasAuthority('usermanager_addrole')")
	public @ResponseBody ResponseEntity<?> addRole(@RequestBody Role tRole, HttpServletRequest request) throws MalformedURLException {

		// pruefen ob Recht bereits vorhanden ist
		if (roleService.findByName(tRole.getName()).isPresent()) {
			LOGGER.info("POST Role: Rolle {} existiert bereits", tRole.getName());
			RestMessage message = new RestMessage(HttpStatus.CONFLICT, "Role [" + tRole.getName() + "] already defined");
			return new ResponseEntity<RestMessage>(message, message.getState());
		}	
		
		// Recht speichern
		Role savedRole = roleService.save(tRole.getName(), tRole.getDescription());

		// Http Header fuer response vorbereiten
		URL url = new URL(request.getRequestURL().toString());
	    HttpHeaders headers = new HttpHeaders();
	    String hostUri = url.getProtocol() + "://" + url.getHost() + ":" + url.getPort();
	    URI locationUri = URI.create(hostUri + "/api/role/" + savedRole.getName());
	    headers.setLocation(locationUri);

	    ExtendedRoleDto role = roleConverter.createFullRoleDto(savedRole);
	    
	    // response zurueck geben
	    return new ResponseEntity<ExtendedRoleDto>(role, headers, HttpStatus.CREATED);

	}
	
	/**
	 * Liest ein Rolle aus der Datenbank
	 * 
	 * @param roleId
	 * @return Role Object
	 */
	@RequestMapping(value="/{roleName}", method = RequestMethod.GET, produces="application/json")
	@PreAuthorize("hasAuthority('usermanager_showrole')")
	public @ResponseBody ResponseEntity<?> getRole(@PathVariable String roleName) {

		Optional<Role> tRole = roleService.findByRoleNameWithGraphInitialized(roleName);

		// pruefen ob Rolle vorhanden ist
		if (!tRole.isPresent()) {
			LOGGER.info("GET Role: Rolle {} existiert nicht", roleName);
			RestMessage message = new RestMessage(HttpStatus.NOT_FOUND, "Role [" + roleName + "] not found");
			return new ResponseEntity<RestMessage>(message, message.getState());
		}

		ExtendedRoleDto role = roleConverter.createFullRoleDto(tRole.get());
		
		// response zurueck geben
		return new ResponseEntity<ExtendedRoleDto>(role, HttpStatus.OK);
	}

	/**
	 * Loescht ein Rolle aus der Datenbank
	 * 
	 * @param roleId
	 * @return
	 */
	@RequestMapping(value="/{roleName}", method = RequestMethod.DELETE, produces="application/json")
	@PreAuthorize("hasAuthority('usermanager_deleterole')")
	public @ResponseBody ResponseEntity<?> deleteRole(@PathVariable String roleName) {

		Optional<Role> tRole = roleService.findByName(roleName);

		// pruefen ob Rolle vorhanden ist
		if (!tRole.isPresent()) {
			LOGGER.info("DELETE Role: Rolle {} existiert nicht", roleName);
			RestMessage message = new RestMessage(HttpStatus.NOT_FOUND, "Role [" + roleName + "] not found! Cannot delete");
			return new ResponseEntity<RestMessage>(message, message.getState());
		}

		roleService.delete(tRole.get());

		RestMessage message = new RestMessage(HttpStatus.OK, "Role [" + roleName + "] successfully deleted");

		// response zurueck geben
		return new ResponseEntity<RestMessage>(message , HttpStatus.OK);
	}

	/**
	 * Dated ein Rolle in der Datenbank ab
	 * 
	 * @param tRole
	 * @param roleName
	 * @param request
	 * @return
	 * @throws MalformedURLException
	 */
	@RequestMapping(value="/{roleName}", method = RequestMethod.PUT, consumes="application/json", produces="application/json")
	@PreAuthorize("hasAuthority('usermanager_updaterole')")
	public @ResponseBody ResponseEntity<?> updateRole(@RequestBody Role tRole, @PathVariable String roleName,  HttpServletRequest request) throws MalformedURLException {
		
		// Rolle updaten
		Optional<Role> role = roleService.update(roleName, tRole);
		
		if (!role.isPresent()) {
			LOGGER.info("PUT Role: Rolle {} konnte nicht upgedated werden.", roleName);
			RestMessage message = new RestMessage(HttpStatus.CONFLICT, "Role [" + roleName + "] update not possible");
			return new ResponseEntity<RestMessage>(message, message.getState());
		}

		// Http Header fuer response vorbereiten
		URL url = new URL(request.getRequestURL().toString());
	    HttpHeaders headers = new HttpHeaders();
	    String hostUri = url.getProtocol() + "://" + url.getHost() + ":" + url.getPort();
	    URI locationUri = URI.create(hostUri + "/api/role/" + role.get().getName());
	    headers.setLocation(locationUri);

	    // response zurueck geben
	    return new ResponseEntity<Role>(role.get(), headers, HttpStatus.OK);

	}
	
	/**
	 * Fuegt einer Rolle ein Recht hinzu
	 * 
	 * @param roleName
	 * @param rightName
	 * @param request
	 * @return
	 * @throws MalformedURLException
	 */
	@RequestMapping(value="/{roleName}/right/{rightName}", method = RequestMethod.PUT , produces="application/json")
	@PreAuthorize("hasAuthority('usermanager_roleaddright')")
	public @ResponseBody ResponseEntity<?> roleAddRight(@PathVariable String roleName, @PathVariable String rightName,  HttpServletRequest request) throws MalformedURLException {

		Optional<Role> role = roleService.findByName(roleName);
		
		Optional<Right> right = rightService.findByName(rightName);
		
		// pruefen ob Rolle vorhanden ist
		if (!role.isPresent()) {
			LOGGER.info("PUT Role Right: Rolle {} existiert nicht.", roleName);
			RestMessage message = new RestMessage(HttpStatus.NOT_FOUND, "Role [" + roleName + "] not exists. Create it first.");
			return new ResponseEntity<RestMessage>(message, message.getState());
		}
		
		// pruefen ob Recht vorhanden
		if (!right.isPresent()) {
			LOGGER.info("PUT Role Right: Recht {} existiert nicht.", rightName);
			RestMessage message = new RestMessage(HttpStatus.NOT_FOUND, "Recht [" + rightName + "] not exists. Create it first.");
			return new ResponseEntity<RestMessage>(message, message.getState());
		}

		Role tRole = roleService.roleAddRight(roleName, rightName);
		

		ExtendedRoleDto responseRole = roleConverter.createFullRoleDto(tRole);
		
	    // response zurueck geben
	    return new ResponseEntity<ExtendedRoleDto>(responseRole, HttpStatus.OK);

	}
	
	/**
	 * Entfernt ein Recht von einer Rolle
	 * 
	 * @param roleName
	 * @param rightName
	 * @param request
	 * @return
	 * @throws MalformedURLException
	 */
	@RequestMapping(value="/{roleName}/right/{rightName}", method = RequestMethod.DELETE, produces="application/json")
	@PreAuthorize("hasAuthority('usermanager_roleremoveright')")
	public @ResponseBody ResponseEntity<?> roleRemoveRight(@PathVariable String roleName, @PathVariable String rightName, HttpServletRequest request) throws MalformedURLException {

		Optional<Role> role = roleService.findByName(roleName);
		
		Optional<Right> right = rightService.findByName(rightName);
		
		// pruefen ob Rolle vorhanden ist
		if (!role.isPresent()) {
			LOGGER.info("DELETE Role Right: Rolle {} existiert nicht.", roleName);
			RestMessage message = new RestMessage(HttpStatus.NOT_FOUND, "Role [" + roleName + "] not exists. Create it first");
			return new ResponseEntity<RestMessage>(message, message.getState());
		}
		
		// pruefen ob recht vorhanden
		if (!right.isPresent()) {
			LOGGER.info("DELETE Role Right: Recht {} existiert nicht.", rightName);
			RestMessage message = new RestMessage(HttpStatus.NOT_FOUND, "Right [" + rightName + "] not exists.");
			return new ResponseEntity<RestMessage>(message, message.getState());
		}

		// Recht von Rolle entfernen
		Role tRole = roleService.removeRightFromRole(roleName, rightName);
		
		ExtendedRoleDto responseRole = roleConverter.createFullRoleDto(tRole);

	    // response zurueck geben
	    return new ResponseEntity<ExtendedRoleDto>(responseRole, HttpStatus.OK);

	}
	
}
