package com.github.oscarmgh.deskflow.services.impl;

import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.oscarmgh.deskflow.entities.User;
import com.github.oscarmgh.deskflow.entities.enums.TicketStatus;
import com.github.oscarmgh.deskflow.entities.enums.UserRole;
import com.github.oscarmgh.deskflow.repositories.TicketRepository;
import com.github.oscarmgh.deskflow.repositories.UserRepository;
import com.github.oscarmgh.deskflow.services.AgentService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AgentServiceImpl implements AgentService {

	private final UserRepository userRepository;
	private final TicketRepository ticketRepository;

	@Override
	public User findBestAvailableAgent() {

		List<User> agents = userRepository.findByRole(UserRole.AGENT);

		if (agents.isEmpty()) {
			throw new IllegalStateException("No agents available");
		}

		return agents.stream()
				.min(Comparator.comparing(agent -> ticketRepository.countByAgentIdAndStatus(
						agent.getId(),
						TicketStatus.OPEN)))
				.orElseThrow(() -> new IllegalStateException("Unable to determine best agent"));
	}
}