package com.github.oscarmgh.deskflow.exceptions.auth;

public class UserNotFoundException extends RuntimeException {
	public UserNotFoundException() {
		super("User could not be found");
	}
}
