package com.github.oscarmgh.deskflow.entities;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

import com.github.oscarmgh.deskflow.entities.enums.TicketPriority;
import com.github.oscarmgh.deskflow.entities.enums.TicketStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tickets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ticket {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true, length = 20)
	private String code;

	@Column(nullable = false)
	private String title;

	@Column(nullable = false, columnDefinition = "TEXT")
	private String description;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private TicketStatus status;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private TicketPriority priority;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "agent_id")
	private User agent;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "category_id")
	private TicketCategory category;

	@OneToMany(mappedBy = "ticket", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Comment> comments;

	@Column(name = "created_at", nullable = false, updatable = false)
	private OffsetDateTime createdAt;

	@Column(name = "updated_at")
	private OffsetDateTime updatedAt;

	@Column(name = "closed_at")
	private OffsetDateTime closedAt;

	@PrePersist
	protected void onCreate() {
		this.createdAt = OffsetDateTime.now(ZoneOffset.UTC);
		if (this.code == null) {
			this.code = "TK-" + System.currentTimeMillis() % 100000;
		}
	}

	@PreUpdate
	protected void onUpdate() {
		this.updatedAt = OffsetDateTime.now(ZoneOffset.UTC);

		if (this.status == TicketStatus.RESOLVED || this.status == TicketStatus.CLOSED) {
			this.closedAt = OffsetDateTime.now(ZoneOffset.UTC);
		}
	}
}