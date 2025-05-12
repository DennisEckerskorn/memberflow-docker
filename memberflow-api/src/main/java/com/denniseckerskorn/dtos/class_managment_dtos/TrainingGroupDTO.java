package com.denniseckerskorn.dtos.class_managment_dtos;

import com.denniseckerskorn.entities.class_managment.TrainingGroup;
import com.denniseckerskorn.entities.user_managment.users.Student;
import com.denniseckerskorn.entities.user_managment.users.Teacher;

import com.fasterxml.jackson.annotation.JsonFormat;


import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

public class TrainingGroupDTO {

    private Integer id;
    private String name;
    private String level;
    private LocalDateTime schedule;
    private Integer teacherId;
    private Set<Integer> studentIds;

    public TrainingGroupDTO() {
    }

    public TrainingGroupDTO(TrainingGroup group) {
        this.id = group.getId();
        this.name = group.getName();
        this.level = group.getLevel();
        this.schedule = group.getSchedule();
        this.teacherId = group.getTeacher() != null ? group.getTeacher().getId() : null;
        this.studentIds = group.getStudents().stream().map(Student::getId).collect(Collectors.toSet());
    }

    public TrainingGroup toEntity(Teacher teacher, Set<Student> students) {
        TrainingGroup group = new TrainingGroup();
        group.setId(this.id);
        group.setName(this.name);
        group.setLevel(this.level);
        group.setSchedule(this.schedule);
        group.setTeacher(teacher);
        group.setStudents(students);
        return group;
    }

    public static TrainingGroupDTO fromEntity(TrainingGroup group) {
        if (group == null) return null;

        TrainingGroupDTO dto = new TrainingGroupDTO();
        dto.setId(group.getId());
        dto.setName(group.getName());
        dto.setLevel(group.getLevel());
        dto.setSchedule(group.getSchedule());
        dto.setTeacherId(group.getTeacher() != null ? group.getTeacher().getId() : null);
        dto.setStudentIds(group.getStudents() != null
                ? group.getStudents().stream().map(Student::getId).collect(Collectors.toSet())
                : null);
        return dto;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public LocalDateTime getSchedule() {
        return schedule;
    }

    public void setSchedule(LocalDateTime schedule) {
        this.schedule = schedule;
    }

    public Integer getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Integer teacherId) {
        this.teacherId = teacherId;
    }

    public Set<Integer> getStudentIds() {
        return studentIds;
    }

    public void setStudentIds(Set<Integer> studentIds) {
        this.studentIds = studentIds;
    }
}
