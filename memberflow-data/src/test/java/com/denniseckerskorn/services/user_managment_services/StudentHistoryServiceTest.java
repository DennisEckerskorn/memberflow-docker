package com.denniseckerskorn.services.user_managment_services;

import com.denniseckerskorn.entities.user_managment.StudentHistory;
import com.denniseckerskorn.exceptions.EntityNotFoundException;
import com.denniseckerskorn.repositories.user_managment_repositories.StudentHistoryRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StudentHistoryServiceTest {

    @Mock
    private StudentHistoryRepository studentHistoryRepository;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private StudentHistoryService studentHistoryService;

    private StudentHistory history;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        // Inyectar entityManager manualmente en AbstractService
        Field entityManagerField = studentHistoryService.getClass().getSuperclass().getDeclaredField("entityManager");
        entityManagerField.setAccessible(true);
        entityManagerField.set(studentHistoryService, entityManager);

        history = new StudentHistory();
        history.setId(1);
        history.setEventType("REGISTERED");
    }

    @Test
    void save_ValidHistory_ReturnsSavedEntity() {
        when(studentHistoryRepository.save(history)).thenReturn(history);
        StudentHistory saved = studentHistoryService.save(history);
        assertNotNull(saved);
        assertEquals("REGISTERED", saved.getEventType());
    }

    @Test
    void findById_ValidId_ReturnsEntity() {
        when(studentHistoryRepository.existsById(1)).thenReturn(true);
        when(studentHistoryRepository.findById(1)).thenReturn(Optional.of(history));
        StudentHistory found = studentHistoryService.findById(1);
        assertEquals(history.getEventType(), found.getEventType());
    }

    @Test
    void findById_NotFound_ThrowsException() {
        when(studentHistoryRepository.existsById(99)).thenReturn(false);
        assertThrows(EntityNotFoundException.class, () -> studentHistoryService.findById(99));
    }

    @Test
    void deleteById_ValidId_DeletesSuccessfully() {
        when(studentHistoryRepository.existsById(1)).thenReturn(true);
        doNothing().when(studentHistoryRepository).deleteById(1);
        studentHistoryService.deleteById(1);
        verify(studentHistoryRepository).deleteById(1);
    }

    @Test
    void findAll_ReturnsList() {
        when(studentHistoryRepository.findAll()).thenReturn(List.of(history));
        List<StudentHistory> list = studentHistoryService.findAll();
        assertEquals(1, list.size());
        assertEquals("REGISTERED", list.get(0).getEventType());
    }

    @Test
    void exists_WithMatchingEventType_ReturnsTrue() {
        when(studentHistoryRepository.existsByEventType("REGISTERED")).thenReturn(true);
        assertTrue(studentHistoryService.exists(history));
    }

    @Test
    void update_ValidHistory_ReturnsUpdatedEntity() {
        when(studentHistoryRepository.existsById(1)).thenReturn(true);
        when(entityManager.merge(history)).thenReturn(history);
        StudentHistory updated = studentHistoryService.update(history);
        assertEquals("REGISTERED", updated.getEventType());
    }

    @Test
    void update_NonExistent_ThrowsException() {
        history.setId(99);
        when(studentHistoryRepository.existsById(99)).thenReturn(false);
        assertThrows(EntityNotFoundException.class, () -> studentHistoryService.update(history));
    }
}
