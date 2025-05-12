package com.denniseckerskorn.services.class_managment_services;

import com.denniseckerskorn.entities.class_managment.Assistance;
import com.denniseckerskorn.entities.class_managment.TrainingSession;
import com.denniseckerskorn.entities.user_managment.users.Student;
import com.denniseckerskorn.exceptions.EntityNotFoundException;
import com.denniseckerskorn.exceptions.InvalidDataException;
import com.denniseckerskorn.repositories.class_managment_repositories.AssistanceRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AssistanceServiceTest {

    @Mock
    private AssistanceRepository assistanceRepository;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private AssistanceService assistanceService;

    private Assistance assistance;
    private Student student;
    private TrainingSession session;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        student = new Student();
        student.setId(1);

        session = new TrainingSession();
        session.setId(10);

        assistance = new Assistance();
        assistance.setId(100);
        assistance.setStudent(student);
        assistance.setTrainingSession(session);
        assistance.setDate(LocalDateTime.now().plusDays(1));

        // Inyectar EntityManager con reflexión
        Field emField = AssistanceService.class.getSuperclass().getDeclaredField("entityManager");
        emField.setAccessible(true);
        emField.set(assistanceService, entityManager);
    }

    @Test
    void save_ShouldReturnSavedEntity() {
        when(assistanceRepository.save(any())).thenReturn(assistance);
        Assistance result = assistanceService.save(assistance);
        assertEquals(100, result.getId());
    }

    @Test
    void save_InvalidStudent_ShouldThrow() {
        assistance.setStudent(null);
        assertThrows(InvalidDataException.class, () -> assistanceService.save(assistance));
    }

    @Test
    void save_InvalidSession_ShouldThrow() {
        assistance.setTrainingSession(null);
        assertThrows(InvalidDataException.class, () -> assistanceService.save(assistance));
    }

    @Test
    void findById_ShouldReturnAssistance() throws Exception {
        when(assistanceRepository.existsById(100)).thenReturn(true);
        when(assistanceRepository.findById(100)).thenReturn(Optional.of(assistance));

        Assistance found = assistanceService.findById(100);
        assertEquals(assistance.getId(), found.getId());
    }

    @Test
    void findById_NullId_ShouldThrow() {
        assertThrows(InvalidDataException.class, () -> assistanceService.findById(null));
    }

    @Test
    void findById_NotFound_ShouldThrow() {
        when(assistanceRepository.existsById(999)).thenReturn(false);
        assertThrows(EntityNotFoundException.class, () -> assistanceService.findById(999));
    }

    @Test
    void deleteById_ShouldCallRepository() throws Exception {
        when(assistanceRepository.existsById(100)).thenReturn(true);
        when(assistanceRepository.findById(100)).thenReturn(Optional.of(assistance));

        doNothing().when(assistanceRepository).deleteById(100);
        assistanceService.deleteById(100);

        verify(assistanceRepository).deleteById(100);
    }
}
