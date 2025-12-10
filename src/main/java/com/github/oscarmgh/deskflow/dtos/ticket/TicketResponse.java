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

	private OffsetDateTime createdAt;

	public TicketResponse(Ticket ticket) {
		this.id = ticket.getId();
		this.title = ticket.getTitle();
		this.description = ticket.getDescription();
		this.demo = ticket.getDemo();
		this.status = ticket.getStatus().name();
		this.priority = ticket.getPriority().name();
		this.createdAt = ticket.getCreatedAt();
	}
}
