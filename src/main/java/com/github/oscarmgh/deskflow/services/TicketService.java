package com.github.oscarmgh.deskflow.services;

import org.springframework.data.domain.Pageable;

import com.github.oscarmgh.deskflow.dtos.PageResponse;
import com.github.oscarmgh.deskflow.dtos.ticket.TicketRequest;
import com.github.oscarmgh.deskflow.dtos.ticket.TicketResponse;
import com.github.oscarmgh.deskflow.entities.User;

public interface TicketService {
    PageResponse<TicketResponse> getUserTickets(User user, Pageable pageable);

    TicketResponse createTicket(TicketRequest request, User user);

    TicketResponse getById(Long id);

    PageResponse<TicketResponse> getTicketsByAgent(Long id, User user, Pageable pageable);

    TicketResponse getUserTicket(Long id, User user);

    TicketResponse updateTicket(Long id, TicketRequest request, User user);

    void deleteTicket(Long id, User user);

    TicketResponse assignAgent(Long ticketId, Long agentId, User admin);
}
