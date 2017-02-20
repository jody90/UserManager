package com.sortimo.controller;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.sortimo.dao.User;
import com.sortimo.repositories.UserRepository;
import com.sortimo.services.RestErrorMessage;
import com.sortimo.services.RestMessage;

@RestController
@RequestMapping(value="/api/user")
public class UserController {

	@Autowired
	private UserRepository userRepo;

	@RequestMapping(method = RequestMethod.POST, consumes="application/json", produces="application/json")
	public @ResponseBody ResponseEntity<?> register(@RequestBody User user, HttpServletRequest request) throws MalformedURLException {

		// pruefen ob benutzer bereits vorhanden ist
		if (userRepo.findByUsername(user.getUsername()) != null) {
			RestErrorMessage error = new RestErrorMessage(404, "User [" + user.getUsername() + "] already defined");
			return new ResponseEntity<RestErrorMessage>(error, HttpStatus.CONFLICT);
		}

		// Benutzer speichern
		userRepo.save(user);

		// Http Header fuer response vorbereiten
		URL url = new URL(request.getRequestURL().toString());
	    HttpHeaders headers = new HttpHeaders();
	    String hostUri = url.getProtocol() + "://" + url.getHost() + ":" + url.getPort();
	    URI locationUri = URI.create(hostUri + "/api/user/" + user.getUsername());
	    headers.setLocation(locationUri);

	    // response zurueck geben
	    return new ResponseEntity<User>(user, headers, HttpStatus.CREATED);

	}

	@RequestMapping(value="/{username}", method = RequestMethod.GET, produces="application/json")
	public @ResponseBody ResponseEntity<?> getUser(@PathVariable String username) {

		User user = userRepo.findByUsername(username);

		// pruefen ob benutzer vorhanden ist
		if (user == null) {
			RestErrorMessage error = new RestErrorMessage(404, "User [" + username + "] not found");
			return new ResponseEntity<RestErrorMessage>(error, HttpStatus.NOT_FOUND);
		}

		// response zurueck geben
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.GET, produces="application/json")
	public @ResponseBody ResponseEntity<?> getAllUser() {
		
		Iterable<User> usersCollection = userRepo.findAll();

		// pruefen ob benutzer vorhanden ist
		if (usersCollection == null) {
			RestErrorMessage error = new RestErrorMessage(404, "No Users found");
			return new ResponseEntity<RestErrorMessage>(error, HttpStatus.NOT_FOUND);
		}

		// response zurueck geben
		return new ResponseEntity<Iterable<User>>(usersCollection, HttpStatus.OK);
		
	}

	@RequestMapping(value="/{username}", method = RequestMethod.DELETE, produces="application/json")
	public @ResponseBody ResponseEntity<?> deleteUser(@PathVariable String username) {

		User user = userRepo.findByUsername(username);

		// pruefen ob benutzer vorhanden ist
		if (user == null) {
			RestErrorMessage error = new RestErrorMessage(404, "User [" + username + "] not found! Cannot delete");
			return new ResponseEntity<RestErrorMessage>(error, HttpStatus.NOT_FOUND);
		}

		userRepo.delete(user);

		RestMessage message = new RestMessage(200, "User [" + username + "] successfully deleted");

		// response zurueck geben
		return new ResponseEntity<RestMessage>(message , HttpStatus.OK);
	}

	@RequestMapping(value="/{username}", method = RequestMethod.PUT, consumes="application/json", produces="application/json")
	public @ResponseBody ResponseEntity<?> update(@RequestBody User user, @PathVariable String username,  HttpServletRequest request) throws MalformedURLException {

		// pruefen ob benutzer bereits vorhanden ist
		if (userRepo.findByUsername(username) == null) {
			RestErrorMessage error = new RestErrorMessage(301, "User [" + username + "] not exists. Create it first");
			return new ResponseEntity<RestErrorMessage>(error, HttpStatus.NOT_FOUND);
		}

		user.setUsername(username);

		// Benutzer speichern
		userRepo.save(user);

		// Http Header fuer response vorbereiten
		URL url = new URL(request.getRequestURL().toString());
	    HttpHeaders headers = new HttpHeaders();
	    String hostUri = url.getProtocol() + "://" + url.getHost() + ":" + url.getPort();
	    URI locationUri = URI.create(hostUri + "/api/user/" + user.getUsername());
	    headers.setLocation(locationUri);

	    // response zurueck geben
	    return new ResponseEntity<User>(user, headers, HttpStatus.OK);

	}

}
