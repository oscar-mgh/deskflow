package com.github.oscarmgh.deskflow.entities.enums;

import java.time.OffsetDateTime;

import com.github.oscarmgh.deskflow.entities.Ticket;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "ticket_files")
@Getter
@Setter
public class TicketFile {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String fileName;

	@Column(nullable = false)
	private String fileUrl;

	@Column(nullable = false)
	private String fileType;

	@ManyToOne
	@JoinColumn(name = "ticket_id", nullable = false)
	private Ticket ticket;

	@Column(nullable = false)
	private OffsetDateTime uploadedAt = OffsetDateTime.now();
}
