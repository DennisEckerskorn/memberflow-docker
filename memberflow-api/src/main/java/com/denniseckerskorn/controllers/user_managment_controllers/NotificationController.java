package com.denniseckerskorn.controllers.user_managment_controllers;

import com.denniseckerskorn.dtos.user_managment_dtos.NotificationDTO;
import com.denniseckerskorn.entities.user_managment.Notification;
import com.denniseckerskorn.entities.user_managment.users.User;
import com.denniseckerskorn.services.user_managment_services.NotificationService;
import com.denniseckerskorn.services.user_managment_services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/notifications")
@Tag(name = "Notification Management", description = "Operations related to notifications")
public class NotificationController {

    private final NotificationService notificationService;
    private final UserService userService;

    public NotificationController(NotificationService notificationService, UserService userService) {
        this.notificationService = notificationService;
        this.userService = userService;
    }

    @Operation(summary = "Get all notifications", description = "Retrieve a list of all notifications")
    @GetMapping("/getAll")
    public ResponseEntity<List<NotificationDTO>> getAll() {
        List<Notification> notifications = notificationService.findAll();
        if (notifications.isEmpty()) return ResponseEntity.noContent().build();

        List<NotificationDTO> dtos = notifications.stream().map(this::toDTO).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @Operation(summary = "Get a notification by ID", description = "Retrieve a notification by its ID")
    @GetMapping("/getById/{id}")
    public ResponseEntity<NotificationDTO> getById(@PathVariable Integer id) {
        Notification notification = notificationService.findById(id);
        return ResponseEntity.ok(toDTO(notification));
    }

    @Operation(summary = "Create a new notification", description = "Create a new notification")
    @PostMapping("/create")
    public ResponseEntity<NotificationDTO> create(@RequestBody NotificationDTO dto) {
        Notification notification = toEntity(dto, false);
        Notification saved = notificationService.save(notification);

        if (dto.getUserIds() != null) {
            for (Integer userId : dto.getUserIds()) {
                User user = userService.findById(userId);
                notificationService.addNotificationToUser(saved, user);
            }
        }

        return ResponseEntity.status(201).body(toDTO(saved));
    }


    @Operation(summary = "Update a notification by ID", description = "Update a notification by its ID")
    @PutMapping("/update/{id}")
    public ResponseEntity<NotificationDTO> update(@PathVariable Integer id, @RequestBody NotificationDTO dto) {
        dto.setId(id);
        Notification notification = toEntity(dto, false);
        Notification updated = notificationService.update(notification);

        Set<User> existingUsers = updated.getUsers();
        if (existingUsers != null) {
            List<User> clonedUsers = new ArrayList<>(existingUsers);
            for (User user : clonedUsers) {
                notificationService.removeNotificationFromUser(updated, user);
            }
        }

        if (dto.getUserIds() != null) {
            for (Integer userId : dto.getUserIds()) {
                User user = userService.findById(userId);
                notificationService.addNotificationToUser(updated, user);
            }
        }

        return ResponseEntity.ok(toDTO(updated));
    }


    @Operation(summary = "Delete a notification by ID", description = "Delete a notification by its ID")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Integer id) {
        notificationService.deleteById(id);
        return ResponseEntity.ok("Notification deleted successfully.");
    }

    // -------------------- Mapping --------------------

    private NotificationDTO toDTO(Notification notification) {
        Set<Integer> userIds = notification.getUsers() != null
                ? notification.getUsers().stream().map(User::getId).collect(Collectors.toSet())
                : null;

        return new NotificationDTO(
                notification.getId(),
                notification.getTitle(),
                notification.getMessage(),
                notification.getShippingDate(),
                notification.getType(),
                notification.getStatus(),
                userIds
        );
    }

    private Notification toEntity(NotificationDTO dto, boolean includeUsers) {
        Notification notification = (dto.getId() != null)
                ? notificationService.findById(dto.getId())
                : new Notification();

        notification.setTitle(dto.getTitle());
        notification.setMessage(dto.getMessage());
        notification.setShippingDate(dto.getShippingDate());
        notification.setType(dto.getType());
        notification.setStatus(dto.getStatus());

        return notification;
    }

}
