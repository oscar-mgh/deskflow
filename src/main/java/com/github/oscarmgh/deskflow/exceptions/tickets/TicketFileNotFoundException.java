package com.github.oscarmgh.deskflow.exceptions.tickets;

public class TicketFileNotFoundException extends RuntimeException {
	public TicketFileNotFoundException() {
		super("File not found for the specified ticket");
	}
}