package com.denniseckerskorn.controllers.class_management_controllers;

import com.denniseckerskorn.dtos.class_managment_dtos.TrainingGroupDTO;
import com.denniseckerskorn.entities.class_managment.TrainingGroup;
import com.denniseckerskorn.entities.class_managment.TrainingSession;
import com.denniseckerskorn.entities.user_managment.users.Student;
import com.denniseckerskorn.entities.user_managment.users.Teacher;

import com.denniseckerskorn.enums.StatusValues;
import com.denniseckerskorn.exceptions.EntityNotFoundException;
import com.denniseckerskorn.exceptions.InvalidDataException;
import com.denniseckerskorn.services.class_managment_services.TrainingGroupService;
import com.denniseckerskorn.services.class_managment_services.TrainingSessionService;
import com.denniseckerskorn.services.user_managment_services.StudentService;
import com.denniseckerskorn.services.user_managment_services.TeacherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Controller for managing training groups.
 * Provides endpoints for creating, updating, retrieving, and deleting training groups.
 */
@RestController
@RequestMapping("/api/v1/training-groups")
@Tag(name = "Training Group Management", description = "Operations related to training group management")
public class TrainingGroupController {

    private final TrainingGroupService trainingGroupService;
    private final TeacherService teacherService;
    private final StudentService studentService;
    private final TrainingSessionService trainingSessionService;

    /**
     * Constructor for TrainingGroupController.
     *
     * @param trainingGroupService   Service for handling training group records.
     * @param teacherService         Service for handling teacher records.
     * @param studentService         Service for handling student records.
     * @param trainingSessionService Service for handling training session records.
     */
    public TrainingGroupController(TrainingGroupService trainingGroupService, TeacherService teacherService, StudentService studentService, TrainingSessionService trainingSessionService) {
        this.trainingGroupService = trainingGroupService;
        this.teacherService = teacherService;
        this.studentService = studentService;
        this.trainingSessionService = trainingSessionService;
    }

    /**
     * Creates a new training group with a teacher.
     *
     * @param dto The TrainingGroupDTO containing the details of the group to be created.
     * @return ResponseEntity containing the created TrainingGroupDTO.
     */
    @Operation(summary = "Create a training group with a teacher")
    @PostMapping("/create")
    public ResponseEntity<TrainingGroupDTO> createGroup(@RequestBody TrainingGroupDTO dto) {
        Teacher teacher = teacherService.findById(dto.getTeacherId());

        TrainingGroup group = new TrainingGroup();
        group.setName(dto.getName());
        group.setLevel(dto.getLevel());
        group.setSchedule(dto.getSchedule());
        group.setTeacher(teacher);
        TrainingGroup createdGroup = trainingGroupService.save(group);

        int months = dto.getRecurrenceMonths() != null && dto.getRecurrenceMonths() > 0
                ? dto.getRecurrenceMonths()
                : 1;
        trainingSessionService.generateRecurringSession(createdGroup, months);

        return ResponseEntity.status(HttpStatus.CREATED).body(new TrainingGroupDTO(createdGroup));
    }

    /**
     * Assigns a student to a training group.
     *
     * @param groupId   The ID of the training group.
     * @param studentId The ID of the student to be assigned.
     * @return ResponseEntity indicating the result of the operation.
     */
    @Operation(summary = "Assign a student to a group")
    @PutMapping("/assign-student")
    public ResponseEntity<Void> assignStudent(@RequestParam Integer groupId, @RequestParam Integer studentId) {
        TrainingGroup group = trainingGroupService.findById(groupId);
        Student student = studentService.findById(studentId);

        trainingGroupService.addStudentToGroup(group, student);
        return ResponseEntity.ok().build();
    }

    /**
     * Removes a student from a training group.
     *
     * @param groupId   The ID of the training group.
     * @param studentId The ID of the student to be removed.
     * @return ResponseEntity indicating the result of the operation.
     */
    @Operation(summary = "Remove a student from a group")
    @PutMapping("/remove-student")
    public ResponseEntity<Void> removeStudent(@RequestParam Integer groupId, @RequestParam Integer studentId) {
        TrainingGroup group = trainingGroupService.findById(groupId);
        Student student = studentService.findById(studentId);

        trainingGroupService.removeStudentFromGroup(group, student);
        return ResponseEntity.ok().build();
    }

