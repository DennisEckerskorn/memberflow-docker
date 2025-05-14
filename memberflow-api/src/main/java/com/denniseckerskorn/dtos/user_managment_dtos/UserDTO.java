package com.denniseckerskorn.dtos.user_managment_dtos;

import com.denniseckerskorn.entities.user_managment.users.User;
import com.denniseckerskorn.enums.StatusValues;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.Set;

public class UserDTO {
    private Integer id;
    private String name;
    private String surname;
    private String email;
    private String password;
    private String phoneNumber;
    private String address;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime registerDate;
    private String roleName;
    private StatusValues status;

    // 🔥 Nuevos campos para el perfil completo
    private Set<NotificationMiniDTO> notifications;
    private StudentMiniDTO student;

    public UserDTO() {
    }

    public UserDTO(Integer id, String name, String surname, String email, String password, String phoneNumber,
                   String address, LocalDateTime registerDate, String roleName, StatusValues status) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.registerDate = registerDate;
        this.roleName = roleName;
        this.status = status;
    }

    public UserDTO(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.surname = user.getSurname();
        this.email = user.getEmail();
        this.phoneNumber = user.getPhoneNumber();
        this.address = user.getAddress();
        this.registerDate = user.getRegisterDate();
        this.roleName = user.getRole() != null ? user.getRole().getName() : null;
        this.status = user.getStatus();
    }


    // Getters y Setters
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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDateTime getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(LocalDateTime registerDate) {
        this.registerDate = registerDate;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public StatusValues getStatus() {
        return status;
    }

    public void setStatus(StatusValues status) {
        this.status = status;
    }

    public Set<NotificationMiniDTO> getNotifications() {
        return notifications;
    }

    public void setNotifications(Set<NotificationMiniDTO> notifications) {
        this.notifications = notifications;
    }

    public StudentMiniDTO getStudent() {
        return student;
    }

    public void setStudent(StudentMiniDTO student) {
        this.student = student;
    }

    // Conversión (opcional)
    public static UserDTO fromEntity(User user) {
        if (user == null) return null;

        return new UserDTO(
                user.getId(),
                user.getName(),
                user.getSurname(),
                user.getEmail(),
                null,
                user.getPhoneNumber(),
                user.getAddress(),
                user.getRegisterDate(),
                user.getRole() != null ? user.getRole().getName() : null,
                user.getStatus()
        );
    }

    public User toEntity() {
        User user = new User();
        user.setId(this.id);
        user.setName(this.name);
        user.setSurname(this.surname);
        user.setEmail(this.email);
        user.setPassword(this.password);
        user.setPhoneNumber(this.phoneNumber);
        user.setAddress(this.address);
        user.setRegisterDate(this.registerDate);
        user.setStatus(this.status);
        return user;
    }
}
