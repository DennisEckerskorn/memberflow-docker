package com.denniseckerskorn.entities.class_managment;

import com.denniseckerskorn.entities.user_managment.users.Student;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "ASSISTANCE")
public class Assistance {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "fk_training_session", nullable = false)
    private TrainingSession trainingSession;

    @ManyToOne
    @JoinColumn(name = "fk_student", nullable = false)
    private Student student;

    @Column(name = "date_time", nullable = false)
    private LocalDateTime date;

    public Assistance() {

    }

    public Integer getId() {
        return id;
    }

    public TrainingSession getTrainingSession() {
        return trainingSession;
    }

    public Student getStudent() {
        return student;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setTrainingSession(TrainingSession trainingSession) {
        this.trainingSession = trainingSession;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Assistance that = (Assistance) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Assistance{" +
                "id=" + id +
                ", trainingSessionId=" + (trainingSession != null ? trainingSession.getId() : "null") +
                ", studentId=" + (student != null ? student.getId() : "null") +
                ", date=" + date +
                '}';
    }
}
