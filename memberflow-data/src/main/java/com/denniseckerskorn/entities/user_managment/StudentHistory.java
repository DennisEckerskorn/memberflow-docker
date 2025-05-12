package com.denniseckerskorn.entities.user_managment;

import com.denniseckerskorn.entities.user_managment.users.Student;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "STUDENT_HISTORY")
public class StudentHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "fk_student", nullable = false)
    private Student student;

    @Column(name = "event_date")
    private LocalDate eventDate;

    @Column(name = "event_type")
    private String eventType;

    @Column(name = "description")
    private String description;

    public StudentHistory() {

    }

    public Integer getId() {
        return id;
    }

    public Student getStudent() {
        return student;
    }

    public LocalDate getEventDate() {
        return eventDate;
    }

    public String getEventType() {
        return eventType;
    }

    public String getDescription() {
        return description;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public void setEventDate(LocalDate eventDate) {
        this.eventDate = eventDate;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StudentHistory that = (StudentHistory) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "StudentHistory{" +
                "id=" + (id != null ? id : "null") +
                ", student=" + (student != null ? "Student{id=" + student.getId() + "}" : "null") +
                ", eventDate=" + (eventDate != null ? eventDate : "null") +
                ", eventType='" + (eventType != null ? eventType : "null") + '\'' +
                ", description='" + (description != null ? description : "null") + '\'' +
                '}';
    }
}
