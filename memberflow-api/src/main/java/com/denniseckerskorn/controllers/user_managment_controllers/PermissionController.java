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

@RestController
@RequestMapping("/api/v1/permissions")
@Tag(name = "Permission Management", description = "Operations related to permissions")
public class PermissionController {

    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

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

    @Operation(summary = "Get permission by ID", description = "Retrieve a permission by its ID")
    @GetMapping("/getById/{id}")
    public ResponseEntity<PermissionDTO> getById(@PathVariable Integer id) {
        Permission permission = permissionService.findById(id);
        return ResponseEntity.ok(toDTO(permission));
    }

    @Operation(summary = "Create a new permission", description = "Create a new permission")
    @PostMapping("/create")
    public ResponseEntity<PermissionDTO> create(@RequestBody PermissionDTO dto) {
        Permission permission = toEntity(dto);
        Permission saved = permissionService.save(permission);
        return ResponseEntity.status(201).body(toDTO(saved));
    }

    @Operation(summary = "Update an existing permission", description = "Update an existing permission")
    @PutMapping("/update/{id}")
    public ResponseEntity<PermissionDTO> update(@PathVariable Integer id, @RequestBody PermissionDTO dto) {
        dto.setId(id);
        Permission updated = permissionService.update(toEntity(dto));
        return ResponseEntity.ok(toDTO(updated));
    }

    @Operation(summary = "Delete a permission", description = "Delete a permission by its ID")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Integer id) {
        permissionService.deleteById(id);
        return ResponseEntity.ok("Permission deleted successfully.");
    }

    // -------------------- Mapping --------------------

    private PermissionDTO toDTO(Permission permission) {
        return new PermissionDTO(permission.getId(), permission.getPermissionName());
    }

    private Permission toEntity(PermissionDTO dto) {
        Permission permission = (dto.getId() != null)
                ? permissionService.findById(dto.getId())
                : new Permission();

        permission.setPermissionName(dto.getPermissionName());
        return permission;
    }
}
