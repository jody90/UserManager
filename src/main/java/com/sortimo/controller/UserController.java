package com.sortimo.controller;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

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

import com.sortimo.dto.UserGetDto;
import com.sortimo.dto.UserPostDto;
import com.sortimo.model.Right;
import com.sortimo.model.Role;
import com.sortimo.model.User;
import com.sortimo.repositories.RightRepository;
import com.sortimo.repositories.RoleRepository;
import com.sortimo.repositories.UserRepository;
import com.sortimo.services.RestMessage;
import com.sortimo.services.database.UserService;

@RestController
@RequestMapping(value="/api/user")
public class UserController {

	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private RightRepository rightRepo;
	
	@Autowired
	private RoleRepository roleRepo;
	
	@Autowired
	private UserService userService;
	
	/**
	 * Liest alle Benutzer aus der Datenbank aus
	 * 
	 * @return Collection von User Objekten
	 */
	@RequestMapping(value="", method = RequestMethod.GET, produces="application/json")
	@PreAuthorize("hasAnyAuthority('superRight', 'userManager_showAllUsers')")
	public @ResponseBody ResponseEntity<?> getAllUser() {
		
		List<UserGetDto> usersCollection = userService.findAll();
				
		// response zurueck geben
		return new ResponseEntity<List<UserGetDto>>(usersCollection, HttpStatus.OK);
		
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
		
		UserGetDto user;
		
		try {			
			user = userService.findByUsername(username);
		}
		catch (NullPointerException e) {
			RestMessage error = new RestMessage(404, "User [" + username + "] not found");
			return new ResponseEntity<RestMessage>(error, HttpStatus.NOT_FOUND);
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
			RestMessage error = new RestMessage(404, "User [" + user.getUsername() + "] already defined");
			return new ResponseEntity<RestMessage>(error, HttpStatus.CONFLICT);
		}
		
		// Benutzer speichern
		UserPostDto savedUser = userService.create(user);

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

		User user = userRepo.findByUsername(username);

		// pruefen ob benutzer vorhanden ist
		if (user == null) {
			RestMessage error = new RestMessage(404, "User [" + username + "] not found! Cannot delete");
			return new ResponseEntity<RestMessage>(error, HttpStatus.NOT_FOUND);
		}

		userRepo.delete(user);

		RestMessage message = new RestMessage(200, "User [" + username + "] successfully deleted");

		// response zurueck geben
		return new ResponseEntity<RestMessage>(message , HttpStatus.OK);
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

		User existingUser = userRepo.findByUsername(username);
		
		// pruefen ob benutzer vorhanden ist
		if (existingUser == null) {
			RestMessage error = new RestMessage(404, "User [" + username + "] not exists. Create it first");
			return new ResponseEntity<RestMessage>(error, HttpStatus.NOT_FOUND);
		}

		user.setUsername(username);
		
		if (existingUser.getRights().contains(rightRepo.findByName("Testrecht1"))) {
			System.out.println("User hat das Recht");
			existingUser.getRights().remove(rightRepo.findByName("Testrecht1"));
		}

		// Benutzer speichern
		userRepo.save(user);

	    // response zurueck geben
	    return new ResponseEntity<User>(user, HttpStatus.OK);

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
	@RequestMapping(value="/{username}/right/{rightId}", method = RequestMethod.PUT	, produces="application/json")
	@PreAuthorize("hasAnyAuthority('superRight', 'userManager_userAddRight')")
	public @ResponseBody ResponseEntity<?> userAddRight(@PathVariable String username, @PathVariable Long rightId,  HttpServletRequest request) throws MalformedURLException {

		User user = userRepo.findByUsername(username);
		
		Right right = rightRepo.findOne(rightId);
		
		// pruefen ob recht vorhanden
		if (right == null) {
			RestMessage error = new RestMessage(404, "Right [" + rightId + "] not exists.");
			return new ResponseEntity<RestMessage>(error, HttpStatus.NOT_FOUND);
		}
		
		// pruefen ob benutzer vorhanden ist
		if (user == null) {
			RestMessage error = new RestMessage(404, "User [" + username + "] not exists. Create it first");
			return new ResponseEntity<RestMessage>(error, HttpStatus.NOT_FOUND);
		}

		user.getRights().add(right);
		
		// Benutzer speichern
		userRepo.save(user);

	    // response zurueck geben
	    return new ResponseEntity<User>(user, HttpStatus.OK);

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
	@RequestMapping(value="/{username}/right/{rightId}", method = RequestMethod.DELETE, produces="application/json")
	@PreAuthorize("hasAnyAuthority('superRight', 'userManager_userRemoveRight')")
	public @ResponseBody ResponseEntity<?> userRemoveRight(@PathVariable String username, @PathVariable Long rightId,  HttpServletRequest request) throws MalformedURLException {

		User user = userRepo.findByUsername(username);
		
		Right right = rightRepo.findOne(rightId);
		
		// pruefen ob benutzer vorhanden ist
		if (user == null) {
			RestMessage error = new RestMessage(404, "User [" + username + "] not exists. Create it first");
			return new ResponseEntity<RestMessage>(error, HttpStatus.NOT_FOUND);
		}
		
		// pruefen ob recht vorhanden
		if (right == null) {
			RestMessage error = new RestMessage(404, "Right [" + rightId + "] not exists.");
			return new ResponseEntity<RestMessage>(error, HttpStatus.NOT_FOUND);
		}
		
		// prufen ob user das recht besitzt
		if (!user.getRights().contains(right)) {
			RestMessage error = new RestMessage(404, "User [" + username + "] does not have the right [" + rightId + "]. Could not be deleted!");
			return new ResponseEntity<RestMessage>(error, HttpStatus.NOT_FOUND);
		}

		// recht von benutzer entfernen
		user.getRights().remove(right);
		
		// benutzer speichern
		userRepo.save(user);

	    // response zurueck geben
	    return new ResponseEntity<User>(user, HttpStatus.OK);

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
	@RequestMapping(value="/{username}/role/{roleId}", method = RequestMethod.PUT	, produces="application/json")
	@PreAuthorize("hasAnyAuthority('superRight', 'userManager_userAddRole')")
	public @ResponseBody ResponseEntity<?> userAddRole(@PathVariable String username, @PathVariable Long roleId,  HttpServletRequest request) throws MalformedURLException {

		System.out.println("roleId: "+roleId);
		
		User user = userRepo.findByUsername(username);
		
		Role role = roleRepo.findOne(roleId);
		
		// pruefen ob rolle vorhanden
		if (role == null) {
			RestMessage error = new RestMessage(404, "Role [" + roleId + "] not exists.");
			return new ResponseEntity<RestMessage>(error, HttpStatus.NOT_FOUND);
		}
		
		// pruefen ob benutzer vorhanden ist
		if (user == null) {
			RestMessage error = new RestMessage(404, "User [" + username + "] not exists. Create it first");
			return new ResponseEntity<RestMessage>(error, HttpStatus.NOT_FOUND);
		}

		user.getRoles().add(role);
		
		System.out.println("Role: "+role);
		System.out.println("User: "+user);
		
		// Benutzer speichern
		userRepo.save(user);

	    // response zurueck geben
	    return new ResponseEntity<User>(user, HttpStatus.OK);

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
	@RequestMapping(value="/{username}/role/{roleId}", method = RequestMethod.DELETE, produces="application/json")
	@PreAuthorize("hasAnyAuthority('superRight', 'userManager_userRemoveRole')")
	public @ResponseBody ResponseEntity<?> userRemoveRoles(@PathVariable String username, @PathVariable Long roleId,  HttpServletRequest request) throws MalformedURLException {

		User user = userRepo.findByUsername(username);
		
		Role role = roleRepo.findOne(roleId);
		
		// pruefen ob benutzer vorhanden ist
		if (user == null) {
			RestMessage error = new RestMessage(404, "User [" + username + "] not exists. Create it first");
			return new ResponseEntity<RestMessage>(error, HttpStatus.NOT_FOUND);
		}
		
		// pruefen ob rolle vorhanden
		if (role == null) {
			RestMessage error = new RestMessage(404, "Role [" + roleId + "] not exists.");
			return new ResponseEntity<RestMessage>(error, HttpStatus.NOT_FOUND);
		}
		
		// prufen ob user die Rolle besitzt
		if (!user.getRoles().contains(role)) {
			RestMessage error = new RestMessage(404, "User [" + username + "] does not have the role [" + roleId + "]. Could not be deleted!");
			return new ResponseEntity<RestMessage>(error, HttpStatus.NOT_FOUND);
		}

		// recht von benutzer entfernen
		user.getRoles().remove(role);
		
		// benutzer speichern
		userRepo.save(user);

	    // response zurueck geben
	    return new ResponseEntity<User>(user, HttpStatus.OK);

	}
	

}
