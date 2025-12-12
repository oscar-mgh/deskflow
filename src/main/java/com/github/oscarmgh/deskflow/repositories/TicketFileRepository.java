package com.github.oscarmgh.deskflow.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.oscarmgh.deskflow.entities.enums.TicketFile;

public interface TicketFileRepository extends JpaRepository<TicketFile, Long> {
}