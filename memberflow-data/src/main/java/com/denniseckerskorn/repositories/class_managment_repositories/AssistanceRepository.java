package com.denniseckerskorn.repositories.class_managment_repositories;

import com.denniseckerskorn.entities.class_managment.Assistance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository interface for managing Assistance entities.
 * Provides methods to find assistance records by training session ID, student ID,
 * and date range, as well as a method to find assistance by both training session ID and student ID.
 */
public interface AssistanceRepository extends JpaRepository<Assistance, Integer> {
    Assistance findByTrainingSessionId(Integer trainingSessionId);

    Assistance findByStudentId(Integer studentId);

    @Query("SELECT a FROM Assistance a WHERE a.date BETWEEN :start AND :end")
    List<Assistance> findAllByDateRange(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    Assistance findByTrainingSessionIdAndStudentId(Integer trainingSessionId, Integer studentId);
}
