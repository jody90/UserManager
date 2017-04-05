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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import de.sortimo.base.rest.RestMessage;
import de.sortimo.rest.converter.RightConverter;
import de.sortimo.rest.dto.SimpleRightDto;
import de.sortimo.service.RightService;
import de.sortimo.service.model.Right;

@RestController
@RequestMapping(value="/api/right")
public class RightController {

	private static final Logger LOGGER = LoggerFactory.getLogger(RightController.class);
	
	@Autowired
	private RightService rightService;
	
	private RightConverter rightConverter = new RightConverter();
	
	/**
	 * Liest alle Rechte aus der Datenbank
	 * 
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, produces="application/json")
	public @ResponseBody ResponseEntity<?> getAllRights() {
		
		Optional<Iterable<Right>> rightsCollection = rightService.findAll();

		// pruefen ob Rechte vorhanden sind
		if (!rightsCollection.isPresent()) {
			LOGGER.info("GET Rights: Es wurden keine Rechte in der Datenbank gefunden.");
			RestMessage message = new RestMessage(HttpStatus.NOT_FOUND, "No Rights found");
			return new ResponseEntity<RestMessage>(message, message.getState());
		}
		
		Set<SimpleRightDto> rights = rightConverter.createDtoList(rightsCollection.get());
	
		// response zurueck geben
		return new ResponseEntity<Set<SimpleRightDto>>(rights, HttpStatus.OK);
		
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
	public @ResponseBody ResponseEntity<?> addRight(@RequestBody Right tRight, HttpServletRequest request) throws MalformedURLException {

		// pruefen ob Recht bereits vorhanden ist
		if (rightService.findByName(tRight.getName()).isPresent()) {
			LOGGER.info("POST Right: Recht {} existiert bereits", tRight.getName());
			RestMessage message = new RestMessage(HttpStatus.CONFLICT, "Right [" + tRight.getName() + "] already defined");
			return new ResponseEntity<RestMessage>(message, message.getState());
		}	
		
		// Recht speichern
		Right savedRight = rightService.save(tRight.getName(), tRight.getDescription());
		
		// Http Header fuer response vorbereiten
		URL url = new URL(request.getRequestURL().toString());
	    HttpHeaders headers = new HttpHeaders();
	    String hostUri = url.getProtocol() + "://" + url.getHost() + ":" + url.getPort();
	    URI locationUri = URI.create(hostUri + "/api/right/" + savedRight.getName());
	    headers.setLocation(locationUri);

	    SimpleRightDto right = rightConverter.createDto(savedRight);
	    
	    // response zurueck geben
	    return new ResponseEntity<SimpleRightDto>(right, headers, HttpStatus.CREATED);

	}
	
	/**
	 * Liest ein Recht aus der Datenbank
	 * 
	 * @param rightId
	 * @return Right Object
	 */
	@RequestMapping(value="/{rightName}", method = RequestMethod.GET, produces="application/json")
	public @ResponseBody ResponseEntity<?> getRight(@PathVariable String rightName) {

		Optional<Right> tRight = rightService.findByName(rightName);

		// pruefen ob Recht vorhanden ist
		if (!tRight.isPresent()) {
			LOGGER.info("GET Right: Recht {} existiert nicht", rightName);
			RestMessage message = new RestMessage(HttpStatus.NOT_FOUND, "Right [" + rightName + "] not found");
			return new ResponseEntity<RestMessage>(message, message.getState());
		}

		SimpleRightDto right = rightConverter.createDto(tRight.get());
		
		// response zurueck geben
		return new ResponseEntity<SimpleRightDto>(right, HttpStatus.OK);
	}

	/**
	 * Loescht ein Recht aus der Datenbank
	 * 
	 * @param rightId
	 * @return
	 */
	@RequestMapping(value="/{rightName}", method = RequestMethod.DELETE, produces="application/json")
	public @ResponseBody ResponseEntity<?> deleteRight(@PathVariable String rightName) {

		Optional<Right> tRight = rightService.findByName(rightName);

		// pruefen ob Recht vorhanden ist
		if (!tRight.isPresent()) {
			LOGGER.info("DELETE Right: Recht {} existiert nicht", rightName);
			RestMessage message = new RestMessage(HttpStatus.NOT_FOUND, "Right [" + rightName + "] not found! Cannot delete");
			return new ResponseEntity<RestMessage>(message, message.getState());
		}

		rightService.delete(tRight.get());

		RestMessage message = new RestMessage(HttpStatus.OK, "Right [" + rightName + "] successfully deleted");

		// response zurueck geben
		return new ResponseEntity<RestMessage>(message , HttpStatus.OK);
	}

	/**
	 * Dated ein Recht in der Datenbank ab
	 * 
	 * @param tRight
	 * @param rightName
	 * @param request
	 * @return
	 * @throws MalformedURLException
	 */
	@RequestMapping(value="/{rightName}", method = RequestMethod.PUT, consumes="application/json", produces="application/json")
	public @ResponseBody ResponseEntity<?> updateRight(@RequestBody Right tRight, @PathVariable String rightName,  HttpServletRequest request) throws MalformedURLException {
		
		// Recht updaten
		Optional<Right> right = rightService.update(rightName, tRight);
		
		if (!right.isPresent()) {
			LOGGER.info("PUT Right: Recht {} konnte nicht upgedated werden.", rightName);
			RestMessage message = new RestMessage(HttpStatus.CONFLICT, "Right [" + rightName + "] update not possible");
			return new ResponseEntity<RestMessage>(message, message.getState());
		}

		// Http Header fuer response vorbereiten
		URL url = new URL(request.getRequestURL().toString());
	    HttpHeaders headers = new HttpHeaders();
	    String hostUri = url.getProtocol() + "://" + url.getHost() + ":" + url.getPort();
	    URI locationUri = URI.create(hostUri + "/api/right/" + right.get().getName());
	    headers.setLocation(locationUri);
	    
	    SimpleRightDto simpleRightDto = rightConverter.createDto(right.get());

	    // response zurueck geben
	    return new ResponseEntity<SimpleRightDto>(simpleRightDto, headers, HttpStatus.OK);

	}

}
