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

@RestController
@RequestMapping("/api/v1/students")
@Tag(name = "Student Management", description = "Operations related to student management")
public class StudentController {

    private final StudentService studentService;
    private final RoleService roleService;
    private final UserService userService;
    private final MembershipService membershipService;
    private final TrainingSessionService trainingSessionService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public StudentController(StudentService studentService, RoleService roleService, UserService userService, MembershipService membershipService, TrainingSessionService trainingSessionService) {
        this.studentService = studentService;
        this.roleService = roleService;
        this.userService = userService;
        this.membershipService = membershipService;
        this.trainingSessionService = trainingSessionService;
    }

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

    @Operation(summary = "Create a new Student", description = "Creates a new student with the provided details.")
    @PostMapping("/create")
    public ResponseEntity<StudentDTO> createStudent(@RequestBody StudentDTO dto) {
        Student student = convertToEntity(dto, true);
        Student saved = studentService.save(student);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDTO(saved));
    }

    @Operation(summary = "Update an existing Student", description = "Updates the details of an existing student.")
    @PutMapping("/update/{id}")
    public ResponseEntity<StudentDTO> updateStudent(@PathVariable Integer id, @RequestBody StudentDTO dto) {
        dto.setId(id);
        Student student = convertToEntity(dto, false);
        Student updated = studentService.update(student);
        return ResponseEntity.ok(convertToDTO(updated));
    }

    @Operation(summary = "Find a Student by ID", description = "Retrieves a student by their ID.")
    @GetMapping("/findById/{id}")
    public ResponseEntity<StudentDTO> getStudentById(@PathVariable Integer id) {
        Student student = studentService.findById(id);
        return ResponseEntity.ok(convertToDTO(student));
    }

    @Operation(summary = "Delete a Student", description = "Deletes a student by their ID.")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteStudent(@PathVariable Integer id) {
        studentService.deleteById(id);
        return ResponseEntity.ok("Student deleted successfully");
    }

    @Operation(summary = "Add a Student History", description = "Adds a history record to a student by their ID.")
    @PostMapping("/addStudentHistory/{id}")
    public ResponseEntity<String> addStudentHistory(@PathVariable Integer id, @RequestBody StudentHistory history) {
        Student student = studentService.findById(id);
        studentService.addStudentHistoryToStudent(student, history);
        return ResponseEntity.ok("Student history added successfully");
    }

    @Operation(summary = "Remove a Student History", description = "Removes a history record from a student.")
    @DeleteMapping("/removeStudentHistory/{id}")
    public ResponseEntity<String> removeStudentHistory(@PathVariable Integer id) {
        studentService.deleteStudentHistory(id);
        return ResponseEntity.ok("Student history removed successfully");
    }

    @Operation(summary = "Add a Membership to a Student", description = "Adds a membership to a student by their ID.")
    @PostMapping("/addMembershipToStudent/{id}")
    public ResponseEntity<String> addMembershipToStudent(@PathVariable Integer id, @RequestBody Membership membership) {
        Student student = studentService.findById(id);
        studentService.addStudentToMembership(student, membership);
        return ResponseEntity.ok("Membership added to student successfully");
    }

    @Operation(summary = "Remove a Membership from a Student", description = "Removes a membership from a student by their ID.")
    @DeleteMapping("/removeMembershipFromStudent/{id}")
    public ResponseEntity<String> removeMembershipFromStudent(@PathVariable Integer id) {
        studentService.deleteStudentMembership(id);
        return ResponseEntity.ok("Membership removed from student successfully");
    }

    @Operation(summary = "Add an Assistance to a Student", description = "Adds an assistance record to a student by their ID.")
    @PostMapping("/addAssistanceToStudent/{id}")
    public ResponseEntity<String> addAssistanceToStudent(@PathVariable Integer id, @RequestBody AssistanceDTO assistanceDTO) {
        Student student = studentService.findById(id);
        TrainingSession session = trainingSessionService.findById(assistanceDTO.getSessionId());
        Assistance assistance = assistanceDTO.toEntity(student, session);
        studentService.addAssistanceToStudent(student, assistance);
        return ResponseEntity.ok("Assistance added to student successfully");
    }

    @Operation(summary = "Remove an Assistance from a Student", description = "Removes an assistance record from a student by their ID.")
    @DeleteMapping("/deleteAssistanceFromStudent/{id}")
    public ResponseEntity<String> deleteAssistanceFromStudent(@PathVariable Integer studentId, @PathVariable Integer assistanceId) {
        studentService.deleteSingleAssistanceFromStudent(studentId, assistanceId);
        return ResponseEntity.ok("Assistance removed from student successfully");
    }

    @Operation(summary = "Add a Training Group to a Student", description = "Adds a training group to a student by their ID.")
    @PostMapping("/addGroupToStudent/{id}")
    public ResponseEntity<String> addGroupToStudent(@PathVariable Integer id, @RequestBody TrainingGroup group) {
        studentService.addGroupToStudent(id, group);
        return ResponseEntity.ok("Training group added to student successfully");
    }

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



    /* ----------------- Mapping Methods ------------------  */

    private StudentDTO convertToDTO(Student student) {
        return StudentDTO.fromEntity(student);
    }


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
