package com.github.oscarmgh.deskflow.dtos.ticket;

import com.github.oscarmgh.deskflow.entities.enums.TicketFile;

public class TicketFileMapper {

	public static TicketFileResponse toResponse(TicketFile file) {
		return new TicketFileResponse(
				file.getId(),
				file.getFileName(),
				file.getMimeType(),
				file.getFileUrl(),
				file.getUploadedAt());
	}
}