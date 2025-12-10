package com.github.oscarmgh.deskflow.exceptions.tickets;

public class UnauthorizedTicketAccessException extends RuntimeException {
    public UnauthorizedTicketAccessException() {
        super("Unauthorized access to the ticket");
    }
}
