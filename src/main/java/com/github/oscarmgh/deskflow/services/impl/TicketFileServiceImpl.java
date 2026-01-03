package com.github.oscarmgh.deskflow.services.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.github.oscarmgh.deskflow.dtos.ticket.TicketFileMapper;
import com.github.oscarmgh.deskflow.dtos.ticket.TicketFileResponse;
import com.github.oscarmgh.deskflow.entities.Ticket;
import com.github.oscarmgh.deskflow.entities.User;
import com.github.oscarmgh.deskflow.entities.enums.TicketFile;
import com.github.oscarmgh.deskflow.entities.enums.UserRole;
import com.github.oscarmgh.deskflow.exceptions.tickets.FileDeleteNotAllowedException;
import com.github.oscarmgh.deskflow.exceptions.tickets.FileUploadNotAllowedException;
import com.github.oscarmgh.deskflow.exceptions.tickets.ResourceNotFoundException;
import com.github.oscarmgh.deskflow.exceptions.tickets.UnauthorizedTicketAccessException;
import com.github.oscarmgh.deskflow.repositories.TicketFileRepository;
import com.github.oscarmgh.deskflow.repositories.TicketRepository;
import com.github.oscarmgh.deskflow.services.CloudinaryService;
import com.github.oscarmgh.deskflow.services.TicketFileService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TicketFileServiceImpl implements TicketFileService {

	private final TicketRepository ticketRepository;
	private final TicketFileRepository ticketFileRepository;
	private final CloudinaryService cloudinaryService;

	@Override
	@Transactional
	public TicketFileResponse uploadFile(Long ticketId, MultipartFile file) {
		if (file == null || file.isEmpty()) {
			throw new IllegalArgumentException("File is empty");
		}

		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if (user.getRole() != UserRole.PREMIUM && user.getRole() != UserRole.ADMIN) {
			throw new FileUploadNotAllowedException();
		}

		Ticket ticket = ticketRepository.findById(ticketId)
				.orElseThrow(() -> new ResourceNotFoundException("Ticket", ticketId));

		Map<String, Object> result = cloudinaryService.upload(file);

		TicketFile ticketFile = new TicketFile();
		ticketFile.setTicket(ticket);
		ticketFile.setFileName(file.getOriginalFilename());
		ticketFile.setMimeType(file.getContentType());
		ticketFile.setFileUrl(result.get("secure_url").toString());
		ticketFile.setPublicId(result.get("public_id").toString());

		return TicketFileMapper.toResponse(ticketFileRepository.save(ticketFile));
	}

	public List<TicketFileResponse> getFiles(Long ticketId) {
		return ticketFileRepository.findByTicketId(ticketId)
				.stream()
				.map(TicketFileMapper::toResponse)
				.collect(Collectors.toList());
	}

	public void deleteFile(Long ticketId, Long fileId) {

		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		System.out.println(user.getRole());
		System.out.println(user.getRole() != UserRole.PREMIUM);
		System.out.println(user.getRole() != UserRole.ADMIN);

		if (user.getRole() != UserRole.PREMIUM && user.getRole() != UserRole.ADMIN) {
			throw new FileDeleteNotAllowedException();
		}

		TicketFile file = ticketFileRepository.findById(fileId)
				.orElseThrow(() -> new ResourceNotFoundException("TicketFile", fileId));

		if (!file.getTicket().getId().equals(ticketId)) {
			throw new UnauthorizedTicketAccessException();
		}
		cloudinaryService.delete(file.getPublicId());
		ticketFileRepository.delete(file);
	}
}