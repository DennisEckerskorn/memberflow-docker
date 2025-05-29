package com.denniseckerskorn.controllers.user_managment_controllers;

import com.denniseckerskorn.dtos.class_managment_dtos.AssistanceDTO;
import com.denniseckerskorn.dtos.user_managment_dtos.StudentDTO;
import com.denniseckerskorn.dtos.user_managment_dtos.StudentRegisterDTO;
import com.denniseckerskorn.entities.class_managment.Assistance;
import com.denniseckerskorn.entities.class_managment.Membership;
import com.denniseckerskorn.entities.class_managment.TrainingGroup;
import com.denniseckerskorn.entities.class_managment.TrainingSession;
import com.denniseckerskorn.entities.user_managment.Role;
import com.denniseckerskorn.entities.user_managment.StudentHistory;
import com.denniseckerskorn.entities.user_managment.users.Student;
import com.denniseckerskorn.entities.user_managment.users.User;
import com.denniseckerskorn.enums.StatusValues;
import com.denniseckerskorn.exceptions.InvalidDataException;
import com.denniseckerskorn.services.class_managment_services.MembershipService;
import com.denniseckerskorn.services.class_managment_services.TrainingSessionService;
import com.denniseckerskorn.services.user_managment_services.RoleService;
import com.denniseckerskorn.services.user_managment_services.StudentService;
import com.denniseckerskorn.services.user_managment_services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller for managing students.
 * Provides endpoints for creating, retrieving, updating, and deleting students.
 */
@RestController
@RequestMapping("/api/v1/students")
@Tag(name = "Student Management", description = "Operations related to student management")
public class StudentController {

    private final StudentService studentService;
    private final RoleService roleService;
    private final UserService userService;
    private final MembershipService membershipService;
    private final TrainingSessionService trainingSessionService;

    /**
     * Password encoder for encoding passwords.
     */
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Constructor for StudentController.
     *
     * @param studentService         Service for handling student records.
     * @param roleService            Service for handling roles.
     * @param userService            Service for handling user records.
     * @param membershipService      Service for handling membership records.
     * @param trainingSessionService Service for handling training session records.
     */
    public StudentController(StudentService studentService, RoleService roleService, UserService userService, MembershipService membershipService, TrainingSessionService trainingSessionService) {
        this.studentService = studentService;
        this.roleService = roleService;
        this.userService = userService;
        this.membershipService = membershipService;
        this.trainingSessionService = trainingSessionService;
    }

