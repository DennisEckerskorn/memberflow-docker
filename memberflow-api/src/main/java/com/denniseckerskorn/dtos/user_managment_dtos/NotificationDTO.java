package com.denniseckerskorn.dtos.user_managment_dtos;

import com.denniseckerskorn.enums.StatusValues;

import java.time.LocalDateTime;
import java.util.Set;

public class NotificationDTO {
    private Integer id;
    private String title;
    private String message;
    private LocalDateTime shippingDate;
    private String type;
    private StatusValues status;
    private Set<Integer> userIds;

    public NotificationDTO() {}

    public NotificationDTO(Integer id, String title, String message, LocalDateTime shippingDate, String type, StatusValues status, Set<Integer> userIds) {
        this.id = id;
        this.title = title;
        this.message = message;
        this.shippingDate = shippingDate;
        this.type = type;
        this.status = status;
        this.userIds = userIds;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getShippingDate() {
        return shippingDate;
    }

    public void setShippingDate(LocalDateTime shippingDate) {
        this.shippingDate = shippingDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public StatusValues getStatus() {
        return status;
    }

    public void setStatus(StatusValues status) {
        this.status = status;
    }

    public Set<Integer> getUserIds() {
        return userIds;
    }

    public void setUserIds(Set<Integer> userIds) {
        this.userIds = userIds;
    }
}
