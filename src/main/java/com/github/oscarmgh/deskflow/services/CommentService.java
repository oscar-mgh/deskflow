package com.github.oscarmgh.deskflow.services;

import java.util.List;

import com.github.oscarmgh.deskflow.dtos.ticket.CommentRequest;
import com.github.oscarmgh.deskflow.dtos.ticket.CommentResponse;
import com.github.oscarmgh.deskflow.entities.User;

public interface CommentService {
	CommentResponse addComment(Long ticketId, CommentRequest request, User user);

	List<CommentResponse> getComments(Long ticketId, User user);
}