    /**
     * Registers a new student with the provided details.
     *
     * @param studentRegisterDTO The DTO containing the student's registration details.
     * @return ResponseEntity containing the created StudentDTO.
     */
    @Operation(summary = "Register a new Student", description = "Registers a new student with the provided details.")
    @PostMapping("/register")
    public ResponseEntity<StudentDTO> registerStudent(@RequestBody @Valid StudentRegisterDTO studentRegisterDTO) {

        User user = new User();
        user.setName(studentRegisterDTO.getName());
        user.setSurname(studentRegisterDTO.getSurname());
        user.setEmail(studentRegisterDTO.getEmail());
        user.setPassword(passwordEncoder.encode(studentRegisterDTO.getPassword())); // Encriptar
        user.setPhoneNumber(studentRegisterDTO.getPhoneNumber());
        user.setAddress(studentRegisterDTO.getAddress());
        user.setStatus(StatusValues.valueOf(studentRegisterDTO.getStatus()));
        user.setRegisterDate(LocalDateTime.now());

        Role role = roleService.findRoleByName(studentRegisterDTO.getRoleName());
        user.setRole(role);

        User createdUser = userService.save(user);

        Student student = new Student();
        student.setUser(createdUser);
        student.setDni(studentRegisterDTO.getDni());
        student.setBirthdate(studentRegisterDTO.getBirthdate());
        student.setBelt(studentRegisterDTO.getBelt());
        student.setProgress(studentRegisterDTO.getProgress());
        student.setMedicalReport(studentRegisterDTO.getMedicalReport());
        student.setParentName(studentRegisterDTO.getParentName());

        if (studentService.existsByDni(student.getDni())) {
            throw new InvalidDataException("A student with this DNI already exists");
        }

        if (studentRegisterDTO.getMembershipId() != null) {
            Membership membership = membershipService.findById(studentRegisterDTO.getMembershipId());

            if (membership == null) {
                throw new InvalidDataException("Membership not found with id: " + studentRegisterDTO.getMembershipId());
            }

            student.setMembership(membership);
        }
        Student createdStudent = studentService.save(student);

        StudentDTO response = StudentDTO.fromEntity(studentService.findById(createdStudent.getId()));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Retrieves all students.
     *
     * @return ResponseEntity containing a list of StudentDTOs.
     */
    @Operation(summary = "Get all Students", description = "Retrieves a list of all students.")
    @GetMapping("/getAll")
    public ResponseEntity<List<StudentDTO>> getAllStudents() {
        List<Student> students = studentService.findAll();
        if (students.isEmpty()) return ResponseEntity.noContent().build();

        List<StudentDTO> dtos = students.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    /**
     * Creates a new student with the provided details.
     *
     * @param dto The StudentDTO containing the details of the student to be created.
     * @return ResponseEntity containing the created StudentDTO.
     */
    @Operation(summary = "Create a new Student", description = "Creates a new student with the provided details.")
    @PostMapping("/create")
    public ResponseEntity<StudentDTO> createStudent(@RequestBody StudentDTO dto) {
        Student student = convertToEntity(dto, true);
        Student saved = studentService.save(student);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDTO(saved));
    }

    /**
     * Updates an existing student with the provided details.
     *
     * @param id  The ID of the student to update.
     * @param dto The StudentDTO containing the updated details of the student.
     * @return ResponseEntity containing the updated StudentDTO.
     */
    @Operation(summary = "Update an existing Student", description = "Updates the details of an existing student.")
    @PutMapping("/update/{id}")
    public ResponseEntity<StudentDTO> updateStudent(@PathVariable Integer id, @RequestBody StudentDTO dto) {
        dto.setId(id);
        Student student = convertToEntity(dto, false);
        Student updated = studentService.update(student);
        return ResponseEntity.ok(convertToDTO(updated));
    }

    /**
     * Retrieves a student by their ID.
     *
     * @param id The ID of the student to find.
     * @return ResponseEntity containing the StudentDTO or not found if the ID does not exist.
     */
    @Operation(summary = "Find a Student by ID", description = "Retrieves a student by their ID.")
    @GetMapping("/findById/{id}")
    public ResponseEntity<StudentDTO> getStudentById(@PathVariable Integer id) {
        Student student = studentService.findById(id);
        return ResponseEntity.ok(convertToDTO(student));
    }

    /**
     * Deletes a student by their ID.
     *
     * @param id The ID of the student to delete.
     * @return ResponseEntity with a success message.
     */
    @Operation(summary = "Delete a Student", description = "Deletes a student by their ID.")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteStudent(@PathVariable Integer id) {
        studentService.deleteById(id);
        return ResponseEntity.ok("Student deleted successfully");
    }

    /**
     * Adds a history record to a student by their ID.
     *
     * @param id      The ID of the student to whom the history will be added.
     * @param history The StudentHistory object containing the history details to be added.
     * @return ResponseEntity with a success message.
     */
    @Operation(summary = "Add a Student History", description = "Adds a history record to a student by their ID.")
    @PostMapping("/addStudentHistory/{id}")
    public ResponseEntity<String> addStudentHistory(@PathVariable Integer id, @RequestBody StudentHistory history) {
        Student student = studentService.findById(id);
        studentService.addStudentHistoryToStudent(student, history);
        return ResponseEntity.ok("Student history added successfully");
    }

    /**
     * Removes a history record from a student by their ID.
     *
     * @param id The ID of the history record to remove.
     * @return ResponseEntity with a success message.
     */
    @Operation(summary = "Remove a Student History", description = "Removes a history record from a student.")
    @DeleteMapping("/removeStudentHistory/{id}")
    public ResponseEntity<String> removeStudentHistory(@PathVariable Integer id) {
        studentService.deleteStudentHistory(id);
        return ResponseEntity.ok("Student history removed successfully");
    }

    /**
     * Adds a membership to a student by their ID.
     *
     * @param id         The ID of the student to whom the membership will be added.
     * @param membership The Membership object containing the membership details to be added.
     * @return ResponseEntity with a success message.
     */
    @Operation(summary = "Add a Membership to a Student", description = "Adds a membership to a student by their ID.")
    @PostMapping("/addMembershipToStudent/{id}")
    public ResponseEntity<String> addMembershipToStudent(@PathVariable Integer id, @RequestBody Membership membership) {
        Student student = studentService.findById(id);
        studentService.addStudentToMembership(student, membership);
        return ResponseEntity.ok("Membership added to student successfully");
    }

    /**
     * Removes a membership from a student by their ID.
     *
     * @param id The ID of the student from whom the membership will be removed.
     * @return ResponseEntity with a success message.
     */
    @Operation(summary = "Remove a Membership from a Student", description = "Removes a membership from a student by their ID.")
    @DeleteMapping("/removeMembershipFromStudent/{id}")
    public ResponseEntity<String> removeMembershipFromStudent(@PathVariable Integer id) {
        studentService.deleteStudentMembership(id);
        return ResponseEntity.ok("Membership removed from student successfully");
    }

    /**
     * Adds an assistance record to a student by their ID.
     *
     * @param id            The ID of the student to whom the assistance will be added.
     * @param assistanceDTO The AssistanceDTO containing the details of the assistance to be added.
     * @return ResponseEntity with a success message.
     */
    @Operation(summary = "Add an Assistance to a Student", description = "Adds an assistance record to a student by their ID.")
    @PostMapping("/addAssistanceToStudent/{id}")
    public ResponseEntity<String> addAssistanceToStudent(@PathVariable Integer id, @RequestBody AssistanceDTO assistanceDTO) {
        Student student = studentService.findById(id);
        TrainingSession session = trainingSessionService.findById(assistanceDTO.getSessionId());
        Assistance assistance = assistanceDTO.toEntity(student, session);
        studentService.addAssistanceToStudent(student, assistance);
        return ResponseEntity.ok("Assistance added to student successfully");
    }

    /**
     * Removes an assistance record from a student by their ID.
     *
     * @param studentId    The ID of the student from whom the assistance will be removed.
     * @param assistanceId The ID of the assistance record to remove.
     * @return ResponseEntity with a success message.
     */
    @Operation(summary = "Remove an Assistance from a Student", description = "Removes an assistance record from a student by their ID.")
    @DeleteMapping("/deleteAssistanceFromStudent/{id}")
    public ResponseEntity<String> deleteAssistanceFromStudent(@PathVariable Integer studentId, @PathVariable Integer assistanceId) {
        studentService.deleteSingleAssistanceFromStudent(studentId, assistanceId);
        return ResponseEntity.ok("Assistance removed from student successfully");
    }

    /**
     * Adds a training group to a student by their ID.
     *
     * @param id    The ID of the student to whom the training group will be added.
     * @param group The TrainingGroup object containing the details of the group to be added.
     * @return ResponseEntity with a success message.
     */
    @Operation(summary = "Add a Training Group to a Student", description = "Adds a training group to a student by their ID.")
    @PostMapping("/addGroupToStudent/{id}")
    public ResponseEntity<String> addGroupToStudent(@PathVariable Integer id, @RequestBody TrainingGroup group) {
        studentService.addGroupToStudent(id, group);
        return ResponseEntity.ok("Training group added to student successfully");
    }

    /**
     * Updates a student's membership by their ID.
     *
     * @param studentId    The ID of the student whose membership will be updated.
     * @param membershipId The ID of the membership to assign to the student.
     * @return ResponseEntity indicating the result of the operation.
     */
    @Operation(summary = "Update a student's membership")
    @PutMapping("/updateMembership/{studentId}")
    public ResponseEntity<Void> updateMembership(
            @PathVariable Integer studentId,
            @RequestParam Integer membershipId) {

        Student student = studentService.findById(studentId);
        Membership membership = membershipService.findById(membershipId);

        student.setMembership(membership);
        studentService.update(student);

        return ResponseEntity.ok().build();
    }



    /* ----------------- Utils ------------------  */

    /**
     * Converts a Student entity to a StudentDTO.
     *
     * @param student The Student entity to convert.
     * @return The converted StudentDTO.
     */
    private StudentDTO convertToDTO(Student student) {
        return StudentDTO.fromEntity(student);
    }


    /**
     * Converts a StudentDTO to a Student entity.
     *
     * @param dto      The StudentDTO to convert.
     * @param isCreate Indicates if the conversion is for creating a new student.
     * @return The converted Student entity.
     */
    private Student convertToEntity(StudentDTO dto, boolean isCreate) {
        if (dto == null || dto.getUser() == null) {
            throw new InvalidDataException("User data is required");
        }

        User user;

        if (isCreate) {
            user = new User();
            user.setRegisterDate(dto.getUser().getRegisterDate() != null ? dto.getUser().getRegisterDate() : LocalDateTime.now());
        } else {
            Student existing = studentService.findById(dto.getId());
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
                throw new InvalidDataException("Password is required for new student");
            }
            user.setPassword(passwordEncoder.encode(dto.getUser().getPassword()));
        } else {
            if (dto.getUser().getPassword() != null && !dto.getUser().getPassword().isBlank()) {
                user.setPassword(passwordEncoder.encode(dto.getUser().getPassword()));
            }
        }

        Student student = isCreate ? new Student() : studentService.findById(dto.getId());
        student.setUser(user);
        student.setDni(dto.getDni());
        student.setBirthdate(dto.getBirthdate());
        student.setBelt(dto.getBelt());
        student.setProgress(dto.getProgress());
        student.setMedicalReport(dto.getMedicalReport());
        student.setParentName(dto.getParentName());

        return student;
    }
}
