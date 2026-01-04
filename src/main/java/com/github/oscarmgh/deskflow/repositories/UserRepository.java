package com.github.oscarmgh.deskflow.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.github.oscarmgh.deskflow.entities.User;
import com.github.oscarmgh.deskflow.entities.enums.TicketStatus;
import com.github.oscarmgh.deskflow.entities.enums.UserRole;

import jakarta.transaction.Transactional;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByEmail(String email);

	List<User> findByRole(UserRole role);

	@Modifying
	@Transactional
	@Query("UPDATE User u SET u.role = :role WHERE u.email = :email")
	void updateRole(String email, UserRole role);

	@Query("""
			SELECT u FROM User u
			LEFT JOIN Ticket t ON u.id = t.agent.id AND t.status = :status
			WHERE u.role = 'AGENT'
			GROUP BY u.id
			ORDER BY COUNT(t.id) ASC, u.id ASC
			""")
	Optional<User> findLeastBusyAgent(@Param("status") TicketStatus status);
}
