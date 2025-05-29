package com.denniseckerskorn.dtos.user_managment_dtos;

import java.time.LocalDateTime;

public class NotificationMiniDTO {
    private Integer id;
    private String title;
    private String message;
    private LocalDateTime shippingDate;
    private String type;

    public NotificationMiniDTO() {
    }

    public NotificationMiniDTO(Integer id, String title, String message, LocalDateTime shippingDate, String type) {
        this.id = id;
        this.title = title;
        this.message = message;
        this.shippingDate = shippingDate;
        this.type = type;
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
}
