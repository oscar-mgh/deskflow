package com.github.oscarmgh.deskflow.services;

import java.util.UUID;

import com.github.oscarmgh.deskflow.entities.User;
import com.github.oscarmgh.deskflow.entities.UserToken;

public interface TokenService {
	UserToken generateToken(User user);

	boolean isValid(String tokenValue);

	User getUserFromToken(String tokenValue);

	void revokeToken(String tokenValue);
}
