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

import com.sortimo.model.Right;
import com.sortimo.repositories.RightRepository;
import com.sortimo.services.RestErrorMessage;
import com.sortimo.services.RestMessage;

@RestController
@RequestMapping(value="/api/right")
public class RightController {

//	Pageable pageable;
	
	@Autowired
	private RightRepository rightRepo;
	
	/**
	 * Liest alle Rechte aus der Datenbank
	 * 
	 * @return Collection von Right Objekten
	 */
	@RequestMapping(method = RequestMethod.GET, produces="application/json")
	public @ResponseBody ResponseEntity<?> getAllRights() {
		
		Iterable<Right> rightsCollection = rightRepo.findAll();

		// pruefen ob benutzer vorhanden ist
		if (rightsCollection == null) {
			RestErrorMessage error = new RestErrorMessage(404, "No Rights found");
			return new ResponseEntity<RestErrorMessage>(error, HttpStatus.NOT_FOUND);
		}

		// response zurueck geben
		return new ResponseEntity<Iterable<Right>>(rightsCollection, HttpStatus.OK);
		
	}

	/**
	 * Fuegt ein neues Recht in die Datenbank ein
	 * 
	 * @param right
	 * @param request
	 * @return
	 * @throws MalformedURLException
	 */
	@RequestMapping(method = RequestMethod.POST, consumes="application/json", produces="application/json")
	public @ResponseBody ResponseEntity<?> addRight(@RequestBody Right right, HttpServletRequest request) throws MalformedURLException {

		// pruefen ob Rolle bereits vorhanden ist
		if (rightRepo.findByName(right.getName()) != null) {
			RestErrorMessage error = new RestErrorMessage(404, "Right [" + right.getName() + "] already defined");
			return new ResponseEntity<RestErrorMessage>(error, HttpStatus.CONFLICT);
		}	
		
		// Benutzer speichern
		rightRepo.save(right);

		// Http Header fuer response vorbereiten
		URL url = new URL(request.getRequestURL().toString());
	    HttpHeaders headers = new HttpHeaders();
	    String hostUri = url.getProtocol() + "://" + url.getHost() + ":" + url.getPort();
	    URI locationUri = URI.create(hostUri + "/api/right/" + right.getName());
	    headers.setLocation(locationUri);

	    // response zurueck geben
	    return new ResponseEntity<Right>(right, headers, HttpStatus.CREATED);

	}
	
	/**
	 * Liest ein Recht aus der Datenbank
	 * 
	 * @param rightId
	 * @return Right Object
	 */
	@RequestMapping(value="/{rightId}", method = RequestMethod.GET, produces="application/json")
	public @ResponseBody ResponseEntity<?> getRight(@PathVariable Long rightId) {

		Right right = rightRepo.findOne(rightId);

		// pruefen ob benutzer vorhanden ist
		if (right == null) {
			RestErrorMessage error = new RestErrorMessage(404, "Right [" + rightId + "] not found");
			return new ResponseEntity<RestErrorMessage>(error, HttpStatus.NOT_FOUND);
		}

		// response zurueck geben
		return new ResponseEntity<Right>(right, HttpStatus.OK);
	}

	/**
	 * Loescht ein Recht aus der Datenbank
	 * 
	 * @param rightId
	 * @return
	 */
	@RequestMapping(value="/{rightId}", method = RequestMethod.DELETE, produces="application/json")
	public @ResponseBody ResponseEntity<?> deleteRight(@PathVariable Long rightId) {

		Right right = rightRepo.findOne(rightId);

		// pruefen ob Rolle vorhanden ist
		if (right == null) {
			RestErrorMessage error = new RestErrorMessage(404, "Right [" + rightId + "] not found! Cannot delete");
			return new ResponseEntity<RestErrorMessage>(error, HttpStatus.NOT_FOUND);
		}

		rightRepo.delete(right);

		RestMessage message = new RestMessage(200, "Right [" + rightId + "] successfully deleted");

		// response zurueck geben
		return new ResponseEntity<RestMessage>(message , HttpStatus.OK);
	}

	/**
	 * Dated ein Recht in der Datenbank ab
	 * 
	 * @param right
	 * @param rightId
	 * @param request
	 * @return
	 * @throws MalformedURLException
	 */
	@RequestMapping(value="/{rightname}", method = RequestMethod.PUT, consumes="application/json", produces="application/json")
	public @ResponseBody ResponseEntity<?> updateRight(@RequestBody Right right, @PathVariable Long rightId,  HttpServletRequest request) throws MalformedURLException {

		Right storedRight = rightRepo.findOne(rightId);
		
		// pruefen ob Rolle bereits vorhanden ist
		if (storedRight == null) {
			RestErrorMessage error = new RestErrorMessage(404, "Right [" + rightId + "] not exists. Create it first");
			return new ResponseEntity<RestErrorMessage>(error, HttpStatus.NOT_FOUND);
		}
		
		right.setId(storedRight.getId());

		// Rolle speichern
		rightRepo.save(right);

		// Http Header fuer response vorbereiten
		URL url = new URL(request.getRequestURL().toString());
	    HttpHeaders headers = new HttpHeaders();
	    String hostUri = url.getProtocol() + "://" + url.getHost() + ":" + url.getPort();
	    URI locationUri = URI.create(hostUri + "/api/right/" + right.getName());
	    headers.setLocation(locationUri);

	    // response zurueck geben
	    return new ResponseEntity<Right>(right, headers, HttpStatus.OK);

	}

}
