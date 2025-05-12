package com.denniseckerskorn.entities.user_managment;

import com.denniseckerskorn.enums.PermissionValues;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "PERMISSIONS")
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "name", nullable = false, length = 45)
    private PermissionValues permissionName;

    @ManyToMany(mappedBy = "permissions", fetch = FetchType.LAZY)
    private Set<Role> roles = new HashSet<>();

    public Permission() {
    }

    public Integer getId() {
        return id;
    }

    public PermissionValues getPermissionName() {
        return permissionName;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public void setPermissionName(PermissionValues permissionName) {
        this.permissionName = permissionName;
    }

    public void addRole(Role role) {
        this.roles.add(role);
        role.getPermissions().add(this);
    }

    public void removeRole(Role role) {
        this.roles.remove(role);
        role.getPermissions().remove(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Permission that = (Permission) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Permission{" +
                "id=" + id +
                ", privilegeName=" + permissionName +
                '}';
    }

}
