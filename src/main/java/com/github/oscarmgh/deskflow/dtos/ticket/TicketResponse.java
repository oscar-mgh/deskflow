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
	private String description;
	private String status;
	private String priority;
	private Boolean demo;
	private String createdBy;
	private String createdByRole;

	private OffsetDateTime createdAt;

	public TicketResponse(Ticket ticket) {
		this.id = ticket.getId();
		this.title = ticket.getTitle();
		this.description = ticket.getDescription();
		this.demo = ticket.getDemo();
		this.status = ticket.getStatus().name();
		this.priority = ticket.getPriority().name();
		if (ticket.getUser() != null) {
			this.createdBy = ticket.getUser().getFullName();
			this.createdByRole = ticket.getUser().getRole().name();
		} else {
			this.createdBy = "Anonymous";
			this.createdByRole = "N/A";
		}
		this.createdAt = ticket.getCreatedAt();
	}
}
