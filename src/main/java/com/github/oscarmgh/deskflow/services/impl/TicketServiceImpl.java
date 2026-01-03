package com.github.oscarmgh.deskflow.services.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.oscarmgh.deskflow.dtos.PageResponse;
import com.github.oscarmgh.deskflow.dtos.ticket.TicketRequest;
import com.github.oscarmgh.deskflow.dtos.ticket.TicketResponse;
import com.github.oscarmgh.deskflow.entities.Ticket;
import com.github.oscarmgh.deskflow.entities.TicketCategory;
import com.github.oscarmgh.deskflow.entities.User;
import com.github.oscarmgh.deskflow.entities.enums.TicketStatus;
import com.github.oscarmgh.deskflow.entities.enums.UserRole;
import com.github.oscarmgh.deskflow.exceptions.tickets.ResourceNotFoundException;
import com.github.oscarmgh.deskflow.exceptions.tickets.UnauthorizedTicketAccessException;
import com.github.oscarmgh.deskflow.repositories.TicketCategoryRepository;
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
    private final TicketCategoryRepository ticketCategoryRepository;

    @Override
    public PageResponse<TicketResponse> getUserTickets(User user, Pageable pageable) {

        Page<TicketResponse> page = ticketRepository.findByUser(user, pageable)
                .map(TicketResponse::new);

        return PageResponse.<TicketResponse>builder()
                .content(page.getContent())
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .build();
    }

    @Override
    @Transactional
    public TicketResponse createTicket(TicketRequest request, User user) {
        TicketCategory category = ticketCategoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category", request.getCategoryId()));

        User agent = agentService.findBestAvailableAgent();

        Ticket ticket = Ticket.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .priority(request.getPriority())
                .status(TicketStatus.OPEN)
                .user(user)
                .agent(agent)
                .category(category)
                .build();

        return new TicketResponse(ticketRepository.save(ticket));
    }

    @Override
    public TicketResponse getById(Long id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket", id));
        return new TicketResponse(ticket);
    }

    @Override
    public TicketResponse getUserTicket(Long id, User user) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket", id));

        authorizeTicketAccess(ticket, user);

        return new TicketResponse(ticket);
    }

    @Override
    public TicketResponse updateTicket(Long ticketId, TicketRequest request, User user) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket", ticketId));

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
                .orElseThrow(() -> new ResourceNotFoundException("Ticket", ticketId));

        if (user.getRole() != UserRole.ADMIN &&
                !ticket.getUser().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("Ticket", ticketId);
        }

        ticketRepository.delete(ticket);
    }

    @Override
    public TicketResponse assignAgent(Long ticketId, Long agentId, User admin) {

        if (admin.getRole() != UserRole.ADMIN) {
            throw new UnauthorizedTicketAccessException();
        }

        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket", ticketId));

        User agent = userRepository.findById(agentId)
                .orElseThrow(() -> new ResourceNotFoundException("User", agentId));

        if (agent.getRole() != UserRole.AGENT) {
            throw new IllegalStateException("The selected user is not an agent");
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
                    return;
                }
                throw new ResourceNotFoundException("Ticket", ticket.getId());

            case USER:
            case PREMIUM:
                if (ticket.getUser() != null &&
                        ticket.getUser().getId().equals(user.getId())) {
                    return;
                }
                throw new ResourceNotFoundException("Ticket", ticket.getId());

            default:
                throw new ResourceNotFoundException("Ticket", ticket.getId());
        }
    }

}
