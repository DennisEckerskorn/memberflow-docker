package com.denniseckerskorn.controllers.user_managment_controllers;

import com.denniseckerskorn.dtos.user_managment_dtos.PermissionDTO;
import com.denniseckerskorn.entities.user_managment.Permission;
import com.denniseckerskorn.services.user_managment_services.PermissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * PermissionController handles requests related to permission management.
 * It provides endpoints for creating, retrieving, updating, and deleting permissions.
 */
@RestController
@RequestMapping("/api/v1/permissions")
@Tag(name = "Permission Management", description = "Operations related to permissions")
public class PermissionController {

    private final PermissionService permissionService;

    /**
     * Constructor for PermissionController.
     *
     * @param permissionService Service for handling permission records.
     */
    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    /**
     * Retrieves all permissions and returns them as a list of DTOs.
     *
     * @return ResponseEntity containing a list of PermissionDTOs or no content if none found.
     */
    @Operation(summary = "Get all permissions", description = "Retrieve a list of all permissions")
    @GetMapping("/getAll")
    public ResponseEntity<List<PermissionDTO>> getAll() {
        List<Permission> permissions = permissionService.findAll();
        if (permissions.isEmpty()) return ResponseEntity.noContent().build();

        List<PermissionDTO> dtos = permissions.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    /**
     * Retrieves a permission by its ID and returns it as a DTO.
     *
     * @param id The ID of the permission to find.
     * @return ResponseEntity containing the PermissionDTO or not found if it doesn't exist.
     */
    @Operation(summary = "Get permission by ID", description = "Retrieve a permission by its ID")
    @GetMapping("/getById/{id}")
    public ResponseEntity<PermissionDTO> getById(@PathVariable Integer id) {
        Permission permission = permissionService.findById(id);
        return ResponseEntity.ok(toDTO(permission));
    }

    /**
     * Creates a new permission and returns the created permission as a DTO.
     *
     * @param dto The PermissionDTO containing the details of the permission to be created.
     * @return ResponseEntity containing the created PermissionDTO.
     */
    @Operation(summary = "Create a new permission", description = "Create a new permission")
    @PostMapping("/create")
    public ResponseEntity<PermissionDTO> create(@RequestBody PermissionDTO dto) {
        Permission permission = toEntity(dto);
        Permission saved = permissionService.save(permission);
        return ResponseEntity.status(201).body(toDTO(saved));
    }

    /**
     * Updates an existing permission and returns the updated permission as a DTO.
     *
     * @param id  The ID of the permission to update.
     * @param dto The PermissionDTO containing the updated details of the permission.
     * @return ResponseEntity containing the updated PermissionDTO.
     */
    @Operation(summary = "Update an existing permission", description = "Update an existing permission")
    @PutMapping("/update/{id}")
    public ResponseEntity<PermissionDTO> update(@PathVariable Integer id, @RequestBody PermissionDTO dto) {
        dto.setId(id);
        Permission updated = permissionService.update(toEntity(dto));
        return ResponseEntity.ok(toDTO(updated));
    }

    /**
     * Deletes a permission by its ID.
     *
     * @param id The ID of the permission to delete.
     * @return ResponseEntity with a success message.
     */
    @Operation(summary = "Delete a permission", description = "Delete a permission by its ID")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Integer id) {
        permissionService.deleteById(id);
        return ResponseEntity.ok("Permission deleted successfully.");
    }

    /* -------------------- Utils -------------------- */

    /**
     * Converts a Permission entity to a PermissionDTO.
     *
     * @param permission The Permission entity to convert.
     * @return The converted PermissionDTO.
     */
    private PermissionDTO toDTO(Permission permission) {
        return new PermissionDTO(permission.getId(), permission.getPermissionName());
    }

    /**
     * Converts a PermissionDTO to a Permission entity.
     *
     * @param dto The PermissionDTO to convert.
     * @return The converted Permission entity.
     */
    private Permission toEntity(PermissionDTO dto) {
        Permission permission = (dto.getId() != null)
                ? permissionService.findById(dto.getId())
                : new Permission();

        permission.setPermissionName(dto.getPermissionName());
        return permission;
    }
}
