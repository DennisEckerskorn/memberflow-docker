package com.denniseckerskorn.controllers.user_managment_controllers;

import com.denniseckerskorn.dtos.user_managment_dtos.TeacherDTO;
import com.denniseckerskorn.entities.user_managment.users.Teacher;
import com.denniseckerskorn.entities.user_managment.users.User;
import com.denniseckerskorn.exceptions.InvalidDataException;
import com.denniseckerskorn.services.user_managment_services.RoleService;
import com.denniseckerskorn.services.user_managment_services.TeacherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/teachers")
@Tag(name = "Teacher Management", description = "Operations related to teachers")
public class TeacherController {

    private final TeacherService teacherService;
    private final RoleService roleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public TeacherController(TeacherService teacherService, RoleService roleService) {
        this.teacherService = teacherService;
        this.roleService = roleService;
    }

    @Operation(summary = "Get all teachers", description = "Retrieve a list of all teachers")
    @GetMapping("/getAll")
    public ResponseEntity<List<TeacherDTO>> getAllTeachers() {
        List<Teacher> teachers = teacherService.findAll();
        if (teachers.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<TeacherDTO> dtos = teachers.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    @Operation(summary = "Create a new teacher", description = "Create a new teacher")
    @PostMapping("/create")
    public ResponseEntity<TeacherDTO> createTeacher(@RequestBody TeacherDTO dto) {
        Teacher teacher = convertToEntity(dto, true);
        Teacher saved = teacherService.save(teacher);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDTO(saved));
    }

    @Operation(summary = "Find a teacher by ID", description = "Retrieve a teacher by their ID")
    @GetMapping("/findById/{id}")
    public ResponseEntity<TeacherDTO> findById(@PathVariable Integer id) {
        Teacher teacher = teacherService.findById(id);
        if (teacher == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(convertToDTO(teacher));
    }

    @Operation(summary = "Update a teacher", description = "Update an existing teacher")
    @PutMapping("/update/{id}")
    public ResponseEntity<TeacherDTO> updateTeacher(@PathVariable Integer id, @RequestBody TeacherDTO dto) {
        dto.setId(id);
        Teacher teacher = convertToEntity(dto, false);
        Teacher updated = teacherService.update(teacher);
        return ResponseEntity.ok(convertToDTO(updated));
    }


    @Operation(summary = "Delete a teacher", description = "Delete a teacher by their ID")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteTeacher(@PathVariable Integer id) {
        teacherService.deleteById(id);
        return ResponseEntity.ok("Teacher deleted successfully");
    }

    // ----------------- Mapping ------------------

    private TeacherDTO convertToDTO(Teacher teacher) {
        return TeacherDTO.fromEntity(teacher);
    }

    private Teacher convertToEntity(TeacherDTO dto, boolean isCreate) {
        if (dto == null || dto.getUser() == null) {
            throw new InvalidDataException("User data is required");
        }

        User user;

        if (isCreate) {
            user = new User();
            user.setRegisterDate(dto.getUser().getRegisterDate() != null ? dto.getUser().getRegisterDate() : LocalDateTime.now());
        } else {
            Teacher existing = teacherService.findById(dto.getId());
            user = existing.getUser();
        }

        user.setName(dto.getUser().getName());
        user.setSurname(dto.getUser().getSurname());
        user.setEmail(dto.getUser().getEmail());
        user.setPhoneNumber(dto.getUser().getPhoneNumber());
        user.setAddress(dto.getUser().getAddress());
        user.setStatus(dto.getUser().getStatus());

        if (dto.getUser().getRoleName() != null) {
            user.setRole(roleService.findRoleByName(dto.getUser().getRoleName()));
        }

        if (isCreate) {
            if (dto.getUser().getPassword() == null || dto.getUser().getPassword().isBlank()) {
                throw new InvalidDataException("Password is required for new teacher");
            }
            user.setPassword(passwordEncoder.encode(dto.getUser().getPassword()));
        } else {
            if (dto.getUser().getPassword() != null && !dto.getUser().getPassword().isBlank()) {
                user.setPassword(passwordEncoder.encode(dto.getUser().getPassword()));
            }
        }

        Teacher teacher = isCreate ? new Teacher() : teacherService.findById(dto.getId());
        teacher.setUser(user);
        teacher.setDiscipline(dto.getDiscipline());

        return teacher;
    }

}
