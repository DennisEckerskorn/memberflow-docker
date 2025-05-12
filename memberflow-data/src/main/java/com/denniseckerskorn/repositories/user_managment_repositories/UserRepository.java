package com.denniseckerskorn.repositories.user_managment_repositories;

import com.denniseckerskorn.entities.user_managment.Notification;
import com.denniseckerskorn.entities.user_managment.users.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsByEmail(String email);

    User findByEmail(String email);

    List<User> findAllByRoleName(String userRole);
}
