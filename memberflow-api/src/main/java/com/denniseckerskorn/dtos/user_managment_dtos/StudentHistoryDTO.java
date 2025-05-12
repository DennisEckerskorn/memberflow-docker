package com.denniseckerskorn.dtos.user_managment_dtos;

import java.time.LocalDate;

public class StudentHistoryDTO {
    private Integer id;
    private Integer studentId;
    private LocalDate eventDate;
    private String eventType;
    private String description;

    public StudentHistoryDTO() {
    }

    public StudentHistoryDTO(Integer id, Integer studentId, LocalDate eventDate, String eventType, String description) {
        this.id = id;
        this.studentId = studentId;
        this.eventDate = eventDate;
        this.eventType = eventType;
        this.description = description;
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

    public LocalDate getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDate eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
