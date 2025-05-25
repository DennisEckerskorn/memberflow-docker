package com.denniseckerskorn.entities.user_managment.users;

import com.denniseckerskorn.entities.class_managment.Assistance;
import com.denniseckerskorn.entities.class_managment.TrainingGroup;
import com.denniseckerskorn.entities.class_managment.Membership;
import com.denniseckerskorn.entities.user_managment.StudentHistory;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "STUDENTS")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "fk_user", nullable = false, unique = true)
    private User user;

    @Column(name = "dni", nullable = false, length = 10, unique = true)
    private String dni;

    @Column(name = "birthdate", nullable = false)
    private LocalDate birthdate;

    @Column(name = "belt", length = 20)
    private String belt;

    @Column(name = "progress", columnDefinition = "TEXT")
    private String progress;

    @Column(name = "medical_report", length = 500)
    private String medicalReport;

    @Column(name = "parent_name", length = 50)
    private String parentName;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<StudentHistory> histories = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "fk_membership")
    private Membership membership;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Assistance> assistances = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "STUDENTS_GROUPS", joinColumns = @JoinColumn(name = "fk_student"), inverseJoinColumns = @JoinColumn(name = "fk_group"))
    private Set<TrainingGroup> trainingGroups = new HashSet<>();

    public Student() {
    }

    public Integer getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public String getDni() {
        return dni;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public Set<StudentHistory> getHistories() {
        return histories;
    }

    public Membership getMembership() {
        return membership;
    }

    public String getMedicalReport() {
        return medicalReport;
    }

    public String getParentName() {
        return parentName;
    }

    public String getBelt() {
        return belt;
    }

    public String getProgress() {
        return progress;
    }

    public Set<Assistance> getAssistances() {
        return assistances;
    }

    public Set<TrainingGroup> getTrainingGroups() {
        return trainingGroups;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setMedicalReport(String medicalReport) {
        this.medicalReport = medicalReport;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public void setBelt(String belt) {
        this.belt = belt;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    public void setHistories(Set<StudentHistory> histories) {
        this.histories = histories;
    }

    public void setMembership(Membership membership) {
        this.membership = membership;
    }

    public void setAssistances(Set<Assistance> assistances) {
        this.assistances = assistances;
    }

    public void setTrainingGroups(Set<TrainingGroup> trainingGroups) {
        this.trainingGroups = trainingGroups;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        Student student = (Student) o;
        return Objects.equals(id, student.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Student{" +
                "dni='" + (dni != null ? dni : "null") + '\'' +
                ", birthdate=" + (birthdate != null ? birthdate : "null") +
                ", belt='" + (belt != null ? belt : "null") + '\'' +
                ", progress='" + (progress != null ? progress : "null") + '\'' +
                ", medicalReport='" + (medicalReport != null ? medicalReport : "null") + '\'' +
                ", parentName='" + (parentName != null ? parentName : "null") + '\'' +
                ", membershipId=" + (membership != null ? membership.getId() : "null") +
                //", trainingGroups=" + (trainingGroups != null ? trainingGroups.size() : "null") +
                //", assistances=" + (assistances != null ? assistances.size() : "null") +
                //", histories=" + (histories != null ? histories.size() : "null") +
                '}';
    }
}
