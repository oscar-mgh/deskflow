package com.github.oscarmgh.deskflow.repositories;

import com.github.oscarmgh.deskflow.entities.Comment;
import com.github.oscarmgh.deskflow.entities.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
	List<Comment> findByTicketOrderByCreatedAtAsc(Ticket ticket);
}