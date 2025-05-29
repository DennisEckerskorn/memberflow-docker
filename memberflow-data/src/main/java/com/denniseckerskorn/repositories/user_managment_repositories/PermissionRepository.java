package com.denniseckerskorn.repositories.user_managment_repositories;

import com.denniseckerskorn.entities.user_managment.Permission;
import com.denniseckerskorn.enums.PermissionValues;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing Permission entities.
 * Provides methods to perform CRUD operations on Permission entities.
 */
public interface PermissionRepository extends JpaRepository<Permission, Integer> {
    Permission findByPermissionName(PermissionValues permissionName);
    boolean existsByPermissionName(PermissionValues permissionName);

}
