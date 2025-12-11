package com.github.oscarmgh.deskflow.services;

import java.util.List;

import com.github.oscarmgh.deskflow.dtos.ticket.TicketRequest;
import com.github.oscarmgh.deskflow.dtos.ticket.TicketResponse;
import com.github.oscarmgh.deskflow.entities.User;

public interface TicketService {

    List<TicketResponse> getDemoTickets();

    TicketResponse getDemoTicket(Long id);

    List<TicketResponse> getUserTickets(User user);

    TicketResponse createTicket(TicketRequest request, User user);

    TicketResponse getById(Long id);

    TicketResponse getUserTicket(Long id, User user);

    TicketResponse updateTicket(Long id, TicketRequest request, User user);

    void deleteTicket(Long id, User user);
}
