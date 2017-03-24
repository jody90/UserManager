package com.sortimo.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sortimo.dto.UserGetDto;
import com.sortimo.dto.UserPostDto;
import com.sortimo.model.User;
import com.sortimo.security.JwtUser;
import com.sortimo.services.object.UserObjectService;

@Service
public class UserConverter {
	
	@Autowired
	private UserObjectService userObjectService;

	public UserGetDto getUserGetDto(User user) {
		return new UserGetDto(user.getUsername(), user.getFirstname(), user.getLastname(), user.getEmail(), user.getRights(), user.getRoles(), userObjectService.getAuthorities(user));
	}
	
	public UserPostDto getUserPostDto(User user) {
		return new UserPostDto(user.getUsername(), user.getFirstname(), user.getLastname(), user.getEmail(), user.getPassword());
	}
	
	public JwtUser getJwtUser(User user) {
		return new JwtUser(user.getUsername(), user.getFirstname(), user.getLastname(), user.getEmail(), user.getPassword(), user.getRights(), user.getRoles(), userObjectService.getAuthorities(user));
	}
	
	
}
