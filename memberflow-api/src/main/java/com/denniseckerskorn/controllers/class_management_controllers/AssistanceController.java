package com.denniseckerskorn.controllers.class_management_controllers;


import com.denniseckerskorn.dtos.class_managment_dtos.AssistanceDTO;
import com.denniseckerskorn.entities.class_managment.Assistance;
import com.denniseckerskorn.entities.class_managment.TrainingSession;
import com.denniseckerskorn.entities.user_managment.users.Student;
import com.denniseckerskorn.exceptions.BadRequestException;
import com.denniseckerskorn.services.class_managment_services.AssistanceService;
import com.denniseckerskorn.services.class_managment_services.TrainingSessionService;
import com.denniseckerskorn.services.user_managment_services.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/assistances")
@Tag(name = "Assistance Management", description = "Operations related to assistance management")
public class AssistanceController {

    private final AssistanceService assistanceService;
    private final StudentService studentService;
    private final TrainingSessionService trainingSessionService;

    public AssistanceController(
            AssistanceService assistanceService,
            StudentService studentService,
            TrainingSessionService trainingSessionService
    ) {
        this.assistanceService = assistanceService;
        this.studentService = studentService;
        this.trainingSessionService = trainingSessionService;
    }

    @Operation(summary = "Create a new assistance record")
    @PostMapping("/create")
    public ResponseEntity<AssistanceDTO> create(@RequestBody AssistanceDTO dto) {
        Student student = studentService.findById(dto.getStudentId());
        TrainingSession session = trainingSessionService.findById(dto.getSessionId());

        if (!student.getTrainingGroups().contains(session.getTrainingGroup())) {
            throw new BadRequestException("The student does not belong to the selected training group.");
        }

        Assistance assistance = dto.toEntity(student, session);
        Assistance saved = assistanceService.save(assistance);

        return ResponseEntity.ok(AssistanceDTO.fromEntity(saved));
    }


    @Operation(summary = "Update an existing assistance record", description = "Update an existing assistance record")
    @PutMapping("/update")
    public ResponseEntity<AssistanceDTO> update(@RequestBody AssistanceDTO dto) {
        Assistance existing = assistanceService.findById(dto.getId());
        existing.setDate(dto.getDate());
        existing.setStudent(studentService.findById(dto.getStudentId()));
        existing.setTrainingSession(trainingSessionService.findById(dto.getSessionId()));
        Assistance updated = assistanceService.save(existing);
        return ResponseEntity.ok(AssistanceDTO.fromEntity(updated));
    }

    @Operation(summary = "Get all assistance records", description = "Retrieve a list of all assistance records")
    @GetMapping("/getAll")
    public ResponseEntity<List<AssistanceDTO>> getAll() {
        List<AssistanceDTO> list = assistanceService.findAll()
                .stream()
                .map(AssistanceDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @Operation(summary = "Get assistance records by student ID", description = "Retrieve assistance records for a specific student")
    @GetMapping("/getById/{id}")
    public ResponseEntity<AssistanceDTO> getAssistanceById(@PathVariable Integer id) {
        Assistance assistance = assistanceService.findById(id);
        return ResponseEntity.ok(AssistanceDTO.fromEntity(assistance));
    }

    @Operation(summary = "Delete an assistance record by ID")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        assistanceService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
