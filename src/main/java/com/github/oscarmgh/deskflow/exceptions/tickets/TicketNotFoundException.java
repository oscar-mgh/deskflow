package com.github.oscarmgh.deskflow.exceptions.tickets;

public class TicketNotFoundException extends RuntimeException {
	public TicketNotFoundException() {
		super("Ticket not found");
	}
}
