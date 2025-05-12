package com.denniseckerskorn.controllers.user_managment_controllers;

import com.denniseckerskorn.dtos.user_managment_dtos.NotificationMiniDTO;
import com.denniseckerskorn.dtos.user_managment_dtos.StudentMiniDTO;
import com.denniseckerskorn.dtos.user_managment_dtos.StudentHistoryMiniDTO;
import com.denniseckerskorn.dtos.user_managment_dtos.UserDTO;
import com.denniseckerskorn.entities.user_managment.Notification;
import com.denniseckerskorn.entities.user_managment.users.Student;
import com.denniseckerskorn.entities.user_managment.users.User;
import com.denniseckerskorn.exceptions.InvalidDataException;
import com.denniseckerskorn.security.JwtUtil;
import com.denniseckerskorn.services.user_managment_services.RoleService;
import com.denniseckerskorn.services.user_managment_services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * UserController is responsible for handling user-related operations such as creating, updating,
 * deleting, and retrieving user information.
 */
@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "Users", description = "Operations related to user management")
public class UserController {

    private final UserService userService;
    private final RoleService roleService;
    private final JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserController(UserService userService, RoleService roleService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.roleService = roleService;
        this.jwtUtil = jwtUtil;
    }

    @Operation(summary = "Get all users", description = "Retrieve a list of all users")
    @GetMapping("/getAll")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<User> users = userService.findAll();
        if (users.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<UserDTO> userDTOs = users.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(userDTOs);
    }

    @Operation(summary = "Find a User by ID", description = "Retrieve a user by their ID")
    @GetMapping("/getById/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Integer id) {
        User user = userService.findById(id);
        return ResponseEntity.ok(convertToDTO(user));
    }

    @Operation( summary = "Find a User by email", description = "Retrieve a user by their email")
    @PostMapping("/create")
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
        User user = convertToEntity(userDTO, true);
        User saved = userService.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDTO(saved));
    }

    @Operation(summary = "Update a user by ID", description = "Update an existing user")
    @PutMapping("/update/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Integer id, @RequestBody UserDTO userDTO) {
        userDTO.setId(id);
        User user = convertToEntity(userDTO, false);
        User updated = userService.update(user);
        return ResponseEntity.ok(convertToDTO(updated));
    }

    @Operation(summary = "Update a user by email", description = "Update an existing user")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Integer id) {
        userService.deleteById(id);
        return ResponseEntity.ok("User deleted successfully");
    }

    @Operation(summary = "Get current user", description = "Retrieve the currently authenticated user")
    @GetMapping("/me")
    public ResponseEntity<UserDTO> getCurrentUser(HttpServletRequest request) {
        String token = extractTokenFromRequest(request);
        String email = jwtUtil.extractUsername(token);

        User user = userService.findByEmail(email);
        return ResponseEntity.ok(convertToDTO(user));
    }

    // ------------------ UTILS ------------------

    private String extractTokenFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new InvalidDataException("Missing or invalid Authorization header");
        }
        return authHeader.substring(7); // Remove "Bearer "
    }

    private UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO(
                user.getId(),
                user.getName(),
                user.getSurname(),
                user.getEmail(),
                null,
                user.getPhoneNumber(),
                user.getAddress(),
                user.getRegisterDate(),
                user.getRole() != null ? user.getRole().getName() : null,
                user.getStatus()
        );

        if (user.getNotifications() != null && !user.getNotifications().isEmpty()) {
            Set<NotificationMiniDTO> notificationDTOs = user.getNotifications().stream()
                    .map(this::convertNotificationToMiniDTO)
                    .collect(Collectors.toSet());
            dto.setNotifications(notificationDTOs);
        }

        if (user.getStudent() != null) {
            Student student = user.getStudent();
            StudentMiniDTO studentMiniDTO = new StudentMiniDTO();
            studentMiniDTO.setId(student.getId());

            if (student.getHistories() != null && !student.getHistories().isEmpty()) {
                Set<StudentHistoryMiniDTO> historyDTOs = student.getHistories().stream()
                        .map(h -> new StudentHistoryMiniDTO(
                                h.getId(),
                                h.getEventDate(),
                                h.getEventType(),
                                h.getDescription()
                        ))
                        .collect(Collectors.toSet());
                studentMiniDTO.setHistories(historyDTOs);
            }
            dto.setStudent(studentMiniDTO);
        }

        return dto;
    }

    private NotificationMiniDTO convertNotificationToMiniDTO(Notification notification) {
        return new NotificationMiniDTO(
                notification.getId(),
                notification.getTitle(),
                notification.getMessage(),
                notification.getShippingDate(),
                notification.getType()
        );
    }

    private User convertToEntity(UserDTO dto, boolean isCreate) {
        User user;

        if (isCreate) {
            user = new User();
            user.setRegisterDate(dto.getRegisterDate() != null ? dto.getRegisterDate() : LocalDateTime.now());
        } else {
            user = userService.findById(dto.getId());
        }

        user.setName(dto.getName());
        user.setSurname(dto.getSurname());
        user.setEmail(dto.getEmail());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setAddress(dto.getAddress());
        user.setStatus(dto.getStatus());

        if (dto.getRoleName() != null) {
            user.setRole(roleService.findRoleByName(dto.getRoleName()));
        }

        if (isCreate) {
            if (dto.getPassword() == null || dto.getPassword().isBlank()) {
                throw new InvalidDataException("Password is required when creating a user.");
            }
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        } else {
            if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
                user.setPassword(passwordEncoder.encode(dto.getPassword()));
            }
        }

        return user;
    }
}
