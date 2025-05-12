package com.denniseckerskorn.repositories.user_managment_repositories;

import com.denniseckerskorn.entities.user_managment.Permission;
import com.denniseckerskorn.enums.PermissionValues;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission, Integer> {
    Permission findByPermissionName(PermissionValues permissionName);
    boolean existsByPermissionName(PermissionValues permissionName);

}
