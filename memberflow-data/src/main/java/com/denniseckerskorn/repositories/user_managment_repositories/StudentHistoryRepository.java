package com.denniseckerskorn.repositories.user_managment_repositories;

import com.denniseckerskorn.entities.user_managment.StudentHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentHistoryRepository extends JpaRepository<StudentHistory, Integer> {
    boolean existsByEventType(String eventType);

}
