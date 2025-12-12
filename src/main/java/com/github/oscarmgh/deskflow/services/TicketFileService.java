package com.github.oscarmgh.deskflow.services;

import org.springframework.web.multipart.MultipartFile;

import com.github.oscarmgh.deskflow.dtos.ticket.TicketFileResponse;

public interface TicketFileService {
	public TicketFileResponse uploadFile(Long ticketId, MultipartFile file);

	void deleteFile(Long ticketId, Long fileId);
}
