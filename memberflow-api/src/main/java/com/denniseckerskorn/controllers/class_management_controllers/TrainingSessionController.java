package com.denniseckerskorn.controllers.class_management_controllers;

import com.denniseckerskorn.dtos.class_managment_dtos.TrainingSessionDTO;
import com.denniseckerskorn.entities.class_managment.TrainingGroup;
import com.denniseckerskorn.entities.class_managment.TrainingSession;

import com.denniseckerskorn.services.class_managment_services.TrainingGroupService;
import com.denniseckerskorn.services.class_managment_services.TrainingSessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/training-sessions")
@Tag(name = "Training Session Management", description = "Operations related to training session management")
public class TrainingSessionController {
    private final TrainingSessionService trainingSessionService;
    private final TrainingGroupService trainingGroupService;

    public TrainingSessionController(TrainingSessionService trainingSessionService, TrainingGroupService trainingGroupService) {
        this.trainingSessionService = trainingSessionService;
        this.trainingGroupService = trainingGroupService;
    }

    @Operation(summary = "Find a training session by ID", description = "Retrieve a training session by its ID")
    @GetMapping("findById/{id}")
    public TrainingSessionDTO findTrainingSessionById(@PathVariable Integer id) {
        return TrainingSessionDTO.fromEntity(trainingSessionService.findById(id));
    }

    @Operation(summary = "Get all training sessions", description = "Retrieve a list of all training sessions")
    @GetMapping("/getAll")
    public List<TrainingSessionDTO> getAll() {
        return trainingSessionService.findAll()
                .stream()
                .map(TrainingSessionDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Get all training sessions by group ID", description = "Retrieve a list of all training sessions by group ID")
    @DeleteMapping("delete/{id}")
    public void delete(@PathVariable Integer id) {
        trainingSessionService.deleteById(id);
    }
}
