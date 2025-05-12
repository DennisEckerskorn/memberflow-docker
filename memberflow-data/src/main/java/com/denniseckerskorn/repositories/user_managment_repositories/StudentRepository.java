package com.denniseckerskorn.repositories.user_managment_repositories;

import com.denniseckerskorn.entities.user_managment.users.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Integer> {
    boolean existsByDni(String dni);
    Student findByDni(String dni);
    boolean existsByUserEmail(String email);
    Student findByUserEmail(String email);
}
