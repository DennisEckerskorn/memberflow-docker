package com.denniseckerskorn.dtos.class_managment_dtos;

import com.denniseckerskorn.entities.class_managment.Assistance;
import com.denniseckerskorn.entities.class_managment.TrainingSession;
import com.denniseckerskorn.entities.user_managment.users.Student;

import java.time.LocalDateTime;

public class AssistanceDTO {

    private Integer id;
    private Integer studentId;
    private Integer sessionId;
    private LocalDateTime date;

    public AssistanceDTO() {
    }

    public AssistanceDTO(Integer id, Integer studentId, Integer sessionId, LocalDateTime date) {
        this.id = id;
        this.studentId = studentId;
        this.sessionId = sessionId;
        this.date = date;
    }

    public static AssistanceDTO fromEntity(Assistance assistance) {
        return new AssistanceDTO(
                assistance.getId(),
                assistance.getStudent() != null ? assistance.getStudent().getId() : null,
                assistance.getTrainingSession() != null ? assistance.getTrainingSession().getId() : null,
                assistance.getDate()
        );
    }

    public Assistance toEntity(Student student, TrainingSession session) {
        Assistance assistance = new Assistance();
        assistance.setId(this.id);
        assistance.setStudent(student);
        assistance.setTrainingSession(session);
        assistance.setDate(this.date);
        return assistance;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public Integer getSessionId() {
        return sessionId;
    }

    public void setSessionId(Integer sessionId) {
        this.sessionId = sessionId;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
