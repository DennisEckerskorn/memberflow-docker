package com.denniseckerskorn.entities.class_managment;

import com.denniseckerskorn.entities.user_managment.users.Student;
import com.denniseckerskorn.entities.user_managment.users.Teacher;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "TRAINING_GROUPS")
public class TrainingGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "fk_teacher", nullable = false)
    private Teacher teacher;

    @Column(name = "name", nullable = false, length = 45)
    private String name;

    @Column(name = "level", length = 45)
    private String level;

    @Column(name = "schedule", nullable = false)
    private LocalDateTime schedule;

    @OneToMany(mappedBy = "trainingGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TrainingSession> trainingSessions = new HashSet<>();

    @ManyToMany(mappedBy = "trainingGroups")
    private Set<Student> students = new HashSet<>();

    public TrainingGroup() {

    }

    public Integer getId() {
        return id;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public String getName() {
        return name;
    }

    public String getLevel() {
        return level;
    }

    public LocalDateTime getSchedule() {
        return schedule;
    }

    public Set<TrainingSession> getTrainingSessions() {
        return trainingSessions;
    }

    public Set<Student> getStudents() {
        return students;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public void setSchedule(LocalDateTime schedule) {
        this.schedule = schedule;
    }

    public void setTrainingSessions(Set<TrainingSession> trainingSessions) {
        this.trainingSessions = trainingSessions;
    }

    public void setStudents(Set<Student> students) {
        this.students = students;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TrainingGroup group = (TrainingGroup) o;
        return Objects.equals(id, group.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Group{" +
                "id=" + id +
                ", teacherId=" + (teacher != null ? teacher.getId() : "null") +
                ", name='" + name + '\'' +
                ", level='" + (level != null ? level : "null") + '\'' +
                ", schedule='" + schedule + '\'' +
                '}';
    }
}
