package com.denniseckerskorn.services.class_managment_services;

import com.denniseckerskorn.entities.class_managment.TrainingGroup;
import com.denniseckerskorn.entities.class_managment.TrainingSession;
import com.denniseckerskorn.entities.user_managment.users.Student;
import com.denniseckerskorn.entities.user_managment.users.Teacher;
import com.denniseckerskorn.exceptions.EntityNotFoundException;
import com.denniseckerskorn.exceptions.InvalidDataException;
import com.denniseckerskorn.repositories.class_managment_repositories.TrainingGroupRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TrainingGroupServiceTest {

    @Mock
    private TrainingGroupRepository trainingGroupRepository;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private TrainingGroupService trainingGroupService;

    private TrainingGroup group;
    private Student student;
    private TrainingSession session;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        Teacher teacher = new Teacher();
        teacher.setId(1);

        group = new TrainingGroup();
        group.setId(1);
        group.setName("Evening Group");
        group.setTeacher(teacher);
        group.setSchedule(LocalDateTime.now().plusDays(1));
        group.setStudents(new HashSet<>());
        group.setTrainingSessions(new HashSet<>());

        student = new Student();
        student.setId(2);
        student.setTrainingGroups(new HashSet<>());

        session = new TrainingSession();
        session.setId(10);

        // Inyección del EntityManager en AbstractService
        Field emField = TrainingGroupService.class.getSuperclass().getDeclaredField("entityManager");
        emField.setAccessible(true);
        emField.set(trainingGroupService, entityManager);
    }

    @Test
    void save_ShouldReturnSavedGroup() {
        when(trainingGroupRepository.save(group)).thenReturn(group);
        TrainingGroup saved = trainingGroupService.save(group);
        assertEquals("Evening Group", saved.getName());
    }

    @Test
    void findById_ShouldReturnGroup() throws Exception {
        when(trainingGroupRepository.existsById(1)).thenReturn(true);
        when(trainingGroupRepository.findById(1)).thenReturn(Optional.of(group));
        TrainingGroup found = trainingGroupService.findById(1);
        assertEquals(1, found.getId());
    }

    @Test
    void deleteById_ShouldCallRepository() throws Exception {
        when(trainingGroupRepository.existsById(1)).thenReturn(true);
        when(trainingGroupRepository.findById(1)).thenReturn(Optional.of(group));
        doNothing().when(trainingGroupRepository).deleteById(1);
        trainingGroupService.deleteById(1);
        verify(trainingGroupRepository).deleteById(1);
    }

    @Test
    void addStudentToGroup_ShouldAddBidirectional() throws Exception {
        when(trainingGroupRepository.existsById(1)).thenReturn(true);
        when(trainingGroupRepository.findById(1)).thenReturn(Optional.of(group));
        trainingGroupService.addStudentToGroup(group, student);
        assertTrue(group.getStudents().contains(student));
        assertTrue(student.getTrainingGroups().contains(group));
    }

    @Test
    void removeStudentFromGroup_ShouldRemoveBidirectional() throws Exception {
        group.getStudents().add(student);
        student.getTrainingGroups().add(group);
        when(trainingGroupRepository.existsById(1)).thenReturn(true);
        when(trainingGroupRepository.findById(1)).thenReturn(Optional.of(group));
        trainingGroupService.removeStudentFromGroup(group, student);
        assertFalse(group.getStudents().contains(student));
        assertFalse(student.getTrainingGroups().contains(group));
    }

    @Test
    void addTrainingSessionToGroup_ShouldAddSession() throws Exception {
        when(trainingGroupRepository.existsById(1)).thenReturn(true);
        when(trainingGroupRepository.findById(1)).thenReturn(Optional.of(group));
        trainingGroupService.addTrainingSessionToGroup(group, session);
        assertTrue(group.getTrainingSessions().contains(session));
        assertEquals(group, session.getTrainingGroup());
    }

    @Test
    void removeTrainingSessionFromGroup_ShouldRemoveSession() throws Exception {
        group.getTrainingSessions().add(session);
        session.setTrainingGroup(group);
        when(trainingGroupRepository.existsById(1)).thenReturn(true);
        when(trainingGroupRepository.findById(1)).thenReturn(Optional.of(group));
        trainingGroupService.removeTrainingSessionFromGroup(group, session);
        assertFalse(group.getTrainingSessions().contains(session));
        assertNull(session.getTrainingGroup());
    }

    /*
    @Test
    void save_InvalidSchedule_ShouldThrow() {
        group.setSchedule(LocalDateTime.now().minusDays(1));
        assertThrows(InvalidDataException.class, () -> trainingGroupService.save(group));
    }

     */
}
