package com.github.oscarmgh.deskflow.repositories;

import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.github.oscarmgh.deskflow.entities.UserToken;

public interface UserTokenRepository extends JpaRepository<UserToken, Long> {
	Optional<UserToken> findByTokenAndRevokedFalse(String token);
	void deleteByExpiresAtBefore(LocalDateTime now);
}
