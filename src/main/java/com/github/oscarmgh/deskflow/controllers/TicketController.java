package com.github.oscarmgh.deskflow.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.oscarmgh.deskflow.dtos.ticket.TicketRequest;
import com.github.oscarmgh.deskflow.dtos.ticket.TicketResponse;
import com.github.oscarmgh.deskflow.entities.User;
import com.github.oscarmgh.deskflow.services.TicketService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class TicketController {

	private final TicketService ticketService;

	@GetMapping("/public/tickets")
	public List<TicketResponse> demoTickets() {
		return ticketService.getDemoTickets();
	}

	@GetMapping("/public/tickets/{id}")
	public TicketResponse demoTicket(@PathVariable Long id) {
		return ticketService.getDemoTicket(id);
	}

	@GetMapping("/tickets")
	public List<TicketResponse> myTickets(HttpServletRequest request) {
		User user = (User) request.getAttribute("authenticatedUser");
		return ticketService.getUserTickets(user);
	}

	@PostMapping("/tickets")
	public TicketResponse create(
			@RequestBody TicketRequest request,
			HttpServletRequest servletRequest) {
		User user = (User) servletRequest.getAttribute("authenticatedUser");
		return ticketService.createTicket(request, user);
	}
}
