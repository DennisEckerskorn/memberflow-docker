package com.denniseckerskorn.services.user_managment_services;

import com.denniseckerskorn.entities.user_managment.Notification;
import com.denniseckerskorn.entities.user_managment.users.User;
import com.denniseckerskorn.exceptions.DuplicateEntityException;
import com.denniseckerskorn.exceptions.EntityNotFoundException;
import com.denniseckerskorn.exceptions.InvalidDataException;
import com.denniseckerskorn.repositories.user_managment_repositories.NotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationService notificationService;

    private Notification notification;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        notification = new Notification();
        notification.setId(1);
        notification.setTitle("Test");
        notification.setShippingDate(LocalDateTime.now());
    }

    @Test
    void save_ValidNotification_ReturnsSavedNotification() {
        when(notificationRepository.save(notification)).thenReturn(notification);
        Notification saved = notificationService.save(notification);
        assertNotNull(saved);
        assertEquals("Test", saved.getTitle());
    }

    @Test
    void save_NullNotification_ThrowsInvalidDataException() {
        InvalidDataException exception = assertThrows(InvalidDataException.class, () -> notificationService.save(null));
        assertEquals("Notification cannot be null", exception.getMessage());
    }


    @Test
    void findById_ValidId_ReturnsNotification() {
        when(notificationRepository.existsById(1)).thenReturn(true);
        when(notificationRepository.findById(1)).thenReturn(Optional.of(notification));
        Notification found = notificationService.findById(1);
        assertEquals("Test", found.getTitle());
    }

    @Test
    void findById_NullId_ThrowsInvalidDataException() {
        assertThrows(InvalidDataException.class, () -> notificationService.findById(null));
    }

    @Test
    void deleteNotificationById_ValidId_DeletesNotification() {
        when(notificationRepository.existsById(1)).thenReturn(true);
        doNothing().when(notificationRepository).deleteById(1);
        assertDoesNotThrow(() -> notificationService.deleteById(1));
    }

    @Test
    void findAll_ReturnsList() {
        when(notificationRepository.findAll()).thenReturn(Collections.singletonList(notification));
        List<Notification> list = notificationService.findAll();
        assertEquals(1, list.size());
    }

    @Test
    void addNotificationToUser_AddsUserSuccessfully() {
        User user = new User();
        user.setId(1);
        when(notificationRepository.findById(1)).thenReturn(Optional.of(notification));
        when(notificationRepository.existsById(1)).thenReturn(true);
        when(notificationRepository.save(notification)).thenReturn(notification);
        notification.setUsers(new HashSet<>());
        notificationService.addNotificationToUser(notification, user);
        verify(notificationRepository, times(1)).save(any());
    }

    @Test
    void removeNotificationFromUser_RemovesUserSuccessfully() {
        User user = new User();
        user.setId(1);
        notification.addUser(user);
        when(notificationRepository.findById(1)).thenReturn(Optional.of(notification));
        when(notificationRepository.existsById(1)).thenReturn(true);
        when(notificationRepository.save(notification)).thenReturn(notification);
        notificationService.removeNotificationFromUser(notification, user);
        verify(notificationRepository, times(1)).save(notification);
    }
}
