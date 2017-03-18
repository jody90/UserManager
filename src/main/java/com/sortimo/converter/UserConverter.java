package com.sortimo.converter;

import org.springframework.stereotype.Service;

import com.sortimo.dto.UserGetDto;
import com.sortimo.dto.UserPostDto;
import com.sortimo.model.User;
import com.sortimo.security.JwtUser;

@Service
public class UserConverter {

	public UserGetDto getUserGetDto(User user) {
		return new UserGetDto(user.getUsername(), user.getFirstname(), user.getLastname(), user.getEmail(), user.getRights(), user.getRoles());
	}
	
	public UserPostDto getUserPostDto(User user) {
		return new UserPostDto(user.getUsername(), user.getFirstname(), user.getLastname(), user.getEmail(), user.getPassword());
	}
	
	public JwtUser getJwtUser(User user) {
		return new JwtUser(user.getUsername(), user.getFirstname(), user.getLastname(), user.getEmail(), user.getPassword(), user.getRights(), user.getRoles());
	}
	
	
}
