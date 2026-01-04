package com.github.oscarmgh.deskflow.services.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.oscarmgh.deskflow.entities.User;
import com.github.oscarmgh.deskflow.entities.enums.TicketStatus;
import com.github.oscarmgh.deskflow.repositories.UserRepository;
import com.github.oscarmgh.deskflow.services.AgentService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AgentServiceImpl implements AgentService {

	private final UserRepository userRepository;

	@Override
	public User findBestAvailableAgent() {
		return userRepository.findLeastBusyAgent(TicketStatus.OPEN)
				.orElseThrow(() -> new RuntimeException("No available agents"));
	}
}