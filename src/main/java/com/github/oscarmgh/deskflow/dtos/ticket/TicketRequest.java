package com.github.oscarmgh.deskflow.dtos.ticket;

import lombok.Data;

@Data
public class TicketRequest {

	private String title;
	private String description;
	private String priority;
}
