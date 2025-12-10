package com.github.oscarmgh.deskflow.services;

import com.github.oscarmgh.deskflow.dtos.auth.LoginRequest;
import com.github.oscarmgh.deskflow.dtos.auth.RegisterRequest;
import com.github.oscarmgh.deskflow.dtos.auth.TokenResponse;

public interface AuthService {
	public TokenResponse login(LoginRequest request);

	public TokenResponse register(RegisterRequest request);

	public void logout(String token);
}
