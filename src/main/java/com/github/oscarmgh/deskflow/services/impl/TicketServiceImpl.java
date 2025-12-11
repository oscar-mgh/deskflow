package com.github.oscarmgh.deskflow.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.oscarmgh.deskflow.dtos.ticket.TicketRequest;
import com.github.oscarmgh.deskflow.dtos.ticket.TicketResponse;
import com.github.oscarmgh.deskflow.entities.Ticket;
import com.github.oscarmgh.deskflow.entities.User;
import com.github.oscarmgh.deskflow.entities.enums.TicketStatus;
import com.github.oscarmgh.deskflow.exceptions.tickets.TicketNotFoundException;
import com.github.oscarmgh.deskflow.repositories.TicketRepository;
import com.github.oscarmgh.deskflow.services.TicketService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;

    public List<TicketResponse> getDemoTickets() {
        return ticketRepository.findByDemoTrue()
                .stream()
                .map(TicketResponse::new)
                .toList();
    }

    public TicketResponse getDemoTicket(Long id) {
        Ticket ticket = ticketRepository.findById(id)
                .filter(Ticket::getDemo)
                .orElseThrow(() -> new TicketNotFoundException(id.toString()));
        return new TicketResponse(ticket);
    }

    public List<TicketResponse> getUserTickets(User user) {
        return ticketRepository.findByUserAndDemoFalse(user)
                .stream()
                .map(TicketResponse::new)
                .toList();
    }

    public TicketResponse createTicket(TicketRequest request, User user) {
        Ticket ticket = Ticket.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .status(TicketStatus.OPEN)
                .priority(request.getPriority())
                .user(user)
                .demo(false)
                .build();

        Ticket saved = ticketRepository.save(ticket);
        return new TicketResponse(saved);
    }

    public TicketResponse getById(Long id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new TicketNotFoundException(id.toString()));
        return new TicketResponse(ticket);
    }

    public TicketResponse getUserTicket(Long id, User user) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new TicketNotFoundException(id.toString()));

        if (ticket.getUser() == null || !ticket.getUser().getId().equals(user.getId())) {
            throw new TicketNotFoundException(id.toString());
        }

        return new TicketResponse(ticket);
    }

    public TicketResponse updateTicket(Long ticketId, TicketRequest request, User user) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new TicketNotFoundException(ticketId.toString()));

        if (!ticket.getUser().getId().equals(user.getId())) {
            throw new TicketNotFoundException("Ticket not found");
        }

        if (ticket.getDemo()) {
            throw new IllegalStateException("Demo tickets cannot be updated");
        }

        if (request.getTitle() != null) {
            ticket.setTitle(request.getTitle());
            request.getTitle();
        }
        if (request.getDescription() != null) {
            ticket.setDescription(request.getDescription());
            request.getDescription();
        }
        if (request.getPriority() != null) {
            ticket.setPriority(request.getPriority());
            request.getPriority().name();
        }
        if (request.getStatus() != null) {
            ticket.setStatus(request.getStatus());
            request.getStatus().name();
        }

        Ticket updated = ticketRepository.save(ticket);
        return new TicketResponse(updated);
    }

    public void deleteTicket(Long ticketId, User user) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new TicketNotFoundException(ticketId.toString()));
        if (!ticket.getUser().getId().equals(user.getId())) {
            throw new TicketNotFoundException("Ticket not found");
        }
        if (ticket.getDemo()) {
            throw new IllegalStateException("Demo tickets cannot be deleted");
        }
        ticketRepository.delete(ticket);
    }
}
