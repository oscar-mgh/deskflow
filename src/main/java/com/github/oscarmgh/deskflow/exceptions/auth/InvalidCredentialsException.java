package com.github.oscarmgh.deskflow.exceptions.auth;

public class InvalidCredentialsException extends RuntimeException {
	public InvalidCredentialsException() {
		super("Invalid credentials");
	}
}
