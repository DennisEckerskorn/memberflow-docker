package com.denniseckerskorn.entities.user_managment.users;

import com.denniseckerskorn.entities.finance.Invoice;
import com.denniseckerskorn.entities.user_managment.Notification;
import com.denniseckerskorn.entities.user_managment.Role;
import com.denniseckerskorn.enums.StatusValues;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;


import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "USERS")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private Student student;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private Teacher teacher;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private Admin admin;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "surname", nullable = false, length = 50)
    private String surname;

    @Column(name = "phone_number", nullable = false, length = 30)
    private String phoneNumber;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private StatusValues status;

    @Column(name = "register_date", nullable = false)
    private LocalDateTime registerDate;

    @Column(name = "address", nullable = false, length = 100)
    private String address;

    @ManyToOne
    @JoinColumn(name = "fk_role", nullable = false)
    private Role role;

    @ManyToMany(mappedBy = "users", fetch = FetchType.LAZY)
    private Set<Notification> notifications = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST)
    private Set<Invoice> invoices = new HashSet<>();

    public User() {
    }

    public Integer getId() {
        return id;
    }

    public Student getStudent() {
        return student;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public Admin getAdmin() {
        return admin;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public StatusValues getStatus() {
        return status;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public LocalDateTime getRegisterDate() {
        return registerDate;
    }

    public Role getRole() {
        return role;
    }

    public Set<Notification> getNotifications() {
        return notifications;
    }

    public Set<Invoice> getInvoices() {
        return invoices;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setStatus(StatusValues status) {
        this.status = status;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setRegisterDate(LocalDateTime registerDate) {
        this.registerDate = registerDate;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setNotifications(Set<Notification> notifications) {
        this.notifications = notifications;
    }

    public void setInvoices(Set<Invoice> invoices) {
        this.invoices = invoices;
    }

    public void addNotification(Notification notification) {
        this.notifications.add(notification);
        notification.getUsers().add(this);
    }

    public void removeNotification(Notification notification) {
        this.notifications.remove(notification);
        notification.getUsers().remove(this);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + (name != null ? name : "null") + '\'' +
                ", surname='" + (surname != null ? surname : "null") + '\'' +
                ", phoneNumber='" + (phoneNumber != null ? phoneNumber : "null") + '\'' +
                ", email='" + (email != null ? email : "null") + '\'' +
                ", password='******'" +
                ", status='" + (status != null ? status : "null") + '\'' +
                ", registerDate=" + (registerDate != null ? registerDate : "null") +
                ", address='" + (address != null ? address : "null") + '\'' +
                ", role=" + (role != null ? role.getName() : "null") +
                ", studentId=" + (student != null ? student.getId() : "null") +
                ", teacherId=" + (teacher != null ? teacher.getId() : "null") +
                ", adminId=" + (admin != null ? admin.getId() : "null") +
                '}';
    }

}