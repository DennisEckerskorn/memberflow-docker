package com.denniseckerskorn.services.class_managment_services;

import com.denniseckerskorn.entities.class_managment.TrainingGroup;
import com.denniseckerskorn.entities.class_managment.TrainingSession;
import com.denniseckerskorn.entities.user_managment.users.Student;
import com.denniseckerskorn.enums.StatusValues;
import com.denniseckerskorn.exceptions.EntityNotFoundException;
import com.denniseckerskorn.exceptions.InvalidDataException;
import com.denniseckerskorn.repositories.class_managment_repositories.TrainingSessionRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TrainingSessionServiceTest {

    @Mock
    private TrainingSessionRepository trainingSessionRepository;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private TrainingSessionService trainingSessionService;

    private TrainingSession session;
    private TrainingGroup group;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        group = new TrainingGroup();
        group.setId(1);
        group.setStudents(new HashSet<>());

        session = new TrainingSession();
        session.setId(1);
        session.setDate(LocalDateTime.now().plusDays(1));
        session.setStatus(StatusValues.ACTIVE);
        session.setTrainingGroup(group);

        // Inyección del EntityManager en AbstractService
        Field emField = TrainingSessionService.class.getSuperclass().getDeclaredField("entityManager");
        emField.setAccessible(true);
        emField.set(trainingSessionService, entityManager);
    }

    @Test
    void save_ShouldReturnSavedSession() {
        when(trainingSessionRepository.save(session)).thenReturn(session);
        TrainingSession saved = trainingSessionService.save(session);
        assertEquals(1, saved.getId());
    }

    @Test
    void findById_ShouldReturnSession() throws Exception {
        when(trainingSessionRepository.existsById(1)).thenReturn(true);
        when(trainingSessionRepository.findById(1)).thenReturn(Optional.of(session));
        TrainingSession found = trainingSessionService.findById(1);
        assertEquals(1, found.getId());
    }

    @Test
    void deleteById_ShouldCallRepository() throws Exception {
        when(trainingSessionRepository.existsById(1)).thenReturn(true);
        when(trainingSessionRepository.findById(1)).thenReturn(Optional.of(session));
        doNothing().when(trainingSessionRepository).deleteById(1);
        trainingSessionService.deleteById(1);
        verify(trainingSessionRepository).deleteById(1);
    }

    @Test
    void getStudentsFromSession_ShouldReturnStudentSet() throws Exception {
        Student student1 = new Student();
        student1.setId(10);
        group.getStudents().add(student1);

        when(trainingSessionRepository.existsById(1)).thenReturn(true);
        when(trainingSessionRepository.findById(1)).thenReturn(Optional.of(session));

        Set<Student> students = trainingSessionService.getStudentsFromSession(session);
        assertEquals(1, students.size());
        assertTrue(students.contains(student1));
    }
/*
    @Test
    void save_InvalidSession_ShouldThrow() {
        session.setDate(LocalDateTime.now().minusDays(1)); // fecha pasada
        assertThrows(InvalidDataException.class, () -> trainingSessionService.save(session));

        session.setDate(LocalDateTime.now().plusDays(1));
        session.setTrainingGroup(null); // sin grupo
        assertThrows(InvalidDataException.class, () -> trainingSessionService.save(session));

        session.setTrainingGroup(group);
        session.setStatus(null); // sin status
        assertThrows(InvalidDataException.class, () -> trainingSessionService.save(session));
    }

 */
}
