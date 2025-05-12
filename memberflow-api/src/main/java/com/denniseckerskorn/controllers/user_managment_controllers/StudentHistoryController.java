package com.denniseckerskorn.controllers.user_managment_controllers;

import com.denniseckerskorn.dtos.user_managment_dtos.StudentHistoryDTO;
import com.denniseckerskorn.entities.user_managment.StudentHistory;
import com.denniseckerskorn.entities.user_managment.users.Student;
import com.denniseckerskorn.services.user_managment_services.StudentHistoryService;
import com.denniseckerskorn.services.user_managment_services.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/student-history")
@Tag(name = "Student History Management", description = "Operations related to student history")
public class StudentHistoryController {

    private final StudentHistoryService studentHistoryService;
    private final StudentService studentService;

    public StudentHistoryController(StudentHistoryService studentHistoryService, StudentService studentService) {
        this.studentHistoryService = studentHistoryService;
        this.studentService = studentService;
    }

    @Operation(summary = "Get all student histories", description = "Retrieve a list of all student histories")
    @GetMapping("/getAll")
    public ResponseEntity<List<StudentHistoryDTO>> getAll() {
        List<StudentHistory> histories = studentHistoryService.findAll();
        if (histories.isEmpty()) return ResponseEntity.noContent().build();

        List<StudentHistoryDTO> dtos = histories.stream().map(this::toDTO).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @Operation(summary = "find student history by ID", description = "Retrieve a student history by its ID")
    @GetMapping("/findById/{id}")
    public ResponseEntity<StudentHistoryDTO> findById(@PathVariable Integer id) {
        StudentHistory history = studentHistoryService.findById(id);
        return ResponseEntity.ok(toDTO(history));
    }

    @Operation(summary = "Create a new student history", description = "Create a new student history")
    @PostMapping("/create")
    public ResponseEntity<StudentHistoryDTO> create(@RequestBody StudentHistoryDTO dto) {
        StudentHistory history = toEntity(dto, false);
        StudentHistory saved = studentHistoryService.save(history);
        return ResponseEntity.status(201).body(toDTO(saved));
    }

    @Operation(summary = "Update an existing student history", description = "Update an existing student history")
    @PutMapping("/update/{id}")
    public ResponseEntity<StudentHistoryDTO> update(@PathVariable Integer id, @RequestBody StudentHistoryDTO dto) {
        dto.setId(id);
        StudentHistory history = toEntity(dto, true);
        StudentHistory updated = studentHistoryService.update(history);
        return ResponseEntity.ok(toDTO(updated));
    }

    @Operation(summary = "Delete a student history", description = "Delete a student history by its ID")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Integer id) {
        studentHistoryService.deleteById(id);
        return ResponseEntity.ok("Student history deleted successfully.");
    }

    // -------------------- Mapping --------------------

    private StudentHistoryDTO toDTO(StudentHistory history) {
        return new StudentHistoryDTO(
                history.getId(),
                history.getStudent() != null ? history.getStudent().getId() : null,
                history.getEventDate(),
                history.getEventType(),
                history.getDescription()
        );
    }

    private StudentHistory toEntity(StudentHistoryDTO dto, boolean includeExistingStudent) {
        StudentHistory history = (dto.getId() != null)
                ? studentHistoryService.findById(dto.getId())
                : new StudentHistory();

        history.setEventDate(dto.getEventDate());
        history.setEventType(dto.getEventType());
        history.setDescription(dto.getDescription());

        if (!includeExistingStudent && dto.getStudentId() != null) {
            Student student = studentService.findById(dto.getStudentId());
            history.setStudent(student);
        }

        return history;
    }
}
