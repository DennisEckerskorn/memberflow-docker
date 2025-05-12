package com.denniseckerskorn.services.user_managment_services;

import com.denniseckerskorn.entities.class_managment.Assistance;
import com.denniseckerskorn.entities.class_managment.Membership;
import com.denniseckerskorn.entities.class_managment.TrainingGroup;
import com.denniseckerskorn.entities.user_managment.StudentHistory;
import com.denniseckerskorn.entities.user_managment.users.Student;
import com.denniseckerskorn.entities.user_managment.users.User;
import com.denniseckerskorn.exceptions.EntityNotFoundException;
import com.denniseckerskorn.exceptions.InvalidDataException;
import com.denniseckerskorn.repositories.user_managment_repositories.StudentRepository;
import com.denniseckerskorn.repositories.user_managment_repositories.UserRepository;
import com.denniseckerskorn.repositories.class_managment_repositories.AssistanceRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StudentServiceTest {

    @Mock private StudentRepository studentRepository;
    @Mock private UserRepository userRepository;
    @Mock private AssistanceRepository assistanceRepository;
    @Mock private EntityManager entityManager;

    @InjectMocks
    private StudentService studentService;

    private Student student;
    private Membership membership;
    private StudentHistory history;
    private Assistance assistance;
    private TrainingGroup group;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        student = new Student();
        student.setId(1);
        student.setDni("12345678A");
        student.setBirthdate(LocalDate.of(2000, 1, 1));
        student.setUser(new User());
        student.setHistories(new HashSet<>());
        student.setAssistances(new HashSet<>());
        student.setTrainingGroups(new HashSet<>());

        membership = new Membership(); membership.setId(1);
        history = new StudentHistory(); history.setId(1);
        assistance = new Assistance(); assistance.setId(10);
        group = new TrainingGroup(); group.setId(99); group.setStudents(new HashSet<>());

        // Inyectar entityManager usando reflexión
        Field emField = StudentService.class.getSuperclass().getDeclaredField("entityManager");
        emField.setAccessible(true);
        emField.set(studentService, entityManager);
    }

    @Test
    void save_ValidStudent_ShouldSucceed() {
        when(userRepository.save(any())).thenReturn(student.getUser());
        when(studentRepository.save(any())).thenReturn(student);

        Student saved = studentService.save(student);
        assertEquals("12345678A", saved.getDni());
    }

    @Test
    void update_ValidStudent_ShouldSucceed() {
        when(studentRepository.existsById(1)).thenReturn(true);
        when(entityManager.merge(any())).thenReturn(student);
        Student updated = studentService.update(student);
        assertEquals("12345678A", updated.getDni());
    }

    @Test
    void addStudentHistoryToStudent_ShouldAddSuccessfully() {
        when(studentRepository.existsById(1)).thenReturn(true);
        when(studentRepository.findById(1)).thenReturn(Optional.of(student));
        studentService.addStudentHistoryToStudent(student, history);
        assertTrue(student.getHistories().contains(history));
    }

    @Test
    void addStudentToMembership_ShouldLinkMembership() {
        when(studentRepository.existsById(1)).thenReturn(true);
        when(studentRepository.findById(1)).thenReturn(Optional.of(student));
        studentService.addStudentToMembership(student, membership);
        assertEquals(membership, student.getMembership());
    }

    @Test
    void addAssistanceToStudent_ShouldLinkAssistance() {
        when(studentRepository.existsById(1)).thenReturn(true);
        when(studentRepository.findById(1)).thenReturn(Optional.of(student));
        studentService.addAssistanceToStudent(student, assistance);
        assertTrue(student.getAssistances().contains(assistance));
        assertEquals(student, assistance.getStudent());
    }

    @Test
    void addGroupToStudent_ShouldLinkBothSides() {
        when(studentRepository.existsById(1)).thenReturn(true);
        when(studentRepository.findById(1)).thenReturn(Optional.of(student));
        studentService.addGroupToStudent(1, group);
        assertTrue(student.getTrainingGroups().contains(group));
        assertTrue(group.getStudents().contains(student));
    }

    @Test
    void removeGroupFromStudent_ShouldUnlinkBothSides() {
        student.getTrainingGroups().add(group);
        group.getStudents().add(student);

        when(studentRepository.existsById(1)).thenReturn(true);
        when(studentRepository.findById(1)).thenReturn(Optional.of(student));
        studentService.removeGroupFromStudent(1, group);
        assertFalse(student.getTrainingGroups().contains(group));
        assertFalse(group.getStudents().contains(student));
    }

    @Test
    void deleteSingleAssistanceFromStudent_ShouldRemoveCorrectly() {
        assistance.setStudent(student);
        student.getAssistances().add(assistance);

        when(studentRepository.existsById(1)).thenReturn(true);
        when(studentRepository.findById(1)).thenReturn(Optional.of(student));
        doNothing().when(assistanceRepository).delete(any());

        studentService.deleteSingleAssistanceFromStudent(1, 10);
        assertFalse(student.getAssistances().contains(assistance));
    }

    @Test
    void deleteStudentHistory_ShouldClearHistories() {
        history.setStudent(student);
        student.getHistories().add(history);

        when(studentRepository.existsById(1)).thenReturn(true);
        when(studentRepository.findById(1)).thenReturn(Optional.of(student));
        studentService.deleteStudentHistory(1);
        assertTrue(student.getHistories().isEmpty());
    }

    @Test
    void deleteStudentMembership_ShouldUnlink() {
        student.setMembership(membership);
        when(studentRepository.existsById(1)).thenReturn(true);
        when(studentRepository.findById(1)).thenReturn(Optional.of(student));
        studentService.deleteStudentMembership(1);
        assertNull(student.getMembership());
    }

    @Test
    void findByDni_ValidDni_ShouldReturnStudent() {
        when(studentRepository.findByDni("12345678A")).thenReturn(student);
        Student result = studentService.findByDni("12345678A");
        assertEquals("12345678A", result.getDni());
    }

    @Test
    void existsByDni_ShouldReturnTrue() {
        when(studentRepository.existsByDni("12345678A")).thenReturn(true);
        assertTrue(studentService.existsByDni("12345678A"));
    }

    @Test
    void findAll_ShouldReturnList() {
        when(studentRepository.findAll()).thenReturn(List.of(student));
        List<Student> list = studentService.findAll();
        assertEquals(1, list.size());
    }
}
