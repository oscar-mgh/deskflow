package com.github.oscarmgh.deskflow.services;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.oscarmgh.deskflow.entities.User;
import com.github.oscarmgh.deskflow.entities.UserToken;
import com.github.oscarmgh.deskflow.repositories.UserTokenRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TokenService {

	private final UserTokenRepository tokenRepository;

	@Value("${app.security.token-expiration-hours}")
	private long tokenExpirationHours;

	public UserToken generateToken(User user) {

		OffsetDateTime nowUtc = OffsetDateTime.now(ZoneOffset.UTC);

		UserToken token = UserToken.builder()
				.token(generateRandomToken())
				.user(user)
				.expiresAt(
						nowUtc.plusHours(tokenExpirationHours))
				.revoked(false)
				.build();

		return tokenRepository.save(token);
	}

	public boolean isValid(String tokenValue) {

		OffsetDateTime nowUtc = OffsetDateTime.now(ZoneOffset.UTC);

		return tokenRepository
				.findByTokenAndRevokedFalse(tokenValue)
				.filter(token -> token.getExpiresAt().isAfter(nowUtc))
				.isPresent();
	}

	public User getUserFromToken(String tokenValue) {

		OffsetDateTime nowUtc = OffsetDateTime.now(ZoneOffset.UTC);

		return tokenRepository
				.findByTokenAndRevokedFalse(tokenValue)
				.filter(token -> token.getExpiresAt().isAfter(nowUtc))
				.map(UserToken::getUser)
				.orElse(null);
	}

	@Transactional
	public void revokeToken(String tokenValue) {

		tokenRepository.findByTokenAndRevokedFalse(tokenValue)
				.ifPresent(token -> {
					token.setRevoked(true);
					tokenRepository.save(token);
				});
	}

	private String generateRandomToken() {
		return UUID.randomUUID()
				.toString()
				.replace("-", "");
	}
}
