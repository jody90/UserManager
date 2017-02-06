package com.sortimo.services;

public class RestMessage {
	
	private int code;
	
	private String message;
	
	public RestMessage(int code, String message) {
		this.code = code;
		this.message = message;
	}
	
	public int getCode() {
		return code;
	}
	
	public String getMessage() {
		return message;
	}
	
}
