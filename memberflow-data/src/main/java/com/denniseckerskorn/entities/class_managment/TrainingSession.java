package com.denniseckerskorn.entities.class_managment;

import com.denniseckerskorn.enums.StatusValues;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "TRAINING_SESSIONS")
public class TrainingSession {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "fk_group", nullable = false)
    private TrainingGroup trainingGroup;

    @Column(name = "date_time")
    private LocalDateTime date;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 50)
    private StatusValues status;

    //TODO: Control the assitances history, at the moment when group is removed, the assistances are removed too
    @OneToMany(mappedBy = "trainingSession", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Assistance> assistances = new HashSet<>();

    public TrainingSession() {

    }

    public Integer getId() {
        return id;
    }

    public TrainingGroup getTrainingGroup() {
        return trainingGroup;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public StatusValues getStatus() {
        return status;
    }

    public Set<Assistance> getAssistances() {
        return assistances;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setTrainingGroup(TrainingGroup trainingGroup) {
        this.trainingGroup = trainingGroup;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public void setStatus(StatusValues status) {
        this.status = status;
    }

    public void setAssistances(Set<Assistance> assistances) {
        this.assistances = assistances;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TrainingSession that = (TrainingSession) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "TrainingSession{" +
                "id=" + id +
                ", groupId=" + (trainingGroup != null ? trainingGroup.getId() : "null") +
                ", date=" + date +
                ", status=" + status +
                '}';
    }

}
