package de.sortimo.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.sortimo.dto.UserGetDto;
import de.sortimo.dto.UserPostDto;
import de.sortimo.model.User;
import de.sortimo.security.JwtUser;
import de.sortimo.services.object.UserObjectService;

@Service
public class UserConverter {
	
	@Autowired
	private UserObjectService userObjectService;

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
