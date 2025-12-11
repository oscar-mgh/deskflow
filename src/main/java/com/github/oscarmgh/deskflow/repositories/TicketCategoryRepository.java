package com.github.oscarmgh.deskflow.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.oscarmgh.deskflow.entities.TicketCategory;

public interface TicketCategoryRepository extends JpaRepository<TicketCategory, Long> {
	Optional<TicketCategory> findByName(String name);
}
