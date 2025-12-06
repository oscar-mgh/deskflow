package com.github.oscarmgh.deskflow.entities;

import java.time.LocalDateTime;

import com.github.oscarmgh.deskflow.entities.enums.TicketPriority;
import com.github.oscarmgh.deskflow.entities.enums.TicketStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tickets")
@Getter
@Setter
public class Ticket {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String title;

	@Column(nullable = false, columnDefinition = "TEXT")
	private String description;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private TicketStatus status = TicketStatus.OPEN;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private TicketPriority priority = TicketPriority.MEDIUM;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user; // NULL si es invitado

	@Column(nullable = false)
	private LocalDateTime createdAt = LocalDateTime.now();
}
