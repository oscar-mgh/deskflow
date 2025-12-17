package com.github.oscarmgh.deskflow.services;

import com.github.oscarmgh.deskflow.entities.User;

public interface TokenService {
	String generateToken(User user);

	boolean isValid(String tokenValue);

	User getUserFromToken(String tokenValue);
}
