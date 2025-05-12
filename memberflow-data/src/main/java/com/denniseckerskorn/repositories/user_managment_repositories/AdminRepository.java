package com.denniseckerskorn.repositories.user_managment_repositories;

import com.denniseckerskorn.entities.user_managment.users.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Integer> {
    boolean existsByUserEmail(String email);

    Admin findByUserEmail(String email);

}
