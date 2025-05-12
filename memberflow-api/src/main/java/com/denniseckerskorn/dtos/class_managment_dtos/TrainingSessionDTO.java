package com.denniseckerskorn.dtos.class_managment_dtos;

import com.denniseckerskorn.dtos.class_managment_dtos.TrainingGroupDTO;
import com.denniseckerskorn.entities.class_managment.TrainingSession;
import com.denniseckerskorn.enums.StatusValues;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class TrainingSessionDTO {

    private Integer id;

    @NotNull
    private Integer trainingGroupId;

    private LocalDateTime date;

    @NotNull
    private StatusValues status;

    public Integer getId() {
        return id;
    }

    public Integer getTrainingGroupId() {
        return trainingGroupId;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public StatusValues getStatus() {
        return status;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setTrainingGroupId(Integer trainingGroupId) {
        this.trainingGroupId = trainingGroupId;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public void setStatus(StatusValues status) {
        this.status = status;
    }

    public static TrainingSessionDTO fromEntity(TrainingSession session) {
        TrainingSessionDTO dto = new TrainingSessionDTO();
        dto.setId(session.getId());
        dto.setTrainingGroupId(session.getTrainingGroup().getId());
        dto.setDate(session.getDate());
        dto.setStatus(session.getStatus());
        return dto;
    }

    public TrainingSession toEntity() {
        TrainingSession session = new TrainingSession();
        session.setId(this.id);
        session.setDate(this.date);
        session.setStatus(this.status);
        return session;
    }
}
