package com.sortimo.converter;

import com.sortimo.dto.UserGetDto;
import com.sortimo.model.User;

public class UserConverter {

	public UserGetDto getUserDto(User user) {
		return new UserGetDto(user.getUsername(), user.getFirstname(), user.getLastname(), user.getEmail(), user.getRights(), user.getRoles());
	}
	
}
