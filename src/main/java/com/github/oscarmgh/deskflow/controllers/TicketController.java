package com.github.oscarmgh.deskflow.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.github.oscarmgh.deskflow.dtos.ticket.TicketFileResponse;
import com.github.oscarmgh.deskflow.dtos.ticket.TicketRequest;
import com.github.oscarmgh.deskflow.dtos.ticket.TicketResponse;
import com.github.oscarmgh.deskflow.entities.User;
import com.github.oscarmgh.deskflow.services.TicketService;
import com.github.oscarmgh.deskflow.services.impl.TicketFileServiceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Slf4j
public class TicketController {

	private final TicketService ticketService;
	private final TicketFileServiceImpl fileService;

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

	@PostMapping("/tickets/{id}/files")
	public ResponseEntity<TicketFileResponse> uploadFile(
			@PathVariable Long id,
			@RequestParam MultipartFile file) {
		TicketFileResponse response = fileService.uploadFile(id, file);
		return ResponseEntity.ok(response);
	}

	@DeleteMapping("/tickets/{id}/files/{fileId}")
	public ResponseEntity<Void> deleteFile(
			@PathVariable Long id,
			@PathVariable Long fileId) {
		fileService.deleteFile(id, fileId);
		return ResponseEntity.noContent().build();
	}
}
