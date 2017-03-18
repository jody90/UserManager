package com.sortimo.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
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
    
//  @RequestMapping(value = "${jwt.route.authentication.path}", method = RequestMethod.POST)
//  public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtAuthenticationRequest authenticationRequest, Device device) throws AuthenticationException {
//
//      // Perform the security
//      final Authentication authentication = authenticationManager.authenticate(
//              new UsernamePasswordAuthenticationToken(
//                      authenticationRequest.getUsername(),
//                      authenticationRequest.getPassword()
//              )
//      );
//      SecurityContextHolder.getContext().setAuthentication(authentication);
//
//      // Reload password post-security so we can generate token
//      final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
//      final String token = jwtTokenUtil.generateToken(userDetails, device);
//
//      // Return the token
//      return ResponseEntity.ok(new JwtAuthenticationResponse(token));
//  }
	
    @RequestMapping(value = "/api/auth", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtAuthenticationRequest authenticationRequest) throws AuthenticationException {

        // Perform the security
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getUsername(),
                        authenticationRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Reload password post-security so we can generate token
        UserDetails userDetails = myUserDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        
        User user = userRepo.findByUsername(authenticationRequest.getUsername());
        
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
