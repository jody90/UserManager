package com.sortimo.converter;

import com.sortimo.dto.UserDto;
import com.sortimo.model.User;

public class UserConverter {

	public UserDto getUserDto(User user) {
		return new UserDto(user.getUsername(), user.getFirstname(), user.getLastname(), user.getEmail(), user.getRights(), user.getRoles());
	}
	
}
