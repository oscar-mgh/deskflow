package com.github.oscarmgh.deskflow.repositories;

import java.time.OffsetDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.oscarmgh.deskflow.entities.UserToken;

public interface UserTokenRepository extends JpaRepository<UserToken, Long> {
	Optional<UserToken> findByTokenAndRevokedFalse(String token);

	void deleteByExpiresAtBefore(OffsetDateTime now);
}
