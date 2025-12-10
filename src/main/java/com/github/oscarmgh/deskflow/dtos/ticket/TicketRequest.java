package com.github.oscarmgh.deskflow.dtos.ticket;

import com.github.oscarmgh.deskflow.entities.enums.TicketPriority;

import lombok.Data;

@Data
public class TicketRequest {

	private String title;
	private String description;
	private TicketPriority priority;
}
