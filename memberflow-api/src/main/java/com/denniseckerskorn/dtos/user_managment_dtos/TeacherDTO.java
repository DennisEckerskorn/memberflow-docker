package com.denniseckerskorn.dtos.user_managment_dtos;

import com.denniseckerskorn.entities.user_managment.users.Teacher;

public class TeacherDTO {

    private Integer id;
    private UserDTO user;
    private String discipline;

    public TeacherDTO() {
    }

    public TeacherDTO(Integer id, UserDTO user, String discipline) {
        this.id = id;
        this.user = user;
        this.discipline = discipline;
    }

    // Getters y Setters


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

    public String getDiscipline() {
        return discipline;
    }

    public void setDiscipline(String discipline) {
        this.discipline = discipline;
    }

    public static TeacherDTO fromEntity(Teacher teacher) {
        if (teacher == null || teacher.getUser() == null) {
            return null;
        }
        return new TeacherDTO(
                teacher.getId(),
                UserDTO.fromEntity(teacher.getUser()),
                teacher.getDiscipline()
        );
    }

    public Teacher toEntity() {
        Teacher teacher = new Teacher();
        teacher.setId(this.id);
        if (this.user != null) {
            teacher.setUser(this.user.toEntity());
        }
        teacher.setDiscipline(this.discipline);
        return teacher;
    }
}
