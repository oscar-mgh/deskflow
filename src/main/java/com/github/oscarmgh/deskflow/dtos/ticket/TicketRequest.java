package com.github.oscarmgh.deskflow.dtos.ticket;


import com.github.oscarmgh.deskflow.entities.enums.TicketPriority;
import com.github.oscarmgh.deskflow.entities.enums.TicketStatus;

import lombok.Data;

@Data
public class TicketRequest {
	private String title;
	private String description;
	private TicketPriority priority;
	private String categoryName;
	private TicketStatus status;
}
