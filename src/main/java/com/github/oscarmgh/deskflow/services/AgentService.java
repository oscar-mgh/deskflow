package com.github.oscarmgh.deskflow.services;

import com.github.oscarmgh.deskflow.entities.User;

public interface AgentService {
	User findBestAvailableAgent();
}