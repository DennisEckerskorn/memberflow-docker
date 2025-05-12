package com.denniseckerskorn.repositories.class_managment_repositories;

import com.denniseckerskorn.entities.class_managment.TrainingGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainingGroupRepository extends JpaRepository<TrainingGroup, Integer> {
}
