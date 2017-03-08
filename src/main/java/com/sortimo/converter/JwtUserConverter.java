package com.sortimo.converter;

import com.sortimo.model.User;
import com.sortimo.security.JwtUser;

public class JwtUserConverter {

	public JwtUser getJwtUser(User user) {
		return new JwtUser(user.getUsername(), user.getFirstname(), user.getLastname(), user.getEmail(), user.getPassword(), user.getRights(), user.getRoles());
	}
	
}
