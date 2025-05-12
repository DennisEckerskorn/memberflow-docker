package com.denniseckerskorn.entities.user_managment.users;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "ADMINS")
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "fk_user", nullable = false, unique = true)
    private User user;

    public Admin() {
    }

    public Integer getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        Admin admin = (Admin) o;
        return Objects.equals(id, admin.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Admin{" +
                "id=" + id +
                ", userId=" + (user != null ? user.getId() : "null") +
                ", userEmail=" + (user != null ? user.getEmail() : "null") +
                '}';
    }

}
