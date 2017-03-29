package de.sortimo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import de.sortimo.services.database.UserService;

@Component
public class DataLoader implements ApplicationRunner {
	
	@Autowired
	private UserService userService;
	
	@Override
	public void run(ApplicationArguments arg0) throws Exception {
		userService.createAdminUser();
	}

}
