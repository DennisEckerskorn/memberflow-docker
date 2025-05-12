package com.denniseckerskorn.repositories.user_managment_repositories;

import com.denniseckerskorn.entities.user_managment.users.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherRepository extends JpaRepository<Teacher, Integer> {

    boolean existsByUserEmail(String mail);

    Teacher findByUserEmail(String email);


}
