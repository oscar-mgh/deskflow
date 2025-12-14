package com.github.oscarmgh.deskflow.repositories;

import java.time.Instant;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.github.oscarmgh.deskflow.entities.Ticket;
import com.github.oscarmgh.deskflow.entities.TicketCategory;
import com.github.oscarmgh.deskflow.entities.User;
import com.github.oscarmgh.deskflow.entities.enums.TicketStatus;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

	Page<Ticket> findByUser(User user, Pageable pageable);

	List<Ticket> findByCategory(TicketCategory category);

	long countByAgentIdAndStatus(Long agentId, TicketStatus status);

	@Query(value = "SELECT t.id FROM tickets t WHERE t.created_at < :cutoff ORDER BY t.created_at ASC LIMIT :limit", nativeQuery = true)
	List<Long> findOldestIds(@Param("cutoff") Instant cutoff, @Param("limit") int limit);

	@Modifying
	@Query("DELETE FROM Ticket t WHERE t.id IN :ids")
	int deleteByIds(@Param("ids") List<Long> ids);
}
