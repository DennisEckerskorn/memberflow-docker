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

    /**
     * Password encoder for encoding passwords.
     */
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Constructor for UserController.
     *
     * @param userService Service for handling user records.
     * @param roleService Service for handling roles.
     * @param jwtUtil     Utility for JWT operations.
     */
    public UserController(UserService userService, RoleService roleService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.roleService = roleService;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Gets all users and returns them as a list of DTOs.
     *
     * @return ResponseEntity containing a list of UserDTOs or no content if none found.
     */
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

    /**
     * Finds a user by their ID and returns it as a DTO.
     *
     * @param id The ID of the user to find.
     * @return ResponseEntity containing the UserDTO or not found if the ID does not exist.
     */
    @Operation(summary = "Find a User by ID", description = "Retrieve a user by their ID")
    @GetMapping("/getById/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Integer id) {
        User user = userService.findById(id);
        return ResponseEntity.ok(convertToDTO(user));
    }

    /**
     * Creates a new user and returns the created user as a DTO.
     *
     * @param userDTO The UserDTO containing the details of the user to be created.
     * @return ResponseEntity containing the created UserDTO with status 201 Created.
     */
    @Operation(summary = "Create a new User", description = "Create a new user")
    @PostMapping("/create")
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
        User user = convertToEntity(userDTO, true);
        User saved = userService.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDTO(saved));
    }

    /**
     * Updates an existing user by ID and returns the updated user as a DTO.
     *
     * @param id      The ID of the user to update.
     * @param userDTO The UserDTO containing the updated details of the user.
     * @return ResponseEntity containing the updated UserDTO.
     */
    @Operation(summary = "Update a user by ID", description = "Update an existing user")
    @PutMapping("/update/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Integer id, @RequestBody UserDTO userDTO) {
        userDTO.setId(id);
        User user = convertToEntity(userDTO, false);
        User updated = userService.update(user);
        return ResponseEntity.ok(convertToDTO(updated));
    }

    /**
     * Deletes a user by ID.
     *
     * @param id The ID of the user to delete.
     * @return ResponseEntity with a success message.
     */
    @Operation(summary = "Delete User by Id", description = "Delete a user by ID")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Integer id) {
        userService.deleteById(id);
        return ResponseEntity.ok("User deleted successfully");
    }

    /**
     * Retrieves the currently authenticated user based on the JWT token from the request.
     *
     * @param request The HTTP request containing the JWT token.
     * @return ResponseEntity containing the UserDTO of the current user.
     */
    @Operation(summary = "Get current user", description = "Retrieve the currently authenticated user")
    @GetMapping("/me")
    public ResponseEntity<UserDTO> getCurrentUser(HttpServletRequest request) {
        String token = extractTokenFromRequest(request);
        String email = jwtUtil.extractUsername(token);

        User user = userService.findByEmail(email);
        return ResponseEntity.ok(convertToDTO(user));
    }

    /* ------------------ UTILS ------------------ */

    /**
     * Extracts the JWT token from the Authorization header of the request.
     *
     * @param request The HTTP request containing the Authorization header.
     * @return The extracted JWT token.
     * @throws InvalidDataException if the Authorization header is missing or invalid.
     */
    private String extractTokenFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new InvalidDataException("Missing or invalid Authorization header");
        }
        return authHeader.substring(7); // Remove "Bearer "
    }

    /**
     * Converts a User entity to a UserDTO.
     *
     * @param user The User entity to convert.
     * @return The converted UserDTO.
     */
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

    /**
     * Converts a Notification entity to a NotificationMiniDTO.
     *
     * @param notification The Notification entity to convert.
     * @return The converted NotificationMiniDTO.
     */
    private NotificationMiniDTO convertNotificationToMiniDTO(Notification notification) {
        return new NotificationMiniDTO(
                notification.getId(),
                notification.getTitle(),
                notification.getMessage(),
                notification.getShippingDate(),
                notification.getType()
        );
    }

    /**
     * Converts a UserDTO to a User entity.
     *
     * @param dto      The UserDTO to convert.
     * @param isCreate Indicates if the conversion is for creation (true) or update (false).
     * @return The converted User entity.
     * @throws InvalidDataException if the provided data is invalid.
     */
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
