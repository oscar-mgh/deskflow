package com.github.oscarmgh.deskflow.dtos.auth;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.oscarmgh.deskflow.entities.UserToken;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenResponse {
	private String token;
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssX")
	private OffsetDateTime expiresAt;

	public TokenResponse(UserToken token) {
		this.token = token.getToken();
		this.expiresAt = token.getExpiresAt();
	}
}
