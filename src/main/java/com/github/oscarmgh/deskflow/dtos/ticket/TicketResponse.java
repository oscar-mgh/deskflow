package com.github.oscarmgh.deskflow.dtos.ticket;

import java.time.OffsetDateTime;

import com.github.oscarmgh.deskflow.entities.Ticket;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TicketResponse {

	private Long id;
	private String title;
	private String code;
	private String description;
	private String status;
	private String priority;
	private Long agentId;
	private Long userId;
	private String categoryName;

	private OffsetDateTime createdAt;

	public TicketResponse(Ticket ticket) {
		this.id = ticket.getId();
		this.title = ticket.getTitle();
		this.code = ticket.getCode();
		this.description = ticket.getDescription();
		this.status = ticket.getStatus().name();
		this.priority = ticket.getPriority().name();
		this.createdAt = ticket.getCreatedAt();

		this.agentId = ticket.getAgent() != null
				? ticket.getAgent().getId()
				: null;

		this.userId = ticket.getUser() != null
				? ticket.getUser().getId()
				: null;

		this.categoryName = ticket.getCategory() != null
				? ticket.getCategory().getName()
				: null;
	}
}