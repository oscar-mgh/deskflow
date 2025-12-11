package com.github.oscarmgh.deskflow.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.oscarmgh.deskflow.dtos.ticket.TicketRequest;
import com.github.oscarmgh.deskflow.dtos.ticket.TicketResponse;
import com.github.oscarmgh.deskflow.entities.User;
import com.github.oscarmgh.deskflow.entities.enums.TicketStatus;
import com.github.oscarmgh.deskflow.services.TicketService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Slf4j
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

	@GetMapping("/tickets/{id}")
	public TicketResponse userTicket(@PathVariable Long id, Authentication authentication) {
		User user = (User) authentication.getPrincipal();
		return ticketService.getUserTicket(id, user);
	}

	@GetMapping("/tickets")
	public List<TicketResponse> myTickets(Authentication authentication) {
		User user = (User) authentication.getPrincipal();
		return ticketService.getUserTickets(user);
	}

	@PostMapping("/tickets")
	public TicketResponse create(
			@RequestBody TicketRequest request,
			Authentication authentication) {
		User user = (User) authentication.getPrincipal();
		return ticketService.createTicket(request, user);
	}

	@PatchMapping("/tickets/{id}")
	public TicketResponse updateTicket(
			@PathVariable Long id,
			@RequestBody TicketRequest request,
			Authentication authentication) {
		User user = (User) authentication.getPrincipal();
		return ticketService.updateTicket(id, request, user);
	}

	@DeleteMapping("/tickets/{id}")
	public ResponseEntity<Void> deleteTicket(@PathVariable Long id, Authentication authentication) {
		User user = (User) authentication.getPrincipal();
		ticketService.deleteTicket(id, user);
		return ResponseEntity.noContent().build();
	}

}
