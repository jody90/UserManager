package de.sortimo.rest.controller;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.Optional;

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
import de.sortimo.rest.converter.UserConverter;
import de.sortimo.rest.dto.ExtendedUserDto;
import de.sortimo.rest.dto.JwtUserDto;
import de.sortimo.rest.dto.SimpleUserDto;
import de.sortimo.service.RightService;
import de.sortimo.service.RoleService;
import de.sortimo.service.UserService;
import de.sortimo.service.model.Right;
import de.sortimo.service.model.Role;
import de.sortimo.service.model.User;

@RestController
@RequestMapping(value="/api/user")
public class UserController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	private UserService userService;

	@Autowired
	private RightService rightService;
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private UserConverter userConverter;
	
	/**
	 * Liest alle User aus der Datenbank
	 * 
	 * @return Collection von User Objekten
	 */
	@RequestMapping(method = RequestMethod.GET, produces="application/json")
	@PreAuthorize("hasAuthority('usermanager_showusers')")
	public @ResponseBody ResponseEntity<?> getAllUsers() {
		
		Optional<Iterable<User>> usersCollection = userService.findAllWithoutGraph();

		// pruefen ob Rechte vorhanden sind
		if (!usersCollection.isPresent()) {
			LOGGER.info("GET Users: Es wurden keine Benutzer in der Datenbank gefunden.");
			RestMessage message = new RestMessage(HttpStatus.NOT_FOUND, "No Users found");
			return new ResponseEntity<RestMessage>(message, message.getState());
		}
		
		List<SimpleUserDto> users = userConverter.createDtoPreviewList(usersCollection.get());
	
		// response zurueck geben
		return new ResponseEntity<List<SimpleUserDto>>(users, HttpStatus.OK);
		
	}
	
	/**
	 * Liest einen Benutzer anhand von username aus Datenbank
	 * 
	 * @param username
	 * @return ExtendedUserDto Object
	 */
	@RequestMapping(value="/{username}", method = RequestMethod.GET, produces="application/json")
	@PreAuthorize("hasAuthority('usermanager_showuser')")
	public @ResponseBody ResponseEntity<?> getUser(@PathVariable String username) {
		
		Optional<User> tUser = userService.findByUsernameWithGraphInitialized(username);

		if (!tUser.isPresent()) {
			LOGGER.info("GET User: Es wurde ein Benutzer [{}] angefragt der nicht in der Datenbank existiert.", username);
			RestMessage message = new RestMessage(HttpStatus.NOT_FOUND, "User [" + username + "] not found");
			return new ResponseEntity<RestMessage>(message, message.getState());
		}
		
		
		ExtendedUserDto user = userConverter.createFullDto(tUser.get());

		// response zurueck geben
		return new ResponseEntity<ExtendedUserDto>(user, HttpStatus.OK);
		
	}
	
	/**
	 * Gibt einen JwtBenutzer zurück
	 * 
	 * @param username
	 * @return JwtUser Object
	 */
	@RequestMapping(value="/jwt/{username}", method = RequestMethod.GET, produces="application/json")
	@PreAuthorize("hasAuthority('usermanager_showuser')")
	public @ResponseBody ResponseEntity<?> getJwtUser(@PathVariable String username) {
		
		Optional<User> tUser = userService.findByUsernameWithGraphInitialized(username);

		if (!tUser.isPresent()) {
			LOGGER.info("GET JwtUser: Es wurde ein Benutzer [{}] angefragt der nicht in der Datenbank existiert.", username);
			RestMessage message = new RestMessage(HttpStatus.NOT_FOUND, "User [" + username + "] not found");
			return new ResponseEntity<RestMessage>(message, message.getState());
		}
		
		JwtUserDto user = userConverter.getJwtUser(tUser.get());

		// response zurueck geben
		return new ResponseEntity<JwtUserDto>(user, HttpStatus.OK);
		
	}

	/**
	 * Legt einen neuen Benutzer an
	 * 
	 * @param user
	 * @param request
	 * @return
	 * @throws MalformedURLException
	 */
	@RequestMapping(method = RequestMethod.POST, consumes="application/json", produces="application/json")
	@PreAuthorize("hasAuthority('usermanager_createuser')")
	public @ResponseBody ResponseEntity<?> register(@RequestBody User pUser, HttpServletRequest request) throws MalformedURLException {
		
		Optional<User> tUser = userService.findByUsername(pUser.getUsername());
		
		// pruefen ob benutzer bereits vorhanden ist
		if (tUser.isPresent()) {
			LOGGER.info("POST User: Es wurde verucht einen Benutzer [{}] anzulegen, der schon existiert.", pUser.getUsername());
			RestMessage message = new RestMessage(HttpStatus.CONFLICT, "User [" + pUser.getUsername() + "] already defined");
			return new ResponseEntity<RestMessage>(message, message.getState());
		}
		
		// Benutzer speichern
		User savedUser = userService.saveNew(pUser.getUsername(), pUser.getPassword(), pUser.getFirstname(), pUser.getLastname(), pUser.getEmail(), pUser.getRights(), pUser.getRoles());

		// Http Header fuer response vorbereiten
		URL url = new URL(request.getRequestURL().toString());
	    HttpHeaders headers = new HttpHeaders();
	    String hostUri = url.getProtocol() + "://" + url.getHost() + ":" + url.getPort();
	    URI locationUri = URI.create(hostUri + "/api/user/" + savedUser.getUsername());
	    headers.setLocation(locationUri);
	    
	    ExtendedUserDto user = userConverter.createFullDto(savedUser);

	    // response zurueck geben
	    return new ResponseEntity<ExtendedUserDto>(user, headers, HttpStatus.CREATED);

	}
	
	/**
	 * Ändert das Passwort des Benutzers
	 * 
	 * @param user
	 * @param request
	 * @return
	 * @throws MalformedURLException
	 */
	@RequestMapping(value="/{username}", method = RequestMethod.POST, consumes="application/json", produces="application/json")
	public @ResponseBody ResponseEntity<?> changePassword(@PathVariable String username, @RequestBody String password) throws MalformedURLException {
		
		Optional<User> tUser = userService.findByUsername(username);
		
		// pruefen ob benutzer bereits vorhanden ist
		if (!tUser.isPresent()) {
			LOGGER.info("POST User Passwort: Benuzter [{}] existiert nicht. Kann Passwort nicht ändern", username);
			RestMessage message = new RestMessage(HttpStatus.NOT_FOUND, "User [" + username + "] does not exist");
			return new ResponseEntity<RestMessage>(message, message.getState());
		}
		
		userService.changePassword(username, password);

		RestMessage message = new RestMessage(HttpStatus.OK, "Password for User [" + username + "] changed");
		return new ResponseEntity<RestMessage>(message, message.getState());

	}
	
	/**
	 * Loescht einen Benutzer
	 * 
	 * @param username
	 * @return
	 */
	@RequestMapping(value="/{username}", method = RequestMethod.DELETE, produces="application/json")
	@PreAuthorize("hasAuthority('usermanager_deleteuser')")
	public @ResponseBody ResponseEntity<?> deleteUser(@PathVariable String username) {

		Optional<User> tUser = userService.findByUsername(username);
		
		if (!tUser.isPresent()) {
			LOGGER.info("DELETE User: Es wurde ein verucht einen Benutzer [{}] zu löschen, der nicht existiert.", username);
			RestMessage message = new RestMessage(HttpStatus.NOT_FOUND, "User [" + username + "] not found! Cannot delete");
			return new ResponseEntity<RestMessage>(message, message.getState());
		}
		
		User user = tUser.get();
		
		if (user.getUsername().equals("superadmin")) {
			LOGGER.info("DELETE User: Es wurde ein verucht den Benutzer [{}] zu löschen, nicht erlaubt.", user.getUsername());
			RestMessage message = new RestMessage(HttpStatus.FORBIDDEN, "User [" + user.getUsername() + "] cannot be deleted");
			return new ResponseEntity<RestMessage>(message, message.getState());
		}
		
		if (!userService.delete(user.getId())) {
			LOGGER.info("DELETE User: Benutzer [{}] wurde gelöscht.", user.getUsername());
			RestMessage message = new RestMessage(HttpStatus.INTERNAL_SERVER_ERROR, "User [" + user.getUsername() + "] was not deleted");
			return new ResponseEntity<RestMessage>(message, message.getState());
		}
		
		RestMessage message = new RestMessage(HttpStatus.OK, "User [" + user.getUsername() + "] deleted");
		return new ResponseEntity<RestMessage>(message, message.getState());

	}

	/**
	 * Dated einen bestehenden Benutzer ab
	 * 
	 * @param user
	 * @param username
	 * @param request
	 * @return
	 * @throws MalformedURLException
	 */
	@RequestMapping(value="/{username}", method = RequestMethod.PUT, consumes="application/json", produces="application/json")
	@PreAuthorize("hasAuthority('usermanager_updateuser')")
	public @ResponseBody ResponseEntity<?> update(@RequestBody User pUser, @PathVariable String username,  HttpServletRequest request) throws MalformedURLException {

		Optional<User> tUser = userService.findByUsername(username);
		
		if (!tUser.isPresent()) {
			LOGGER.info("PUT User: Es wurde ein verucht einen Benutzer [{}] zu aktualisieren, der nicht existiert.", username);
			RestMessage message = new RestMessage(HttpStatus.NOT_FOUND, "User [" + username + "] not exists. Create it first");
			return new ResponseEntity<RestMessage>(message, message.getState());
		}
		
		User updatedUser = userService.update(username, pUser).get();
		    
	    SimpleUserDto userResponse = userConverter.createPreviewDto(updatedUser);

	    // response zurueck geben
	    return new ResponseEntity<SimpleUserDto>(userResponse, HttpStatus.OK);
		

	}
	
	/**
	 * Fuegt einem User ein Recht hinzu
	 * 
	 * @param username
	 * @param rightName
	 * @param request
	 * @return
	 * @throws MalformedURLException
	 */
	@RequestMapping(value="/{username}/right/{rightName}", method = RequestMethod.PUT, produces="application/json")
	@PreAuthorize("hasAuthority('usermanager_useraddright')")
	public @ResponseBody ResponseEntity<?> userAddRight(@PathVariable String username, @PathVariable String rightName,  HttpServletRequest request) throws MalformedURLException {

		Optional<User> tUser = userService.findByUsername(username);

		Optional<Right> tRight = rightService.findByName(rightName);
		
		// pruefen ob recht vorhanden
		if (!tUser.isPresent()) {
			LOGGER.info("PUT User Right: Es wurde verucht einen Benutzer [{}] zu aktualisieren, der nicht existiert.", username);
			RestMessage message = new RestMessage(HttpStatus.NOT_FOUND, "User [" + username + "] not exists. Create it first");
			return new ResponseEntity<RestMessage>(message, message.getState());
		}
		
		// pruefen ob recht vorhanden
		if (!tRight.isPresent()) {
			LOGGER.info("PUT User Right: Es wurde verucht einem Benutzer [{}] ein Recht [{}] zu geben das nicht existiert.", username, rightName);
			RestMessage message = new RestMessage(HttpStatus.NOT_FOUND, "Right [" + rightName + "] not exists.");
			return new ResponseEntity<RestMessage>(message, message.getState());
		}
		
		User updatedUser = userService.userAddRight(username, rightName).get();
		
		ExtendedUserDto responseUser = userConverter.createFullDto(updatedUser);
		
	    // response zurueck geben
	    return new ResponseEntity<ExtendedUserDto>(responseUser, HttpStatus.OK);

	}
	
	/**
	 * Entfernt ein Recht vom User
	 * 
	 * @param username
	 * @param rightName
	 * @param request
	 * @return
	 * @throws MalformedURLException
	 */
	@RequestMapping(value="/{username}/right/{rightName}", method = RequestMethod.DELETE, produces="application/json")
	@PreAuthorize("hasAuthority('usermanager_userremoveright')")
	public @ResponseBody ResponseEntity<?> userRemoveRight(@PathVariable String username, @PathVariable String rightName,  HttpServletRequest request) throws MalformedURLException {

		Optional<User> tUser = userService.findByUsernameWithGraphInitialized(username);
		
		Optional<Right> tRight = rightService.findByName(rightName);
		
		// pruefen ob benutzer vorhanden ist
		if (!tUser.isPresent()) {
			LOGGER.info("DELETE User Right: Es wurde verucht einen Benutzer [{}] zu aktualisieren, der nicht existiert.", username);
			RestMessage message = new RestMessage(HttpStatus.NOT_FOUND, "User [" + username + "] not exists. Create it first");
			return new ResponseEntity<RestMessage>(message, message.getState());
		}
		
		// pruefen ob recht vorhanden
		if (!tRight.isPresent()) {
			LOGGER.info("DELETE User Right: Es wurde verucht einem Benutzer [{}] ein Recht [{}] zu entziehen das nicht existiert.", username, rightName);
			RestMessage message = new RestMessage(HttpStatus.NOT_FOUND, "Right [" + rightName + "] not exists.");
			return new ResponseEntity<RestMessage>(message, message.getState());
		}
		
		User currentUser = tUser.get();
		Right currentRight = tRight.get();
		
		// prufen ob user das recht besitzt
		if (!currentUser.getRights().contains(currentRight)) {
			LOGGER.info("DELETE User Right: Es wurde verucht einem Benutzer [{}] ein Recht [{}] zu entziehen das er garnicht besitzt.", username, rightName);
			RestMessage message = new RestMessage(HttpStatus.CONFLICT, "User [" + username + "] does not have the right [" + rightName + "]. Could not be deleted!");
			return new ResponseEntity<RestMessage>(message, message.getState());
		}

		// recht von benutzer entfernen
		User updatedUser = userService.removeRightFromUser(username, rightName).get();
		
		ExtendedUserDto returnUser = userConverter.createFullDto(updatedUser);

	    // response zurueck geben
	    return new ResponseEntity<ExtendedUserDto>(returnUser, HttpStatus.OK);

	}
	
	/**
	 * Fuegt einem User eine Rolle hinzu
	 * 
	 * @param username
	 * @param roleName
	 * @param request
	 * @return
	 * @throws MalformedURLException
	 */
	@RequestMapping(value="/{username}/role/{roleName}", method = RequestMethod.PUT, produces="application/json")
	@PreAuthorize("hasAuthority('usermanager_useraddrole')")
	public @ResponseBody ResponseEntity<?> userAddRole(@PathVariable String username, @PathVariable String roleName,  HttpServletRequest request) throws MalformedURLException {

		Optional<User> tUser = userService.findByUsername(username);

		Optional<Role> tRole = roleService.findByName(roleName);
		
		// pruefen ob recht vorhanden
		if (!tUser.isPresent()) {
			LOGGER.info("PUT User Role: Es wurde verucht einen Benutzer [{}] zu aktualisieren, der nicht existiert.", username);
			RestMessage message = new RestMessage(HttpStatus.NOT_FOUND, "User [" + username + "] not exists. Create it first");
			return new ResponseEntity<RestMessage>(message, message.getState());
		}
		
		// pruefen ob recht vorhanden
		if (!tRole.isPresent()) {
			LOGGER.info("PUT User Role: Es wurde verucht einem Benutzer [{}] eine Rolle [{}] zu geben das nicht existiert.", username, roleName);
			RestMessage message = new RestMessage(HttpStatus.NOT_FOUND, "Right [" + roleName + "] not exists.");
			return new ResponseEntity<RestMessage>(message, message.getState());
		}
			
		User updatedUser = userService.userAddRole(username, roleName).get();
		
		ExtendedUserDto responseUser = userConverter.createFullDto(updatedUser);
		
	    // response zurueck geben
	    return new ResponseEntity<ExtendedUserDto>(responseUser, HttpStatus.OK);

	}
	
	/**
	 * Entfernt eine Rolle vom User
	 * 
	 * @param username
	 * @param roleName
	 * @param request
	 * @return
	 * @throws MalformedURLException
	 */
	@RequestMapping(value="/{username}/role/{roleName}", method = RequestMethod.DELETE, produces="application/json")
	@PreAuthorize("hasAuthority('usermanager_userremoverole')")
	public @ResponseBody ResponseEntity<?> userRemoveRole(@PathVariable String username, @PathVariable String roleName,  HttpServletRequest request) throws MalformedURLException {

		Optional<User> tUser = userService.findByUsernameWithGraphInitialized(username);
		
		Optional<Role> tRole = roleService.findByName(roleName);
		
		// pruefen ob benutzer vorhanden ist
		if (!tUser.isPresent()) {
			LOGGER.info("DELETE User Role: Es wurde verucht einen Benutzer [{}] zu aktualisieren, der nicht existiert.", username);
			RestMessage message = new RestMessage(HttpStatus.NOT_FOUND, "User [" + username + "] not exists. Create it first");
			return new ResponseEntity<RestMessage>(message, message.getState());
		}
		
		// pruefen ob recht vorhanden
		if (!tRole.isPresent()) {
			LOGGER.info("DELETE User Role: Es wurde verucht einem Benutzer [{}] eine Rolle [{}] zu entziehen das nicht existiert.", username, roleName);
			RestMessage message = new RestMessage(HttpStatus.NOT_FOUND, "Role [" + roleName + "] not exists.");
			return new ResponseEntity<RestMessage>(message, message.getState());
		}
		
		User currentUser = tUser.get();
		Role currentRole = tRole.get();
		
		// prufen ob user das recht besitzt
		if (!currentUser.getRoles().contains(currentRole)) {
			LOGGER.info("DELETE User Role: Es wurde verucht einem Benutzer [{}] eine Rolle [{}] zu entziehen das er garnicht besitzt.", username, roleName);
			RestMessage message = new RestMessage(HttpStatus.CONFLICT, "User [" + username + "] does not have the role [" + roleName + "]. Could not be deleted!");
			return new ResponseEntity<RestMessage>(message, message.getState());
		}

		// recht von benutzer entfernen
		User updatedUser = userService.removeRoleFromUser(username, roleName).get();
		
		ExtendedUserDto returnUser = userConverter.createFullDto(updatedUser);

	    // response zurueck geben
	    return new ResponseEntity<ExtendedUserDto>(returnUser, HttpStatus.OK);

	}
	

}
