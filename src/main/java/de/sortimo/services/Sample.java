package de.sortimo.services;

import org.springframework.stereotype.Component;

@Component
public class Sample {

	private String name;

	public String getName() {
		return name;
	}

	@Timelog
	public void setName(String name) {
		this.name = name;
	}
	
}
