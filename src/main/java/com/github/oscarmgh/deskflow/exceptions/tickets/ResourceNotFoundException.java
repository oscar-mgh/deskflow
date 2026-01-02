package com.github.oscarmgh.deskflow.exceptions.tickets;

public class ResourceNotFoundException extends RuntimeException {
	public ResourceNotFoundException(String resourceName, Object id) {
		super(String.format("%s con ID [%s] no fue encontrado", resourceName, id));
	}
}
