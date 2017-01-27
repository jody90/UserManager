package com.sortimo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.sortimo.dataObjects.User;
import com.sortimo.repositories.UserRepository;

@Controller
@RequestMapping(value="/api")
public class ApiController {
	
	@Autowired
	UserRepository userRepo;
	
	User user = new User("test", "test", "test", "test", "test@test.de");

	@RequestMapping(value="/register", method = RequestMethod.POST)
	public String printHello(ModelMap model) {
		userRepo.saveUser(user);
		return "hello";
	}

}
