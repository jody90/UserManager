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

import com.sortimo.model.Role;
import com.sortimo.repositories.RoleRepository;
import com.sortimo.services.RestErrorMessage;
import com.sortimo.services.RestMessage;

@RestController
@RequestMapping(value="/api/role")
public class RoleController {
	
	@Autowired
	private RoleRepository roleRepo;

	/**
	 * Liest alle Rollen aus der Datenbank
	 * 
	 * @return Collection von Role Objekten
	 */
	@RequestMapping(method = RequestMethod.GET, produces="application/json")
	public @ResponseBody ResponseEntity<?> getAllRoles() {
		
		Iterable<Role> rolesCollection = roleRepo.findAll();

		// response zurueck geben
		return new ResponseEntity<Iterable<Role>>(rolesCollection, HttpStatus.OK);
		
	}
	
	/**
	 * Fuegt eine Rolle hinzu
	 * 
	 * @param role
	 * @param request
	 * @return
	 * @throws MalformedURLException
	 */
	@RequestMapping(method = RequestMethod.POST, consumes="application/json", produces="application/json")
	public @ResponseBody ResponseEntity<?> addRole(@RequestBody Role role, HttpServletRequest request) throws MalformedURLException {

		// pruefen ob Rolle bereits vorhanden ist
		if (roleRepo.findByName(role.getName()) != null) {
			RestErrorMessage error = new RestErrorMessage(404, "Role [" + role.getName() + "] already defined");
			return new ResponseEntity<RestErrorMessage>(error, HttpStatus.CONFLICT);
		}

		// Benutzer speichern
		roleRepo.save(role);

		// Http Header fuer response vorbereiten
		URL url = new URL(request.getRequestURL().toString());
	    HttpHeaders headers = new HttpHeaders();
	    String hostUri = url.getProtocol() + "://" + url.getHost() + ":" + url.getPort();
	    URI locationUri = URI.create(hostUri + "/api/user/" + role.getId());
	    headers.setLocation(locationUri);

	    // response zurueck geben
	    return new ResponseEntity<Role>(role, headers, HttpStatus.CREATED);

	}
	
	/**
	 * Liest eine Rolle aus der Datenbank
	 * 
	 * @param roleId
	 * @return Role Object
	 */
	@RequestMapping(value="/{roleId}", method = RequestMethod.GET, produces="application/json")
	public @ResponseBody ResponseEntity<?> getRole(@PathVariable Long roleId) {

		Role role = roleRepo.findOne(roleId);

		// pruefen ob benutzer vorhanden ist
		if (role == null) {
			RestErrorMessage error = new RestErrorMessage(404, "Role [" + roleId + "] not found");
			return new ResponseEntity<RestErrorMessage>(error, HttpStatus.NOT_FOUND);
		}

		// response zurueck geben
		return new ResponseEntity<Role>(role, HttpStatus.OK);
	}
	

	
	
	
	
	@RequestMapping(value="/{roleId}/user", method = RequestMethod.GET, produces="application/json")
	public @ResponseBody ResponseEntity<?> getUsersRole(@PathVariable Long roleId) {

//		Role role = roleRepo.findByName(rolename);
		Role role = roleRepo.findOne(roleId);
		
		System.out.println("Role: " + role);

		// pruefen ob benutzer vorhanden ist
		if (role == null) {
			RestErrorMessage error = new RestErrorMessage(404, "Role [" + roleId + "] not found");
			return new ResponseEntity<RestErrorMessage>(error, HttpStatus.NOT_FOUND);
		}

		// response zurueck geben
		return new ResponseEntity<Role>(role, HttpStatus.OK);
	}

	/**
	 * Loescht eine Rolle aus der Datenbank
	 * 
	 * @param roleId
	 * @return
	 */
	@RequestMapping(value="/{roleId}", method = RequestMethod.DELETE, produces="application/json")
	public @ResponseBody ResponseEntity<?> deleteRole(@PathVariable Long roleId) {

		Role role = roleRepo.findOne(roleId);

		// pruefen ob Rolle vorhanden ist
		if (role == null) {
			RestErrorMessage error = new RestErrorMessage(404, "Role [" + roleId + "] not found! Cannot delete");
			return new ResponseEntity<RestErrorMessage>(error, HttpStatus.NOT_FOUND);
		}

		roleRepo.delete(role);

		RestMessage message = new RestMessage(200, "Role [" + roleId + "] successfully deleted");

		// response zurueck geben
		return new ResponseEntity<RestMessage>(message , HttpStatus.OK);
	}

	/**
	 * Dated eine Rolle in der Datenbank ab
	 * 
	 * @param role
	 * @param roleId
	 * @param request
	 * @return
	 * @throws MalformedURLException
	 */
	@RequestMapping(value="/{roleId}", method = RequestMethod.PUT, consumes="application/json", produces="application/json")
	public @ResponseBody ResponseEntity<?> updateRole(@RequestBody Role role, @PathVariable Long roleId,  HttpServletRequest request) throws MalformedURLException {

		Role storedRole = roleRepo.findOne(roleId);
		
		// pruefen ob Rolle bereits vorhanden ist
		if (storedRole == null) {
			RestErrorMessage error = new RestErrorMessage(404, "Role [" + roleId + "] not exists. Create it first");
			return new ResponseEntity<RestErrorMessage>(error, HttpStatus.NOT_FOUND);
		}
		
		role.setId(storedRole.getId());

		// Rolle speichern
		roleRepo.save(role);

		// Http Header fuer response vorbereiten
		URL url = new URL(request.getRequestURL().toString());
	    HttpHeaders headers = new HttpHeaders();
	    String hostUri = url.getProtocol() + "://" + url.getHost() + ":" + url.getPort();
	    URI locationUri = URI.create(hostUri + "/api/role/" + role.getId());
	    headers.setLocation(locationUri);

	    // response zurueck geben
	    return new ResponseEntity<Role>(role, headers, HttpStatus.OK);

	}
	
}