    /**
     * Updates an existing training group.
     *
     * @param id  The ID of the training group to update.
     * @param dto The TrainingGroupDTO containing the updated details of the group.
     * @return ResponseEntity containing the updated TrainingGroupDTO.
     */
    @Operation(summary = "Updates a training-group by its ID", description = "Update a training group with the specified ID")
    @Transactional
    @PutMapping("/update/{id}")
    public ResponseEntity<TrainingGroupDTO> update(@PathVariable Integer id, @RequestBody TrainingGroupDTO dto) {
        dto.setId(id);

        Teacher teacher = teacherService.findById(dto.getTeacherId());

        Set<Integer> studentIds = dto.getStudentIds() != null ? dto.getStudentIds() : Collections.emptySet();

        Set<Student> students = studentIds.stream()
                .map(studentService::findById)
                .collect(Collectors.toSet());

        TrainingGroup updatedGroup = trainingGroupService.update(dto.toEntity(teacher, students));

        updatedGroup.getTrainingSessions().stream().findFirst().ifPresent(session -> {
            session.setDate(updatedGroup.getSchedule());
            trainingSessionService.save(session);
        });

        return ResponseEntity.ok(new TrainingGroupDTO(updatedGroup));
    }

    /**
     * Finds a training group by its ID.
     *
     * @param id The ID of the training group to retrieve.
     * @return ResponseEntity containing the TrainingGroupDTO if found, or 404 Not Found if not found.
     */
    @Operation(summary = "Find a training group by ID", description = "Retrieve a training group with the specified ID")
    @GetMapping("findById/{id}")
    public ResponseEntity<TrainingGroupDTO> findGroupById(@PathVariable Integer id) {
        return ResponseEntity.ok(new TrainingGroupDTO(trainingGroupService.findById(id)));
    }

    /**
     * Retrieves all training groups.
     *
     * @return ResponseEntity containing a list of TrainingGroupDTOs.
     */
    @Operation(summary = "Get all training groups", description = "Retrieve a list of all training groups")
    @GetMapping("/getAll")
    public ResponseEntity<List<TrainingGroupDTO>> getAll() {
        return ResponseEntity.ok(
                trainingGroupService.findAll().stream()
                        .map(TrainingGroupDTO::new)
                        .collect(Collectors.toList())
        );
    }

    /**
     * Deletes a training group by its ID.
     *
     * @param id The ID of the training group to delete.
     * @return ResponseEntity indicating the result of the deletion.
     */
    @Operation(summary = "Delete a training group by ID", description = "Delete a training group with the specified ID")
    @Transactional
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        TrainingGroup group = trainingGroupService.findById(id);
        if (group == null) {
            throw new EntityNotFoundException("Training group not found with ID: " + id);
        }

        for (TrainingSession session : group.getTrainingSessions()) {
            trainingSessionService.deleteAllAssistancesBySession(session.getId());
        }

        for (TrainingSession session : group.getTrainingSessions()) {
            trainingSessionService.deleteById(session.getId());
        }

        Set<Student> students = group.getStudents();
        if (students != null) {
            for (Student student : students) {
                student.getTrainingGroups().remove(group);
            }
            group.getStudents().clear();
        }

        trainingGroupService.update(group);

        trainingGroupService.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    /**
     * Generates recurring training sessions for a group.
     *
     * @param dto The TrainingGroupDTO containing the group ID and recurrence details.
     * @return ResponseEntity containing the updated TrainingGroupDTO.
     */
    @Operation(summary = "Generate recurring training sessions for a group", description = "Generates recurring training sessions for a specified number of months")
    @Transactional
    @PostMapping("/generate-recurring-sessions")
    public ResponseEntity<TrainingGroupDTO> generateRecurringSessions(@RequestBody TrainingGroupDTO dto) {
        TrainingGroup group = trainingGroupService.findById(dto.getId());

        int months = (dto.getRecurrenceMonths() != null && dto.getRecurrenceMonths() > 0)
                ? dto.getRecurrenceMonths()
                : 1;

        trainingSessionService.generateRecurringSession(group, months);

        return ResponseEntity.ok(new TrainingGroupDTO(group));
    }
}
