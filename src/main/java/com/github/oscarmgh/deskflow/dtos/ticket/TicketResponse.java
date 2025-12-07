package com.github.oscarmgh.deskflow.dtos.ticket;

import java.time.LocalDateTime;

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

	private String createdBy;
	private String createdByRole;

	private LocalDateTime createdAt;
}
