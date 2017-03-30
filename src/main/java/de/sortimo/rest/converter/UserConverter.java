package de.sortimo.rest.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.sortimo.rest.dto.UserGetDto;
import de.sortimo.rest.dto.UserPostDto;
import de.sortimo.service.UserSpringSecurityService;
import de.sortimo.service.model.User;
import de.sortimo.service.security.JwtUser;

@Service
public class UserConverter {
	
	@Autowired
	private UserSpringSecurityService userObjectService;

	public UserGetDto getUserGetDto(User user) {
		return new UserGetDto(user.getUsername(), user.getFirstname(), user.getLastname(), user.getEmail(), user.getRights(), user.getRoles(), userObjectService.getAuthorities(user));
	}
	
	public UserPostDto getUserPostDto(User user) {
		return new UserPostDto(user.getUsername(), user.getFirstname(), user.getLastname(), user.getEmail(), user.getPassword(), user.getRights(), user.getRoles());
	}
	
	public JwtUser getJwtUser(User user) {
		return new JwtUser(user.getUsername(), user.getFirstname(), user.getLastname(), user.getEmail(), user.getPassword(), user.getRights(), user.getRoles(), userObjectService.getAuthorities(user));
	}
	
	
}
