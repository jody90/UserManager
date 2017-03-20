package com.sortimo.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import com.sortimo.converter.UserConverter;
import com.sortimo.model.User;
import com.sortimo.repositories.UserRepository;
import com.sortimo.security.JwtAuthenticationRequest;
import com.sortimo.security.JwtAuthenticationResponse;
import com.sortimo.security.JwtTokenUtil;
import com.sortimo.security.JwtUser;
import com.sortimo.security.MyUserDetailsService;
import com.sortimo.services.RestMessage;

@Controller
public class IndexController {
	
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
    
    @RequestMapping(value = "/api/auth", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtAuthenticationRequest authenticationRequest) throws AuthenticationException {
    	
    	// Benutzername leer
    	if (authenticationRequest.getUsername() == null || authenticationRequest.getUsername().trim().isEmpty()) {
    		RestMessage error = new RestMessage(400, "No Username set");
    		return new ResponseEntity<RestMessage>(error, HttpStatus.BAD_REQUEST);
    	}
    	
    	// Passwort leer
    	if (authenticationRequest.getPassword() == null || authenticationRequest.getPassword().trim().isEmpty()) {
    		RestMessage error = new RestMessage(400, "No Password set");
    		return new ResponseEntity<RestMessage>(error, HttpStatus.BAD_REQUEST);
    	}
    	
    	User user = userRepo.findByUsername(authenticationRequest.getUsername());

    	// Benutzer existiert nicht
    	if (user == null) {
			RestMessage error = new RestMessage(404, "User [" + authenticationRequest.getUsername() + "] not found");
			return new ResponseEntity<RestMessage>(error, HttpStatus.NOT_FOUND);
    	}
    	
    	Authentication authentication = null;
    	try {
	    	authentication = authenticationManager.authenticate(
	                new UsernamePasswordAuthenticationToken(
	                        authenticationRequest.getUsername(),
	                        authenticationRequest.getPassword()
	                )
	        );
    	}
    	catch (BadCredentialsException e) {
			RestMessage error = new RestMessage(403, "User [" + authenticationRequest.getUsername() + "] has bad credentials");
			return new ResponseEntity<RestMessage>(error, HttpStatus.FORBIDDEN);
    	}
        
		SecurityContextHolder.getContext().setAuthentication(authentication);

        // Reload password post-security so we can generate token
        UserDetails userDetails = myUserDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        
        JwtUser jwtUser = userConverter.getJwtUser(user);
        
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
			System.err.println("Fehler beim zusammenbau der redirect URL!");
		}
		catch (IOException e) {
			System.err.println("Beim Redirect lief etwas schief!");
		}
    }	
	
}
