package com.github.oscarmgh.deskflow.services.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.oscarmgh.deskflow.dtos.ticket.TicketRequest;
import com.github.oscarmgh.deskflow.dtos.ticket.TicketResponse;
import com.github.oscarmgh.deskflow.entities.Ticket;
import com.github.oscarmgh.deskflow.entities.User;
import com.github.oscarmgh.deskflow.entities.enums.TicketStatus;
import com.github.oscarmgh.deskflow.entities.enums.UserRole;
import com.github.oscarmgh.deskflow.exceptions.auth.UserNotFoundException;
import com.github.oscarmgh.deskflow.exceptions.tickets.TicketNotFoundException;
import com.github.oscarmgh.deskflow.exceptions.tickets.UnauthorizedTicketAccessException;
import com.github.oscarmgh.deskflow.repositories.TicketRepository;
import com.github.oscarmgh.deskflow.repositories.UserRepository;
import com.github.oscarmgh.deskflow.services.AgentService;
import com.github.oscarmgh.deskflow.services.TicketService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final AgentService agentService;
    private final UserRepository userRepository;

    @Override
    public Page<TicketResponse> getUserTickets(User user, Pageable pageable) {
        return ticketRepository.findByUser(user, pageable)
                .map(TicketResponse::new);
    }

    @Override
    public TicketResponse createTicket(TicketRequest request, User user) {
        User agent = agentService.findBestAvailableAgent();

        if (agent == null) {
            throw new IllegalStateException("No agents available");
        }

        Ticket ticket = Ticket.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .status(TicketStatus.OPEN)
                .priority(request.getPriority())
                .user(user)
                .agent(agent)
                .build();

        Ticket saved = ticketRepository.save(ticket);
        return new TicketResponse(saved);
    }

    @Override
    public TicketResponse getById(Long id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(TicketNotFoundException::new);
        return new TicketResponse(ticket);
    }

    @Override
    public TicketResponse getUserTicket(Long id, User user) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(TicketNotFoundException::new);

        authorizeTicketAccess(ticket, user);

        return new TicketResponse(ticket);
    }

    @Override
    public TicketResponse updateTicket(Long ticketId, TicketRequest request, User user) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(TicketNotFoundException::new);

        authorizeTicketAccess(ticket, user);

        if (request.getTitle() != null) {
            ticket.setTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            ticket.setDescription(request.getDescription());
        }
        if (request.getPriority() != null) {
            ticket.setPriority(request.getPriority());
        }
        if (request.getStatus() != null) {
            ticket.setStatus(request.getStatus());
        }

        Ticket updated = ticketRepository.save(ticket);
        return new TicketResponse(updated);
    }

    @Override
    public void deleteTicket(Long ticketId, User user) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new TicketNotFoundException());

        if (user.getRole() != UserRole.ADMIN &&
                !ticket.getUser().getId().equals(user.getId())) {
            throw new TicketNotFoundException();
        }

        ticketRepository.delete(ticket);
    }

    @Override
    public TicketResponse assignAgent(Long ticketId, Long agentId, User admin) {

        if (admin.getRole() != UserRole.ADMIN) {
            throw new UnauthorizedTicketAccessException();
        }

        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(UserNotFoundException::new);

        User agent = userRepository.findById(agentId)
                .orElseThrow(UserNotFoundException::new);

        if (agent.getRole() != UserRole.AGENT) {
            throw new IllegalStateException("El usuario seleccionado no es un agente");
        }

        ticket.setAgent(agent);

        Ticket updated = ticketRepository.save(ticket);
        return new TicketResponse(updated);
    }

    private void authorizeTicketAccess(Ticket ticket, User user) {

        switch (user.getRole()) {

            case ADMIN:
                return;

            case AGENT:
                if (ticket.getAgent() != null &&
                        ticket.getAgent().getId().equals(user.getId())) {
                    return; // agente asignado
                }
                throw new TicketNotFoundException();

            case USER:
            case PREMIUM:
                if (ticket.getUser() != null &&
                        ticket.getUser().getId().equals(user.getId())) {
                    return; // dueño del ticket
                }
                throw new TicketNotFoundException();

            default:
                throw new TicketNotFoundException();
        }
    }

}
