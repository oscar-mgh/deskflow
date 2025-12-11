package com.github.oscarmgh.deskflow.dtos.ticket;

import com.github.oscarmgh.deskflow.entities.TicketCategory;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TicketCategoryResponse {
	private Long id;
	private String name;

	public TicketCategoryResponse(TicketCategory category) {
		this.id = category.getId();
		this.name = category.getName();
	}
}
