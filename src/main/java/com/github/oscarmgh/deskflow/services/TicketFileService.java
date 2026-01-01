package com.github.oscarmgh.deskflow.services;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.github.oscarmgh.deskflow.dtos.ticket.TicketFileResponse;

public interface TicketFileService {
	public TicketFileResponse uploadFile(Long ticketId, MultipartFile file);

	public List<TicketFileResponse> getFiles(Long ticketId);

	void deleteFile(Long ticketId, Long fileId);
}
