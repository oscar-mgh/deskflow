package com.github.oscarmgh.deskflow.exceptions.tickets;

public class TicketNotFoundException extends RuntimeException {
	public TicketNotFoundException(String id) {
		super("Ticket with id: " + id + " not found");
	}
}
