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
import de.sortimo.rest.dto.SimpleRoleDto;
import de.sortimo.rest.dto.UserGetDto;
import de.sortimo.rest.dto.UserPostDto;
import de.sortimo.service.RightService;
import de.sortimo.service.UserService;
import de.sortimo.service.model.Right;
import de.sortimo.service.model.Role;
import de.sortimo.service.model.User;
import de.sortimo.service.repositories.RightRepository;
import de.sortimo.service.repositories.RoleRepository;
import de.sortimo.service.repositories.UserRepository;

@RestController
@RequestMapping(value="/api/user")
public class UserController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	private RightService rightService;
	
	@Autowired
	private RightRepository rightRepo;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private RoleRepository roleRepo;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserConverter userConverter;
	
	/**
	 * Liest alle User aus der Datenbank
	 * 
	 * @return Collection von User Objekten
	 */
	@RequestMapping(method = RequestMethod.GET, produces="application/json")
	public @ResponseBody ResponseEntity<?> getAllUsers() {
		
		Optional<Iterable<User>> usersCollection = userService.findAllWithoutGraph();

		// pruefen ob Rechte vorhanden sind
		if (!usersCollection.isPresent()) {
			LOGGER.info("GET Users: Es wurden keine Benutzer in der Datenbank gefunden.");
			RestMessage message = new RestMessage(HttpStatus.NOT_FOUND, "No Users found");
			return new ResponseEntity<RestMessage>(message, message.getState());
		}
		
		List<SimpleUserDto> roles = userConverter.createDtoPreviewList(usersCollection.get());
	
		// response zurueck geben
		return new ResponseEntity<List<SimpleRoleDto>>(roles, HttpStatus.OK);
		
	}
	
	/**
	 * Liest einen Benutzer anhand von username aus Datenbank
	 * 
	 * @param username
	 * @return User Object
	 */
	@RequestMapping(value="/{username}", method = RequestMethod.GET, produces="application/json")
	@PreAuthorize("hasAnyAuthority('superRight', 'userManager_showUser')")
	public @ResponseBody ResponseEntity<?> getUser(@PathVariable String username) {
		
		UserGetDto user = null;
		
		try {		
			user = userService.findByUsername(username);
		}
		catch (NullPointerException e) {
			RestMessage message = new RestMessage(HttpStatus.NOT_FOUND, "User [" + username + "] not found");
			return new ResponseEntity<RestMessage>(message, message.getState());
		}

		// response zurueck geben
		return new ResponseEntity<UserGetDto>(user, HttpStatus.OK);
		
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
	@PreAuthorize("hasAnyAuthority('superRight', 'userManager_createUser')")
	public @ResponseBody ResponseEntity<?> register(@RequestBody UserPostDto user, HttpServletRequest request) throws MalformedURLException {
		
		// pruefen ob benutzer bereits vorhanden ist
		if (userService.userExists(user)) {
			RestMessage message = new RestMessage(HttpStatus.CONFLICT, "User [" + user.getUsername() + "] already defined");
			return new ResponseEntity<RestMessage>(message, message.getState());
		}
		
		// Benutzer speichern
		UserPostDto savedUser = userService.save(user);

		// Http Header fuer response vorbereiten
		URL url = new URL(request.getRequestURL().toString());
	    HttpHeaders headers = new HttpHeaders();
	    String hostUri = url.getProtocol() + "://" + url.getHost() + ":" + url.getPort();
	    URI locationUri = URI.create(hostUri + "/api/user/" + user.getUsername());
	    headers.setLocation(locationUri);

	    // response zurueck geben
	    return new ResponseEntity<UserPostDto>(savedUser, headers, HttpStatus.CREATED);

	}
	
	/**
	 * Loescht einen Benutzer
	 * 
	 * @param username
	 * @return
	 */
	@RequestMapping(value="/{username}", method = RequestMethod.DELETE, produces="application/json")
	@PreAuthorize("hasAnyAuthority('superRight', 'userManager_deleteUser')")
	public @ResponseBody ResponseEntity<?> deleteUser(@PathVariable String username) {

		try {
			UserGetDto user = userService.findByUsername(username);
			
			userService.delete(user);
			
			RestMessage message = new RestMessage(HttpStatus.OK, "User [" + username + "] successfully deleted");

			// response zurueck geben
			return new ResponseEntity<RestMessage>(message , message.getState());
		}
		catch (NullPointerException e) {
			RestMessage message = new RestMessage(HttpStatus.NOT_FOUND, "User [" + username + "] not found! Cannot delete");
			return new ResponseEntity<RestMessage>(message, message.getState());
		}

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
	@PreAuthorize("hasAnyAuthority('superRight', 'userManager_updateUser')")
	public @ResponseBody ResponseEntity<?> update(@RequestBody User user, @PathVariable String username,  HttpServletRequest request) throws MalformedURLException {

		try {
			userService.findByUsername(username);
			
			user.setUsername(username);
			
			// Benutzer speichern
			userRepo.save(user);
			
			// response zurueck geben
			return new ResponseEntity<User>(user, HttpStatus.OK);
		}
		catch (NullPointerException e) {
			
			RestMessage message = new RestMessage(HttpStatus.NOT_FOUND, "User [" + username + "] not exists. Create it first");
			return new ResponseEntity<RestMessage>(message, message.getState());
		
		}

	}
	
	/**
	 * Fuegt einem User ein Recht hinzu
	 * 
	 * @param username
	 * @param rightId
	 * @param request
	 * @return
	 * @throws MalformedURLException
	 */
	@RequestMapping(value="/{username}/right/{rightName}", method = RequestMethod.PUT	, produces="application/json")
	@PreAuthorize("hasAnyAuthority('superRight', 'userManager_userAddRight')")
	public @ResponseBody ResponseEntity<?> userAddRight(@PathVariable String username, @PathVariable String rightName,  HttpServletRequest request) throws MalformedURLException {

		User user = userRepo.findByUsername(username).get();

		Optional<Right> right = rightService.findByName(rightName);
		
		// pruefen ob recht vorhanden
		if (user == null) {
			RestMessage message = new RestMessage(HttpStatus.NOT_FOUND, "User [" + username + "] not exists. Create it first");
			return new ResponseEntity<RestMessage>(message, message.getState());
		}
		
		// pruefen ob recht vorhanden
		if (!right.isPresent()) {
			RestMessage message = new RestMessage(HttpStatus.NOT_FOUND, "Right [" + rightName + "] not exists.");
			return new ResponseEntity<RestMessage>(message, message.getState());
		}
			
		user.getRights().add(right.get());
			
		UserGetDto returnUser = userConverter.getUserGetDto(user);
		
		// Benutzer speichern
		userService.save(userConverter.getUserPostDto(user));
			
		// response zurueck geben
		return new ResponseEntity<UserGetDto>(returnUser, HttpStatus.OK);

	}
	
	/**
	 * Entfernt ein Recht vom User
	 * 
	 * @param username
	 * @param rightId
	 * @param request
	 * @return
	 * @throws MalformedURLException
	 */
	@RequestMapping(value="/{username}/right/{rightName}", method = RequestMethod.DELETE, produces="application/json")
	@PreAuthorize("hasAnyAuthority('superRight', 'userManager_userRemoveRight')")
	public @ResponseBody ResponseEntity<?> userRemoveRight(@PathVariable String username, @PathVariable String rightName,  HttpServletRequest request) throws MalformedURLException {

		User user = userRepo.findByUsername(username).get();
		
		Optional<Right> right = rightService.findByName(rightName);
		
		// pruefen ob benutzer vorhanden ist
		if (user == null) {
			RestMessage message = new RestMessage(HttpStatus.NOT_FOUND, "User [" + username + "] not exists. Create it first");
			return new ResponseEntity<RestMessage>(message, message.getState());
		}
		
		// pruefen ob recht vorhanden
		if (!right.isPresent()) {
			RestMessage message = new RestMessage(HttpStatus.NOT_FOUND, "Right [" + rightName + "] not exists.");
			return new ResponseEntity<RestMessage>(message, message.getState());
		}
		
		// prufen ob user das recht besitzt
		if (!user.getRights().contains(right.get())) {
			RestMessage message = new RestMessage(HttpStatus.NOT_FOUND, "User [" + username + "] does not have the right [" + rightName + "]. Could not be deleted!");
			return new ResponseEntity<RestMessage>(message, message.getState());
		}

		// recht von benutzer entfernen
		user.getRights().remove(right.get());
		
		UserGetDto returnUser = userConverter.getUserGetDto(user);
		
		// benutzer speichern
		userService.save(userConverter.getUserPostDto(user));

	    // response zurueck geben
	    return new ResponseEntity<UserGetDto>(returnUser, HttpStatus.OK);

	}
	
	/**
	 * Fuegt einem User eine Rolle hinzu
	 * 
	 * @param username
	 * @param roleId
	 * @param request
	 * @return
	 * @throws MalformedURLException
	 */
	@RequestMapping(value="/{username}/role/{roleName}", method = RequestMethod.PUT	, produces="application/json")
	@PreAuthorize("hasAnyAuthority('superRight', 'userManager_userAddRole')")
	public @ResponseBody ResponseEntity<?> userAddRole(@PathVariable String username, @PathVariable String roleName,  HttpServletRequest request) throws MalformedURLException {

		User user = userRepo.findByUsername(username).get();
		
		Optional<Role> role = roleRepo.findByName(roleName);
		
		// pruefen ob rolle vorhanden
		if (role == null) {
			RestMessage message = new RestMessage(HttpStatus.NOT_FOUND, "Role [" + roleName + "] not exists.");
			return new ResponseEntity<RestMessage>(message, message.getState());
		}
		
		// pruefen ob benutzer vorhanden ist
		if (user == null) {
			RestMessage message = new RestMessage(HttpStatus.NOT_FOUND, "User [" + username + "] not exists. Create it first");
			return new ResponseEntity<RestMessage>(message, message.getState());
		}

		user.getRoles().add(role.get());
		
		UserGetDto returnUser = userConverter.getUserGetDto(user);
		
		// benutzer speichern
		userService.save(userConverter.getUserPostDto(user));

	    // response zurueck geben
	    return new ResponseEntity<UserGetDto>(returnUser, HttpStatus.OK);

	}
	
	/**
	 * Entfernt eine Rolle vom User
	 * 
	 * @param username
	 * @param roleId
	 * @param request
	 * @return
	 * @throws MalformedURLException
	 */
	@RequestMapping(value="/{username}/role/{roleName}", method = RequestMethod.DELETE, produces="application/json")
	@PreAuthorize("hasAnyAuthority('superRight', 'userManager_userRemoveRole')")
	public @ResponseBody ResponseEntity<?> userRemoveRoles(@PathVariable String username, @PathVariable String roleName,  HttpServletRequest request) throws MalformedURLException {

		User user = userRepo.findByUsername(username).get();
		
		Optional<Role> role = roleRepo.findByName(roleName);
		
		// pruefen ob benutzer vorhanden ist
		if (user == null) {
			RestMessage message = new RestMessage(HttpStatus.NOT_FOUND, "User [" + username + "] not exists. Create it first");
			return new ResponseEntity<RestMessage>(message, message.getState());
		}
		
		// pruefen ob rolle vorhanden
		if (role == null) {
			RestMessage message = new RestMessage(HttpStatus.NOT_FOUND, "Role [" + roleName + "] not exists.");
			return new ResponseEntity<RestMessage>(message, message.getState());
		}
		
		// prufen ob user die Rolle besitzt
		if (!user.getRoles().contains(role)) {
			RestMessage message = new RestMessage(HttpStatus.NOT_FOUND, "User [" + username + "] does not have the role [" + roleName + "]. Could not be deleted!");
			return new ResponseEntity<RestMessage>(message, message.getState());
		}

		// recht von benutzer entfernen
		user.getRoles().remove(role);
		
		UserGetDto returnUser = userConverter.getUserGetDto(user);
		
		// benutzer speichern
		userService.save(userConverter.getUserPostDto(user));

	    // response zurueck geben
	    return new ResponseEntity<UserGetDto>(returnUser, HttpStatus.OK);

	}
	

}
