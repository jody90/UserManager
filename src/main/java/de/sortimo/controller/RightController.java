package de.sortimo.controller;

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

import de.sortimo.model.Right;
import de.sortimo.repositories.RightRepository;
import de.sortimo.services.RestMessage;

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
			RestMessage error = new RestMessage(404, "No Rights found");
			return new ResponseEntity<RestMessage>(error, HttpStatus.NOT_FOUND);
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
			RestMessage error = new RestMessage(404, "Right [" + right.getName() + "] already defined");
			return new ResponseEntity<RestMessage>(error, HttpStatus.CONFLICT);
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
	@RequestMapping(value="/{rightName}", method = RequestMethod.GET, produces="application/json")
	public @ResponseBody ResponseEntity<?> getRight(@PathVariable String rightName) {

		Right right = rightRepo.findByName(rightName);

		// pruefen ob benutzer vorhanden ist
		if (right == null) {
			RestMessage error = new RestMessage(404, "Right [" + rightName + "] not found");
			return new ResponseEntity<RestMessage>(error, HttpStatus.NOT_FOUND);
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
	@RequestMapping(value="/{rightName}", method = RequestMethod.DELETE, produces="application/json")
	public @ResponseBody ResponseEntity<?> deleteRight(@PathVariable String rightName) {

		Right right = rightRepo.findByName(rightName);

		// pruefen ob Rolle vorhanden ist
		if (right == null) {
			RestMessage error = new RestMessage(404, "Right [" + rightName + "] not found! Cannot delete");
			return new ResponseEntity<RestMessage>(error, HttpStatus.NOT_FOUND);
		}

		rightRepo.delete(right);

		RestMessage message = new RestMessage(200, "Right [" + rightName + "] successfully deleted");

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
	@RequestMapping(value="/{rightName}", method = RequestMethod.PUT, consumes="application/json", produces="application/json")
	public @ResponseBody ResponseEntity<?> updateRight(@RequestBody Right right, @PathVariable String rightName,  HttpServletRequest request) throws MalformedURLException {

		Right storedRight = rightRepo.findByName(rightName);
		
		// pruefen ob Rolle bereits vorhanden ist
		if (storedRight == null) {
			RestMessage error = new RestMessage(404, "Right [" + rightName + "] not exists. Create it first");
			return new ResponseEntity<RestMessage>(error, HttpStatus.NOT_FOUND);
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
