package com.github.oscarmgh.deskflow.dtos.ticket;

import com.github.oscarmgh.deskflow.entities.Comment;
import lombok.Getter;

import java.time.OffsetDateTime;

@Getter
public class CommentResponse {

	private final Long id;
	private final String content;
	private final Long userId;
	private final OffsetDateTime createdAt;

	public CommentResponse(Comment comment) {
		this.id = comment.getId();
		this.content = comment.getContent();
		this.userId = comment.getUser().getId();
		this.createdAt = comment.getCreatedAt();
	}
}