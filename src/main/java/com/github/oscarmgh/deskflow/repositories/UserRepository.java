package com.github.oscarmgh.deskflow.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.github.oscarmgh.deskflow.entities.User;
import com.github.oscarmgh.deskflow.entities.enums.UserRole;

import jakarta.transaction.Transactional;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByEmail(String email);

	List<User> findByRole(UserRole role);

	@Modifying
	@Transactional
	@Query("UPDATE User u SET u.role = :role WHERE u.email = :email")
	void updateRole(String email, UserRole role);
}
