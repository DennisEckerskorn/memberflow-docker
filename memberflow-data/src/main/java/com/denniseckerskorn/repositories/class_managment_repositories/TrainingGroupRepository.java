package com.denniseckerskorn.repositories.class_managment_repositories;

import com.denniseckerskorn.entities.class_managment.TrainingGroup;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing TrainingGroup entities.
 * Provides methods to perform CRUD operations on TrainingGroup entities.
 */
public interface TrainingGroupRepository extends JpaRepository<TrainingGroup, Integer> {
}
