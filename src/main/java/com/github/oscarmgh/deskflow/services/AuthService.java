package com.github.oscarmgh.deskflow.services;

import com.github.oscarmgh.deskflow.dtos.auth.LoginRequest;
import com.github.oscarmgh.deskflow.dtos.auth.RegisterRequest;

public interface AuthService {
	public String login(LoginRequest request);

	public String register(RegisterRequest request);

	public void logout(String token);
}
