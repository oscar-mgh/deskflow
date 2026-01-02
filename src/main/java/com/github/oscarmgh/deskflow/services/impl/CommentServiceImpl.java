package com.github.oscarmgh.deskflow.services.impl;

import com.github.oscarmgh.deskflow.dtos.ticket.CommentRequest;
import com.github.oscarmgh.deskflow.dtos.ticket.CommentResponse;
import com.github.oscarmgh.deskflow.entities.Comment;
import com.github.oscarmgh.deskflow.entities.Ticket;
import com.github.oscarmgh.deskflow.entities.User;
import com.github.oscarmgh.deskflow.entities.enums.UserRole;
import com.github.oscarmgh.deskflow.exceptions.tickets.ResourceNotFoundException;
import com.github.oscarmgh.deskflow.exceptions.tickets.UnauthorizedTicketAccessException;
import com.github.oscarmgh.deskflow.repositories.CommentRepository;
import com.github.oscarmgh.deskflow.repositories.TicketRepository;
import com.github.oscarmgh.deskflow.services.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

	private final TicketRepository ticketRepository;
	private final CommentRepository commentRepository;

	@Override
	public CommentResponse addComment(Long ticketId, CommentRequest request, User user) {

		Ticket ticket = ticketRepository.findById(ticketId)
				.orElseThrow(() -> new ResourceNotFoundException("Ticket", ticketId));

		boolean isOwner = ticket.getUser().getId().equals(user.getId());
		boolean isAgent = ticket.getAgent() != null &&
				ticket.getAgent().getId().equals(user.getId());
		boolean isAdmin = user.getRole() == UserRole.ADMIN;

		if (!isOwner && !isAgent && !isAdmin) {
			throw new UnauthorizedTicketAccessException();
		}

		Comment comment = new Comment();
		comment.setContent(request.getContent());
		comment.setTicket(ticket);
		comment.setUser(user);

		Comment saved = commentRepository.save(comment);
		return new CommentResponse(saved);
	}

	@Override
	public List<CommentResponse> getComments(Long ticketId, User user) {

		Ticket ticket = ticketRepository.findById(ticketId)
				.orElseThrow(() -> new ResourceNotFoundException("Ticket", ticketId));

		boolean isOwner = ticket.getUser().getId().equals(user.getId());
		boolean isAgent = ticket.getAgent() != null &&
				ticket.getAgent().getId().equals(user.getId());
		boolean isAdmin = user.getRole() == UserRole.ADMIN;

		if (!isOwner && !isAgent && !isAdmin) {
			throw new UnauthorizedTicketAccessException();
		}

		return commentRepository.findByTicketOrderByCreatedAtAsc(ticket)
				.stream()
				.map(CommentResponse::new)
				.toList();
	}
}