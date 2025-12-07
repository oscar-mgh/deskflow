package com.github.oscarmgh.deskflow.exceptions.auth;

public class InactiveUserException extends RuntimeException {
	public InactiveUserException() {
		super("Inactive user");
	}
}
