package com.denniseckerskorn.entities.class_managment;

import com.denniseckerskorn.entities.user_managment.users.Student;
import com.denniseckerskorn.enums.MembershipTypeValues;
import com.denniseckerskorn.enums.StatusValues;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "MEMBERSHIPS")
public class Membership {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 50)
    private MembershipTypeValues type;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private StatusValues status;

    @OneToMany(mappedBy = "membership")
    private Set<Student> students = new HashSet<>();

    public Integer getId() {
        return id;
    }

    public MembershipTypeValues getType() {
        return type;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public StatusValues getStatus() {
        return status;
    }

    public Set<Student> getStudents() {
        return students;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setType(MembershipTypeValues type) {
        this.type = type;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public void setStatus(StatusValues status) {
        this.status = status;
    }

    public void setStudents(Set<Student> students) {
        this.students = students;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Membership that = (Membership) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Membership{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", status=" + status +
                '}';
    }
}
