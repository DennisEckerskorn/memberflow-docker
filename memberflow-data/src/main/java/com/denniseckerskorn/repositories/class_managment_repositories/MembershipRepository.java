package com.denniseckerskorn.repositories.class_managment_repositories;

import com.denniseckerskorn.entities.class_managment.Membership;
import com.denniseckerskorn.enums.MembershipTypeValues;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface MembershipRepository extends JpaRepository<Membership, Integer> {
    Membership findByType(MembershipTypeValues type);

    Membership findByStartDate(LocalDate startDate);

    Membership findByEndDate(LocalDate endDate);

    Membership findByStatus(String status);

    Membership findByStudentId(Integer studentId);

    boolean existsByType(MembershipTypeValues type);
}
