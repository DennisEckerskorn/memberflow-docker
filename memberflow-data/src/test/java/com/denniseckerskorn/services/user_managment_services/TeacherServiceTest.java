package com.denniseckerskorn.services.user_managment_services;

import com.denniseckerskorn.entities.class_managment.TrainingGroup;
import com.denniseckerskorn.entities.user_managment.users.Teacher;
import com.denniseckerskorn.entities.user_managment.users.User;
import com.denniseckerskorn.exceptions.DuplicateEntityException;
import com.denniseckerskorn.exceptions.EntityNotFoundException;
import com.denniseckerskorn.exceptions.InvalidDataException;
import com.denniseckerskorn.repositories.user_managment_repositories.TeacherRepository;
import com.denniseckerskorn.repositories.user_managment_repositories.UserRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TeacherServiceTest {

    @Mock
    private TeacherRepository teacherRepository;

    @Mock
    private EntityManager entityManager;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TeacherService teacherService;

    private Teacher teacher;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        User user = new User();
        user.setEmail("test@example.com");
        teacher = new Teacher();
        teacher.setId(1);
        teacher.setUser(user);
        teacher.setTrainingGroups(new HashSet<>());

        // Inject mock EntityManager into AbstractService using reflection
        Field emField = TeacherService.class.getSuperclass().getDeclaredField("entityManager");
        emField.setAccessible(true);
        emField.set(teacherService, entityManager);
    }

    @Test
    void save_ValidTeacher_ReturnsSavedTeacher() {
        when(userRepository.save(teacher.getUser())).thenReturn(teacher.getUser()); // <-- Añadido
        when(teacherRepository.save(teacher)).thenReturn(teacher);

        Teacher saved = teacherService.save(teacher);
        assertNotNull(saved);
        assertEquals("test@example.com", saved.getUser().getEmail());

        verify(userRepository).save(teacher.getUser()); // <-- Validación opcional
    }


    @Test
    void save_NullTeacher_ThrowsInvalidDataException() {
        InvalidDataException exception = assertThrows(InvalidDataException.class, () -> teacherService.save(null));
        assertEquals("Teacher or user email cannot be null.", exception.getMessage());
    }

    @Test
    void update_ValidTeacher_ReturnsUpdatedTeacher() {
        when(teacherRepository.existsById(teacher.getId())).thenReturn(true);
        when(entityManager.merge(teacher)).thenReturn(teacher);
        Teacher updated = teacherService.update(teacher);
        assertNotNull(updated);
        assertEquals("test@example.com", updated.getUser().getEmail());
    }

    @Test
    void update_NullTeacher_ThrowsInvalidDataException() {
        InvalidDataException exception = assertThrows(InvalidDataException.class, () -> teacherService.update(null));
        assertEquals("Teacher or user email cannot be null.", exception.getMessage());
    }

    @Test
    void deleteById_WithTrainingGroups_ThrowsInvalidDataException() {
        teacher.setTrainingGroups(Set.of(new TrainingGroup()));
        when(teacherRepository.findById(1)).thenReturn(Optional.of(teacher));
        when(teacherRepository.existsById(1)).thenReturn(true);

        InvalidDataException exception = assertThrows(InvalidDataException.class, () -> teacherService.deleteById(1));
        assertTrue(exception.getMessage().contains("Cannot delete teacher with ID"));
    }

    @Test
    void deleteById_ValidId_DeletesSuccessfully() {
        when(teacherRepository.findById(1)).thenReturn(Optional.of(teacher));
        when(teacherRepository.existsById(1)).thenReturn(true);
        doNothing().when(teacherRepository).deleteById(1);
        assertDoesNotThrow(() -> teacherService.deleteById(1));
        verify(teacherRepository).deleteById(1);
    }

    @Test
    void findAll_ReturnsListOfTeachers() {
        when(teacherRepository.findAll()).thenReturn(List.of(teacher));
        List<Teacher> list = teacherService.findAll();
        assertEquals(1, list.size());
    }

    @Test
    void exists_ReturnsTrueIfEmailExists() {
        when(teacherRepository.existsByUserEmail("test@example.com")).thenReturn(true);
        assertTrue(teacherService.exists(teacher));
    }

    @Test
    void findById_NullId_ThrowsInvalidDataException() {
        assertThrows(InvalidDataException.class, () -> teacherService.findById(null));
    }

    @Test
    void findById_NotFound_ThrowsEntityNotFoundException() {
        when(teacherRepository.existsById(99)).thenReturn(false);
        assertThrows(EntityNotFoundException.class, () -> teacherService.findById(99));
    }

    @Test
    void findById_ValidId_ReturnsTeacher() {
        when(teacherRepository.existsById(1)).thenReturn(true);
        when(teacherRepository.findById(1)).thenReturn(Optional.of(teacher));
        Teacher found = teacherService.findById(1);
        assertEquals("test@example.com", found.getUser().getEmail());
    }
}
