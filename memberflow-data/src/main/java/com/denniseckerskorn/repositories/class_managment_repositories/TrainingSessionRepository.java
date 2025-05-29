package com.denniseckerskorn.repositories.class_managment_repositories;

import com.denniseckerskorn.entities.class_managment.TrainingSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

import java.util.List;

/**
 * Repository interface for managing TrainingSession entities.
 * Provides methods to find training sessions by date and date range.
 */
public interface TrainingSessionRepository extends JpaRepository<TrainingSession, Integer> {
    TrainingSession findByDate(LocalDateTime date);

    List<TrainingSession> findAllByDateBetween(LocalDateTime start, LocalDateTime end);


}
