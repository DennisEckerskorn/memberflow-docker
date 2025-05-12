package com.denniseckerskorn.dtos.user_managment_dtos;

import java.util.Set;

public class RoleDTO {
    private Integer id;
    private String name;
    private Set<Integer> permissionIds;

    public RoleDTO() {
    }

    public RoleDTO(Integer id, String name, Set<Integer> permissionIds) {
        this.id = id;
        this.name = name;
        this.permissionIds = permissionIds;
    }

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

    public Set<Integer> getPermissionIds() {
        return permissionIds;
    }

    public void setPermissionIds(Set<Integer> permissionIds) {
        this.permissionIds = permissionIds;
    }
}