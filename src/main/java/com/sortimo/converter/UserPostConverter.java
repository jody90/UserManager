package com.sortimo.converter;

import com.sortimo.dto.UserPostDto;
import com.sortimo.model.User;

public class UserPostConverter {

	public UserPostDto getUserPostDto(User user) {
		return new UserPostDto(user.getUsername(), user.getFirstname(), user.getLastname(), user.getEmail(), user.getPassword());
	}
	
}
