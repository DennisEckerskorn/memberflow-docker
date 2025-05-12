package com.denniseckerskorn.dtos.user_managment_dtos;

import com.denniseckerskorn.enums.PermissionValues;

public class PermissionDTO {
    private Integer id;
    private PermissionValues permissionName;

    public PermissionDTO() {
    }

    public PermissionDTO(Integer id, PermissionValues permissionName) {
        this.id = id;
        this.permissionName = permissionName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public PermissionValues getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(PermissionValues permissionName) {
        this.permissionName = permissionName;
    }
}