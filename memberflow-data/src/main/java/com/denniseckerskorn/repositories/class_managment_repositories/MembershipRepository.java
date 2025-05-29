package com.denniseckerskorn.repositories.class_managment_repositories;

import com.denniseckerskorn.entities.class_managment.Membership;
import com.denniseckerskorn.enums.MembershipTypeValues;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

/**
 * Repository interface for managing Membership entities.
 * Provides methods to find memberships by various attributes.
 */
public interface MembershipRepository extends JpaRepository<Membership, Integer> {
    Membership findByType(MembershipTypeValues type);

    Membership findByStartDate(LocalDate startDate);

    Membership findByEndDate(LocalDate endDate);

    Membership findByStatus(String status);

    @Query("SELECT m FROM Membership m JOIN m.students s WHERE s.id = :studentId")
    Membership findByStudentId(@Param("studentId") Integer studentId);

    boolean existsByType(MembershipTypeValues type);
}
