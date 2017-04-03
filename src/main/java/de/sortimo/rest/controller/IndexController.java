package de.sortimo.rest.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import de.sortimo.base.rest.RestMessage;
import de.sortimo.rest.converter.UserConverter;
import de.sortimo.rest.dto.JwtUserDto;
import de.sortimo.service.model.User;
import de.sortimo.service.repositories.UserRepository;
import de.sortimo.service.security.JwtAuthenticationRequest;
import de.sortimo.service.security.JwtAuthenticationResponse;
import de.sortimo.service.security.JwtTokenUtil;
import de.sortimo.service.security.MyUserDetailsService;

@Controller
public class IndexController {
	
	private static final Logger LOGGER  = LoggerFactory.getLogger(IndexController.class);
	
	@Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private MyUserDetailsService myUserDetailsService;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private UserConverter userConverter;
	
    
	/**
	 * Verarbeitet den Login Request
	 * 
	 * @param authenticationRequest
	 * @return
	 * @throws AuthenticationException
	 */
    @RequestMapping(value = "/api/auth", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtAuthenticationRequest authenticationRequest) throws AuthenticationException {
    	
    	// Benutzername leer
    	if (authenticationRequest.getUsername() == null || authenticationRequest.getUsername().trim().isEmpty()) {
    		LOGGER.info("Login Versuch ohne Benutzername.");
    		RestMessage message = new RestMessage(HttpStatus.BAD_REQUEST, "No Username set");
    		return new ResponseEntity<RestMessage>(message, message.getState());
    	}
    	
    	// Passwort leer
    	if (authenticationRequest.getPassword() == null || authenticationRequest.getPassword().trim().isEmpty()) {
    		LOGGER.info("Login Versuch ohne Passwort.");
    		RestMessage message = new RestMessage(HttpStatus.BAD_REQUEST, "No Password set");
    		return new ResponseEntity<RestMessage>(message, message.getState());
    	}
    	
    	User user = userRepo.findByUsername(authenticationRequest.getUsername()).get();

    	// Benutzer existiert nicht
    	if (user == null) {
    		LOGGER.info("Login Versuch mit nicht existierendem Nutzer. Versuchter Benutzer: [{}]", authenticationRequest.getUsername());
			RestMessage message = new RestMessage(HttpStatus.NOT_FOUND, "User [" + authenticationRequest.getUsername() + "] not found");
			return new ResponseEntity<RestMessage>(message, message.getState());
    	}
    	
    	// Authentication check
    	Authentication authentication = null;
    	
    	try {
	    	authentication = authenticationManager.authenticate(
	    		new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
	        );
    	}
    	catch (BadCredentialsException e) {
    		LOGGER.info("Login nicht erfolgreich. Passwort oder Benutzername falsch. Versuchter Benutzer: [{}]", authenticationRequest.getUsername());
			RestMessage message = new RestMessage(HttpStatus.FORBIDDEN, "User [" + authenticationRequest.getUsername() + "] has bad credentials");
			return new ResponseEntity<RestMessage>(message, message.getState());
    	}
        
		SecurityContextHolder.getContext().setAuthentication(authentication);

        // Reload password post-security so we can generate token
        UserDetails userDetails = myUserDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        
        JwtUserDto jwtUser = userConverter.getJwtUser(user);
        
        String token = jwtTokenUtil.generateToken(userDetails, jwtUser);

        // Return the token
        return ResponseEntity.ok(new JwtAuthenticationResponse(token));
    }
	
	/**
	 * Leitet einen Request von Application Root auf Swagger-UI um
	 * 
	 * @param response
	 * @param request
	 */
	@RequestMapping(value={"/", "/api"})
	public void method(HttpServletResponse response, HttpServletRequest request) {
		
		// Http Header fuer response vorbereiten
		try {
			URL url = new URL(request.getRequestURL().toString());
			String hostUrl = url.getProtocol() + "://" + url.getHost() + ":" + url.getPort() + "/swagger-ui.html";
			response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
			response.sendRedirect(hostUrl);
		} catch (MalformedURLException e) {
			LOGGER.error("Fehler beim zusammenbau der Redirect-URL zu Swagger-UI.");			
		}
		catch (IOException e) {
			LOGGER.error("Fehler beim Redirect zu Swagger-UI.");			
		}
    }	
	
}
