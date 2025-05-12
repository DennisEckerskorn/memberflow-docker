package com.denniseckerskorn.entities.user_managment;

import com.denniseckerskorn.entities.user_managment.users.User;
import com.denniseckerskorn.enums.StatusValues;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "NOTIFICATIONS")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "title", length = 200, nullable = false)
    private String title;

    @Column(name = "message", columnDefinition = "TEXT")
    private String message;

    @Column(name = "shipping_date", nullable = false)
    private LocalDateTime shippingDate;

    @Column(length = 100)
    private String type;

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private StatusValues status;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "USERS_NOTIFICATIONS",
            joinColumns = @JoinColumn(name = "fk_notification"),
            inverseJoinColumns = @JoinColumn(name = "fk_user"),
            uniqueConstraints = @UniqueConstraint(
                    name = "uk_notification_user",
                    columnNames = {"fk_notification", "fk_user"}
            )
    )
    private Set<User> users = new HashSet<>();

    public Notification() {
    }

    public Integer getId() {
        return id;
    }

    public Set<User> getUsers() {
        return users;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getShippingDate() {
        return shippingDate;
    }

    public String getType() {
        return type;
    }

    public StatusValues getStatus() {
        return status;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setShippingDate(LocalDateTime shippingDate) {
        this.shippingDate = shippingDate;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setStatus(StatusValues status) {
        this.status = status;
    }

    public void addUser(User user) {
        if (this.users == null) {
            this.users = new HashSet<>();
        }
        this.users.add(user);
        if (user.getNotifications() == null) {
            user.setNotifications(new HashSet<>());
        }
        user.getNotifications().add(this);
    }

    public void removeUser(User user) {
        if (this.users != null) {
            this.users.remove(user);
            if (user.getNotifications() != null) {
                user.getNotifications().remove(this);
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Notification that = (Notification) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Notifications{" +
                "id=" + (id != null ? id : "null") +
                ", title='" + (title != null ? title : "null") + '\'' +
                ", message='" + (message != null ? message : "null") + '\'' +
                ", shippingDate=" + (shippingDate != null ? shippingDate : "null") +
                ", type='" + (type != null ? type : "null") + '\'' +
                ", status=" + (status != null ? status : "null") +
                '}';
    }
}
