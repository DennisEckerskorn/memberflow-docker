package com.denniseckerskorn.dtos.user_managment_dtos;

import com.denniseckerskorn.entities.user_managment.users.Admin;

public class AdminDTO {
    private Integer id;
    private UserDTO user;

    public AdminDTO() {
    }

    public AdminDTO(Integer id, UserDTO user) {
        this.id = id;
        this.user = user;
    }

    public Integer getId() {
        return id;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    // ------------ Métodos de conversión ------------

    public static AdminDTO fromEntity(Admin admin) {
        if (admin == null || admin.getUser() == null) {
            return null;
        }

        return new AdminDTO(
                admin.getId(),
                UserDTO.fromEntity(admin.getUser())
        );
    }

    public Admin toEntity() {
        Admin admin = new Admin();
        admin.setId(this.id);
        if (this.user != null) {
            admin.setUser(this.user.toEntity());
        }
        return admin;
    }
}
