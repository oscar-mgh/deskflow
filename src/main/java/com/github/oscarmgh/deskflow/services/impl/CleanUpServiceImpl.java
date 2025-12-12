package com.github.oscarmgh.deskflow.services.impl;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.oscarmgh.deskflow.repositories.TicketRepository;
import com.github.oscarmgh.deskflow.services.CleanUpService;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class CleanUpServiceImpl implements CleanUpService {

	private final TicketRepository repository;

	public CleanUpServiceImpl(TicketRepository repository) {
		this.repository = repository;
	}

	@Scheduled(cron = "0 0 1 * * *")
	@Transactional
	public void cleanupOldEntities() {
		Instant sixMonthsAgo = Instant.now().minus(6, ChronoUnit.MONTHS);
		final int BATCH_SIZE = 1000;

		int totalDeleted = 0;
		List<Long> idsToDelete;

		do {
			idsToDelete = repository.findOldestIds(sixMonthsAgo, BATCH_SIZE);

			if (!idsToDelete.isEmpty()) {

				int deletedCount = repository.deleteByIds(idsToDelete);
				totalDeleted += deletedCount;
				System.out.println("Deleted batch of " + deletedCount + " entities.");

			}
		} while (!idsToDelete.isEmpty());

		System.out.println("Cleanup task finished. Total deleted: " + totalDeleted);
	}
}