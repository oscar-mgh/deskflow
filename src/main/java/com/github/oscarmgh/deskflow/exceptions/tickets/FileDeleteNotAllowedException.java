package com.github.oscarmgh.deskflow.exceptions.tickets;

public class FileDeleteNotAllowedException extends RuntimeException {
	public FileDeleteNotAllowedException() {
		super("Only PREMIUM users can delete files");
	}
}