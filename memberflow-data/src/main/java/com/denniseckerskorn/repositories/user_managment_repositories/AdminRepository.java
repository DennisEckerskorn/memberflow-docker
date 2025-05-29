package com.denniseckerskorn.repositories.user_managment_repositories;

import com.denniseckerskorn.entities.user_managment.users.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing Admin entities.
 * Provides methods to perform CRUD operations on Admin entities.
 */
public interface AdminRepository extends JpaRepository<Admin, Integer> {
    boolean existsByUserEmail(String email);

    Admin findByUserEmail(String email);

}
