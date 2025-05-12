package com.denniseckerskorn.repositories.class_managment_repositories;

import com.denniseckerskorn.entities.class_managment.Assistance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface AssistanceRepository extends JpaRepository<Assistance, Integer> {
    Assistance findByTrainingSessionId(Integer trainingSessionId);

    Assistance findByStudentId(Integer studentId);

    @Query("SELECT a FROM Assistance a WHERE a.date BETWEEN :start AND :end")
    List<Assistance> findAllByDateRange(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    Assistance findByTrainingSessionIdAndStudentId(Integer trainingSessionId, Integer studentId);
}
