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

@RestController
@RequestMapping("/api/v1/roles")
@Tag(name = "Role Management", description = "Operations related to roles")
public class RoleController {

    private final RoleService roleService;
    private final PermissionService permissionService;

    public RoleController(RoleService roleService, PermissionService permissionService) {
        this.roleService = roleService;
        this.permissionService = permissionService;
    }

    @Operation(summary = "Get all roles", description = "Retrieve a list of all roles")
    @GetMapping("/getAll")
    public ResponseEntity<List<RoleDTO>> getAll() {
        List<Role> roles = roleService.findAll();
        if (roles.isEmpty()) return ResponseEntity.noContent().build();

        List<RoleDTO> dtos = roles.stream().map(this::toDTO).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @Operation(summary = "Get role by ID", description = "Retrieve a role by its ID")
    @GetMapping("/getById/{id}")
    public ResponseEntity<RoleDTO> getById(@PathVariable Integer id) {
        Role role = roleService.findById(id);
        return ResponseEntity.ok(toDTO(role));
    }

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

    @Operation(summary = "Delete a role", description = "Delete a role by its ID")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Integer id) {
        roleService.deleteById(id);
        return ResponseEntity.ok("Role deleted successfully.");
    }

    @Operation(summary = "Add permission to role", description = "Add a permission to a role")
    @PostMapping("/permissions/add/{roleId}/{permissionId}")
    public ResponseEntity<String> addPermissionToRole(@PathVariable Integer roleId, @PathVariable Integer permissionId) {
        Role role = roleService.findById(roleId);
        Permission permission = permissionService.findById(permissionId);
        roleService.addPermissionToRole(role, permission);
        return ResponseEntity.ok("Permission added to role.");
    }

    @Operation(summary = "Remove permission from role", description = "Remove a permission from a role")
    @DeleteMapping("/permissions/remove/{permissionId}/{roleId}")
    public ResponseEntity<String> removePermissionFromRole(@PathVariable Integer roleId, @PathVariable Integer permissionId) {
        Role role = roleService.findById(roleId);
        Permission permission = permissionService.findById(permissionId);
        roleService.removePermissionFromRole(role, permission);
        return ResponseEntity.ok("Permission removed from role.");
    }

    // -------------------- Mapping --------------------

    private RoleDTO toDTO(Role role) {
        Set<Integer> permissionIds = role.getPermissions().stream()
                .map(Permission::getId)
                .collect(Collectors.toSet());

        return new RoleDTO(role.getId(), role.getName(), permissionIds);
    }
}
