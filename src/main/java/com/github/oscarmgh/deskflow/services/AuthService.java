package com.github.oscarmgh.deskflow.services;

import com.github.oscarmgh.deskflow.dtos.auth.AuthResponse;
import com.github.oscarmgh.deskflow.dtos.auth.LoginRequest;
import com.github.oscarmgh.deskflow.dtos.auth.RegisterRequest;

public interface AuthService {
	public AuthResponse login(LoginRequest request);

	public AuthResponse register(RegisterRequest request);
}
