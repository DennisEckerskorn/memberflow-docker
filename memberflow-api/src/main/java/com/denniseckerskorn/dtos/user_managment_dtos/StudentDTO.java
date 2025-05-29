package com.denniseckerskorn.dtos.user_managment_dtos;

import com.denniseckerskorn.dtos.class_managment_dtos.AssistanceDTO;
import com.denniseckerskorn.dtos.class_managment_dtos.MembershipDTO;
import com.denniseckerskorn.dtos.class_managment_dtos.TrainingGroupDTO;
import com.denniseckerskorn.dtos.class_managment_dtos.TrainingSessionDTO;
import com.denniseckerskorn.entities.class_managment.Assistance;
import com.denniseckerskorn.entities.class_managment.TrainingSession;
import com.denniseckerskorn.entities.user_managment.users.Student;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class StudentDTO {

    private Integer id;
    private UserDTO user;
    private String dni;
    private LocalDate birthdate;
    private String belt;
    private String progress;
    private String medicalReport;
    private String parentName;
    private Integer membershipId;

    private List<AssistanceDTO> assistances;
    private List<TrainingGroupDTO> trainingGroups;
    private List<TrainingSessionDTO> trainingSessions;

    private MembershipDTO membership;

    public StudentDTO() {
    }

    public StudentDTO(Integer id, UserDTO user, String dni, LocalDate birthdate, String belt, String progress,
                      String medicalReport, String parentName, Integer membershipId,
                      List<AssistanceDTO> assistances, List<TrainingGroupDTO> trainingGroups, List<TrainingSessionDTO> trainingSessions) {
        this.id = id;
        this.user = user;
        this.dni = dni;
        this.birthdate = birthdate;
        this.belt = belt;
        this.progress = progress;
        this.medicalReport = medicalReport;
        this.parentName = parentName;
        this.membershipId = membershipId;
        this.assistances = assistances;
        this.trainingGroups = trainingGroups;
        this.trainingSessions = trainingSessions;
    }

    public static StudentDTO fromEntity(Student student) {
        if (student == null || student.getUser() == null) {
            return null;
        }

        List<AssistanceDTO> assistanceDTOs = student.getAssistances().stream()
                .map(AssistanceDTO::fromEntity)
                .collect(Collectors.toList());

        List<TrainingGroupDTO> groupDTOs = student.getTrainingGroups().stream()
                .map(TrainingGroupDTO::fromEntity)
                .collect(Collectors.toList());

        Set<TrainingSession> allSessions = student.getAssistances().stream()
                .map(Assistance::getTrainingSession)
                .collect(Collectors.toSet());

        List<TrainingSessionDTO> sessionDTOs = allSessions.stream()
                .map(TrainingSessionDTO::fromEntity)
                .collect(Collectors.toList());

        return new StudentDTO(
                student.getId(),
                UserDTO.fromEntity(student.getUser()),
                student.getDni(),
                student.getBirthdate(),
                student.getBelt(),
                student.getProgress(),
                student.getMedicalReport(),
                student.getParentName(),
                student.getMembership() != null ? student.getMembership().getId() : null,
                assistanceDTOs,
                groupDTOs,
                sessionDTOs
        ).withMembership(student.getMembership() != null ? MembershipDTO.fromEntity(student.getMembership()) : null);
    }

    public StudentDTO withMembership(MembershipDTO membership) {
        this.membership = membership;
        return this;
    }


    public Student toEntity() {
        Student student = new Student();
        student.setId(this.id);
        if (this.user != null) {
            student.setUser(this.user.toEntity());
        }
        student.setDni(this.dni);
        student.setBirthdate(this.birthdate);
        student.setBelt(this.belt);
        student.setProgress(this.progress);
        student.setMedicalReport(this.medicalReport);
        student.setParentName(this.parentName);
        return student;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public String getBelt() {
        return belt;
    }

    public void setBelt(String belt) {
        this.belt = belt;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    public String getMedicalReport() {
        return medicalReport;
    }

    public void setMedicalReport(String medicalReport) {
        this.medicalReport = medicalReport;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public Integer getMembershipId() {
        return membershipId;
    }

    public void setMembershipId(Integer membershipId) {
        this.membershipId = membershipId;
    }

    public List<AssistanceDTO> getAssistances() {
        return assistances;
    }

    public void setAssistances(List<AssistanceDTO> assistances) {
        this.assistances = assistances;
    }

    public List<TrainingGroupDTO> getTrainingGroups() {
        return trainingGroups;
    }

    public void setTrainingGroups(List<TrainingGroupDTO> trainingGroups) {
        this.trainingGroups = trainingGroups;
    }

    public List<TrainingSessionDTO> getTrainingSessions() {
        return trainingSessions;
    }

    public void setTrainingSessions(List<TrainingSessionDTO> trainingSessions) {
        this.trainingSessions = trainingSessions;
    }

    public MembershipDTO getMembership() {
        return membership;
    }

    public void setMembership(MembershipDTO membership) {
        this.membership = membership;
    }
}
