package com.github.oscarmgh.deskflow.exceptions.auth;

public class EmailExistsException extends RuntimeException {
	public EmailExistsException() {
		super("Email already exists");
	}
}
