package com.github.oscarmgh.deskflow.exceptions.tickets;

public class CategoryNotFoundException extends RuntimeException {
	public CategoryNotFoundException(String id) {
		super("Category not found");
	}
}