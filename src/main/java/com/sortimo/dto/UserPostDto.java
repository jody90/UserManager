package com.sortimo.dto;

public class UserPostDto extends SimpleUserDto {
	
	private String password;

	public UserPostDto() {
		super();
	}

	public UserPostDto(String username, String firstname, String lastname, String email, String password) {
		super(username, firstname, lastname, email);
		this.setPassword(password);
	}
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "UserPostDto [password=" + password + ", toString()=" + super.toString() + "]";
	}

}
