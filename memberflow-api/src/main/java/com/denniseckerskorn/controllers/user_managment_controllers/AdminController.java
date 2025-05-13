package com.denniseckerskorn.controllers.user_managment_controllers;

import com.denniseckerskorn.dtos.user_managment_dtos.AdminDTO;
import com.denniseckerskorn.dtos.user_managment_dtos.UserDTO;
import com.denniseckerskorn.entities.user_managment.users.Admin;
import com.denniseckerskorn.entities.user_managment.users.User;
import com.denniseckerskorn.exceptions.InvalidDataException;
import com.denniseckerskorn.services.user_managment_services.AdminService;
import com.denniseckerskorn.services.user_managment_services.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/admins")
@Tag(name = "Administrator Management", description = "Operations related to administrator management")
public class AdminController {

    private final AdminService adminService;
    private final RoleService roleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public AdminController(AdminService adminService, RoleService roleService) {
        this.adminService = adminService;
        this.roleService = roleService;
    }

    @Operation(summary = "Get all administrators")
    @GetMapping("/getAll")
    public ResponseEntity<List<AdminDTO>> getAllAdmins() {
        List<Admin> admins = adminService.findAll();
        List<AdminDTO> dtos = admins.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return dtos.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(dtos);
    }

    @Operation(summary = "Get administrator by ID")
    @GetMapping("/getById/{id}")
    public ResponseEntity<AdminDTO> getById(@PathVariable Integer id) {
        Admin admin = adminService.findById(id);
        return ResponseEntity.ok(convertToDTO(admin));
    }

    @Operation(summary = "Create a new administrator")
    @PostMapping("/create")
    public ResponseEntity<AdminDTO> create(@RequestBody AdminDTO dto) {
        Admin admin = convertToEntity(dto, true);
        Admin saved = adminService.save(admin);
        return ResponseEntity.status(201).body(convertToDTO(saved));
    }

    @Operation(summary = "Update an existing administrator")
    @PutMapping("/update/{id}")
    public ResponseEntity<AdminDTO> update(@PathVariable Integer id, @RequestBody AdminDTO dto) {
        dto.setId(id);
        Admin admin = convertToEntity(dto, false);
        Admin updated = adminService.update(admin);
        return ResponseEntity.ok(convertToDTO(updated));
    }

    @Operation(summary = "Delete an administrator by ID")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Integer id) {
        adminService.deleteById(id);
        return ResponseEntity.ok("Admin deleted");
    }

    // -------------------- Utils --------------------

    private AdminDTO convertToDTO(Admin admin) {
        return AdminDTO.fromEntity(admin);
    }

    private Admin convertToEntity(AdminDTO dto, boolean isCreate) {
        if (dto == null || dto.getUser() == null) {
            throw new InvalidDataException("Admin and User data are required");
        }

        Admin admin;

        if (isCreate) {
            admin = new Admin();
        } else {
            admin = adminService.findById(dto.getId());
            if (admin == null) {
                throw new InvalidDataException("Admin not found for update");
            }
        }

        User user = dto.getUser().toEntity();
        if (isCreate) {
            if (dto.getUser().getPassword() == null || dto.getUser().getPassword().isBlank()) {
                throw new InvalidDataException("Password is required for new admin");
            }
            user.setPassword(passwordEncoder.encode(dto.getUser().getPassword()));
            user.setRegisterDate(user.getRegisterDate() != null ? user.getRegisterDate() : LocalDateTime.now());
        } else {
            if (dto.getUser().getPassword() != null && !dto.getUser().getPassword().isBlank()) {
                user.setPassword(passwordEncoder.encode(dto.getUser().getPassword()));
            }
        }

        if (dto.getUser().getRoleName() != null) {
            user.setRole(roleService.findRoleByName(dto.getUser().getRoleName()));
        }

        admin.setUser(user);
        return admin;
    }

}
