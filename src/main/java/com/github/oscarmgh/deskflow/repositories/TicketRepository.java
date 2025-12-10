package com.github.oscarmgh.deskflow.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.oscarmgh.deskflow.entities.Ticket;
import com.github.oscarmgh.deskflow.entities.User;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

	List<Ticket> findByDemoTrue();

	List<Ticket> findByUser(User user);
}
