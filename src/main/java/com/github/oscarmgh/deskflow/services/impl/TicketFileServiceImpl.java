package com.github.oscarmgh.deskflow.services.impl;

import java.util.Map;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.github.oscarmgh.deskflow.dtos.ticket.TicketFileMapper;
import com.github.oscarmgh.deskflow.dtos.ticket.TicketFileResponse;
import com.github.oscarmgh.deskflow.entities.Ticket;
import com.github.oscarmgh.deskflow.entities.User;
import com.github.oscarmgh.deskflow.entities.enums.TicketFile;
import com.github.oscarmgh.deskflow.entities.enums.UserRole;
import com.github.oscarmgh.deskflow.exceptions.tickets.FileUploadNotAllowedException;
import com.github.oscarmgh.deskflow.exceptions.tickets.TicketNotFoundException;
import com.github.oscarmgh.deskflow.repositories.TicketFileRepository;
import com.github.oscarmgh.deskflow.repositories.TicketRepository;
import com.github.oscarmgh.deskflow.services.CloudinaryService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TicketFileServiceImpl {

	private final TicketRepository ticketRepository;
	private final TicketFileRepository ticketFileRepository;
	private final CloudinaryService cloudinaryService;

	public TicketFileResponse uploadFile(Long ticketId, MultipartFile file) {

		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if (user.getRole() != UserRole.PREMIUM || user.getRole() != UserRole.ADMIN) {
			throw new FileUploadNotAllowedException();
		}

		Ticket ticket = ticketRepository.findById(ticketId)
				.orElseThrow(() -> new TicketNotFoundException(ticketId.toString()));

		Map<String, Object> uploadResult = cloudinaryService.upload(file);

		TicketFile ticketFile = new TicketFile();
		ticketFile.setTicket(ticket);
		ticketFile.setFileName(uploadResult.get("original_filename").toString());
		ticketFile.setMimeType(file.getContentType());
		ticketFile.setFileUrl(uploadResult.get("secure_url").toString());
		ticketFile.setPublicId(uploadResult.get("public_id").toString());

		TicketFile saved = ticketFileRepository.save(ticketFile);

		return TicketFileMapper.toResponse(saved);
	}
}