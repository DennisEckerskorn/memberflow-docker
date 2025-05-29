package com.denniseckerskorn.controllers.user_managment_controllers;

import com.denniseckerskorn.dtos.user_managment_dtos.RoleDTO;
import com.denniseckerskorn.entities.user_managment.Permission;
import com.denniseckerskorn.entities.user_managment.Role;
import com.denniseckerskorn.services.user_managment_services.PermissionService;
import com.denniseckerskorn.services.user_managment_services.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * RoleController handles requests related to role management.
 * It provides endpoints for creating, retrieving, updating, and deleting roles.
 */
@RestController
@RequestMapping("/api/v1/roles")
@Tag(name = "Role Management", description = "Operations related to roles")
public class RoleController {

    private final RoleService roleService;
    private final PermissionService permissionService;

    /**
     * Constructor for RoleController.
     *
     * @param roleService       Service for handling role records.
     * @param permissionService Service for handling permission records.
     */
    public RoleController(RoleService roleService, PermissionService permissionService) {
        this.roleService = roleService;
        this.permissionService = permissionService;
    }

    /**
     * Retrieves all roles and returns them as a list of DTOs.
     *
     * @return ResponseEntity containing a list of RoleDTOs or no content if none found.
     */
    @Operation(summary = "Get all roles", description = "Retrieve a list of all roles")
    @GetMapping("/getAll")
    public ResponseEntity<List<RoleDTO>> getAll() {
        List<Role> roles = roleService.findAll();
        if (roles.isEmpty()) return ResponseEntity.noContent().build();

        List<RoleDTO> dtos = roles.stream().map(this::toDTO).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    /**
     * Retrieves a role by its ID and returns it as a DTO.
     *
     * @param id The ID of the role to find.
     * @return ResponseEntity containing the RoleDTO or not found if it doesn't exist.
     */
    @Operation(summary = "Get role by ID", description = "Retrieve a role by its ID")
    @GetMapping("/getById/{id}")
    public ResponseEntity<RoleDTO> getById(@PathVariable Integer id) {
        Role role = roleService.findById(id);
        return ResponseEntity.ok(toDTO(role));
    }

    /**
     * Creates a new role and returns the created role as a DTO.
     *
     * @param dto The RoleDTO containing the details of the role to be created.
     * @return ResponseEntity containing the created RoleDTO.
     */
    @Operation(summary = "Create a new role", description = "Create a new role")
    @PostMapping("/create")
    public ResponseEntity<RoleDTO> create(@RequestBody RoleDTO dto) {
        Role role = new Role();
        role.setName(dto.getName());
        Role saved = roleService.save(role);

        if (dto.getPermissionIds() != null) {
            for (Integer pid : dto.getPermissionIds()) {
                Permission perm = permissionService.findById(pid);
                roleService.addPermissionToRole(saved, perm);
            }
        }

        return ResponseEntity.status(201).body(toDTO(saved));
    }

    /**
     * Updates an existing role and returns the updated role as a DTO.
     *
     * @param id  The ID of the role to update.
     * @param dto The RoleDTO containing the updated details of the role.
     * @return ResponseEntity containing the updated RoleDTO.
     */
    @Operation(summary = "Update a role", description = "Update an existing role")
    @PutMapping("/update/{id}")
    public ResponseEntity<RoleDTO> update(@PathVariable Integer id, @RequestBody RoleDTO dto) {
        dto.setId(id);
        Role role = roleService.findById(id);
        role.setName(dto.getName());
        Role updated = roleService.update(role);

        if (dto.getPermissionIds() != null) {
            for (Integer pid : dto.getPermissionIds()) {
                Permission perm = permissionService.findById(pid);
                roleService.addPermissionToRole(updated, perm);
            }
        }

        return ResponseEntity.ok(toDTO(updated));
    }

    /**
     * Deletes a role by its ID.
     *
     * @param id The ID of the role to delete.
     * @return ResponseEntity with a message indicating successful deletion.
     */
    @Operation(summary = "Delete a role", description = "Delete a role by its ID")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Integer id) {
        roleService.deleteById(id);
        return ResponseEntity.ok("Role deleted successfully.");
    }

    /**
     * Adds a permission to a role.
     *
     * @param roleId       The ID of the role to which the permission will be added.
     * @param permissionId The ID of the permission to add to the role.
     * @return ResponseEntity with a success message.
     */
    @Operation(summary = "Add permission to role", description = "Add a permission to a role")
    @PostMapping("/permissions/add/{roleId}/{permissionId}")
    public ResponseEntity<String> addPermissionToRole(@PathVariable Integer roleId, @PathVariable Integer permissionId) {
        Role role = roleService.findById(roleId);
        Permission permission = permissionService.findById(permissionId);
        roleService.addPermissionToRole(role, permission);
        return ResponseEntity.ok("Permission added to role.");
    }

    /**
     * Removes a permission from a role.
     *
     * @param roleId       The ID of the role from which the permission will be removed.
     * @param permissionId The ID of the permission to remove from the role.
     * @return ResponseEntity with a success message.
     */
    @Operation(summary = "Remove permission from role", description = "Remove a permission from a role")
    @DeleteMapping("/permissions/remove/{permissionId}/{roleId}")
    public ResponseEntity<String> removePermissionFromRole(@PathVariable Integer roleId, @PathVariable Integer permissionId) {
        Role role = roleService.findById(roleId);
        Permission permission = permissionService.findById(permissionId);
        roleService.removePermissionFromRole(role, permission);
        return ResponseEntity.ok("Permission removed from role.");
    }

    /* -------------------- Utils -------------------- */

    /**
     * Converts a Role entity to a RoleDTO.
     *
     * @param role The Role entity to convert.
     * @return The converted RoleDTO.
     */
    private RoleDTO toDTO(Role role) {
        Set<Integer> permissionIds = role.getPermissions().stream()
                .map(Permission::getId)
                .collect(Collectors.toSet());

        return new RoleDTO(role.getId(), role.getName(), permissionIds);
    }
}
