package com.github.oscarmgh.deskflow.dtos.ticket;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TicketCategoryRequest {
	@NotBlank
	@Size(min = 2, max = 30)
	private String name;
}
