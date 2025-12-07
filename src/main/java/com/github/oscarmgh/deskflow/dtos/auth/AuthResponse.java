package com.github.oscarmgh.deskflow.dtos.auth;

import com.github.oscarmgh.deskflow.dtos.user.UserResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
	public TokenResponse token;
	public UserResponse user;
}
