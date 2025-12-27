package com.github.oscarmgh.deskflow.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;
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

import com.github.oscarmgh.deskflow.dtos.PageResponse;
import com.github.oscarmgh.deskflow.dtos.ticket.AssignAgentRequest;
import com.github.oscarmgh.deskflow.dtos.ticket.CommentRequest;
import com.github.oscarmgh.deskflow.dtos.ticket.CommentResponse;
import com.github.oscarmgh.deskflow.dtos.ticket.TicketFileResponse;
import com.github.oscarmgh.deskflow.dtos.ticket.TicketRequest;
import com.github.oscarmgh.deskflow.dtos.ticket.TicketResponse;
import com.github.oscarmgh.deskflow.entities.User;
import com.github.oscarmgh.deskflow.entities.enums.UserRole;
import com.github.oscarmgh.deskflow.repositories.UserRepository;
import com.github.oscarmgh.deskflow.services.CommentService;
import com.github.oscarmgh.deskflow.services.TicketFileService;
import com.github.oscarmgh.deskflow.services.TicketService;
import com.github.oscarmgh.deskflow.services.TokenService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class TicketController {

	private final TicketService ticketService;
	private final TicketFileService fileService;
	private final CommentService commentService;
	private final UserRepository userRepository;
	private final TokenService tokenService;

	@GetMapping("/tickets/{id}")
	public TicketResponse userTicket(@PathVariable Long id, Authentication authentication) {
		User user = (User) authentication.getPrincipal();
		return ticketService.getUserTicket(id, user);
	}

	@GetMapping("/tickets")
	public PageResponse<TicketResponse> myTickets(Authentication authentication, Pageable pageable) {
		User user = (User) authentication.getPrincipal();
		return ticketService.getUserTickets(user, pageable);
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

	@PatchMapping("/admin/tickets/{id}/assign")
	public TicketResponse assignAgent(
			@PathVariable Long id,
			@RequestBody AssignAgentRequest request,
			Authentication authentication) {
		User admin = (User) authentication.getPrincipal();
		return ticketService.assignAgent(id, request.getAgentId(), admin);
	}

	@PostMapping("/tickets/{id}/comments")
	public CommentResponse addComment(
			@PathVariable Long id,
			@RequestBody CommentRequest request,
			Authentication authentication) {
		User user = (User) authentication.getPrincipal();
		return commentService.addComment(id, request, user);
	}

	@GetMapping("/tickets/{id}/comments")
	public List<CommentResponse> getComments(
			@PathVariable Long id,
			Authentication authentication) {
		User user = (User) authentication.getPrincipal();
		return commentService.getComments(id, user);
	}

	@PostMapping("/upgrade")
	public ResponseEntity<Map<String, String>> upgrade(Authentication authentication) {
		String email = authentication.getName();

		userRepository.updateRole(email, UserRole.PREMIUM);

		User updated = userRepository.findByEmail(email).get();

		String newToken = tokenService.generateToken(updated);

		return ResponseEntity.ok(Map.of("token", newToken));
	}
}
