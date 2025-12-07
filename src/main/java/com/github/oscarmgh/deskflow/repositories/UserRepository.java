package com.github.oscarmgh.deskflow.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.github.oscarmgh.deskflow.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByEmail(String email);
}
