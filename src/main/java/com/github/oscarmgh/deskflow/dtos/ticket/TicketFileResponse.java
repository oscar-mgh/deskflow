package com.github.oscarmgh.deskflow.dtos.ticket;

import java.time.OffsetDateTime;

public record TicketFileResponse(
		Long id,
		String fileName,
		String mimeType,
		String url,
		OffsetDateTime uploadedAt) {
}
