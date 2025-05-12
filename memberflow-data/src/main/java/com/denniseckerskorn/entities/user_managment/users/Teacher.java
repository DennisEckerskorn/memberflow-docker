package com.denniseckerskorn.entities.user_managment.users;


import com.denniseckerskorn.entities.class_managment.TrainingGroup;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "TEACHERS")
public class Teacher {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "fk_user", nullable = false, unique = true)
    private User user;

    @Column(name = "discipline", length = 50)
    private String discipline;

    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TrainingGroup> trainingGroups = new HashSet<>();

    public Teacher() {
    }

    public Integer getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public String getDiscipline() {
        return discipline;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<TrainingGroup> getTrainingGroups() {
        return trainingGroups;
    }

    public void setDiscipline(String discipline) {
        this.discipline = discipline;
    }

    public void setTrainingGroups(Set<TrainingGroup> trainingGroups) {
        this.trainingGroups = trainingGroups;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        Teacher teacher = (Teacher) o;
        return Objects.equals(id, teacher.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "discipline='" + (discipline != null ? discipline : "null") + '\'' +
                ", super=" + super.toString() +
                '}';
    }
}
