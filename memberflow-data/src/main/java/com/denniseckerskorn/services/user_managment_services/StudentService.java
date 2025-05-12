package com.denniseckerskorn.services.user_managment_services;

import com.denniseckerskorn.entities.class_managment.Assistance;
import com.denniseckerskorn.entities.class_managment.Membership;
import com.denniseckerskorn.entities.class_managment.TrainingGroup;
import com.denniseckerskorn.entities.user_managment.StudentHistory;
import com.denniseckerskorn.entities.user_managment.users.Student;
import com.denniseckerskorn.exceptions.DuplicateEntityException;
import com.denniseckerskorn.exceptions.EntityNotFoundException;
import com.denniseckerskorn.exceptions.InvalidDataException;
import com.denniseckerskorn.repositories.class_managment_repositories.AssistanceRepository;
import com.denniseckerskorn.repositories.user_managment_repositories.StudentRepository;
import com.denniseckerskorn.repositories.user_managment_repositories.UserRepository;
import com.denniseckerskorn.services.AbstractService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing Student entities.
 * This class provides methods to perform CRUD operations on Student entities.
 */
@Service
public class StudentService extends AbstractService<Student, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);
    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final AssistanceRepository assistanceRepository;

    /**
     * Constructor for StudentService.
     *
     * @param studentRepository the student repository
     */
    public StudentService(StudentRepository studentRepository, UserRepository userRepository, AssistanceRepository assistanceRepository) {
        super(studentRepository);
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
        this.assistanceRepository = assistanceRepository;
    }

    @Override
    public Student save(Student entity) throws DuplicateEntityException, InvalidDataException {
        logger.info("Saving student: {}", entity);
        if (entity == null || entity.getUser() == null || entity.getDni() == null || entity.getBirthdate() == null) {
            logger.error("Student or DNI cannot be null");
            throw new InvalidDataException("Student or DNI cannot be null");
        }
        logger.info("Student saved: {}", entity);
        userRepository.save(entity.getUser());
        return super.save(entity);
    }

    @Transactional
    @Override
    public Student update(Student entity) throws EntityNotFoundException, InvalidDataException {
        logger.info("Updating student: {}", entity);
        if (entity == null || entity.getDni() == null || entity.getBirthdate() == null) {
            logger.error("Student or DNI cannot be null");
            throw new InvalidDataException("Student or DNI cannot be null");
        }
        logger.info("Student updated: {}", entity);
        return super.update(entity);
    }

    /**
     * Finds all students in the database.
     *
     * @return a list of all students
     */
    @Override
    public List<Student> findAll() {
        logger.info("Fetching all students");
        return super.findAll();

    }

    /**
     * Checks if a student exists in the database.
     *
     * @param entity the student entity to check
     * @return true if the student exists, false otherwise
     */
    @Override
    protected boolean exists(Student entity) {
        return studentRepository.existsByDni(entity.getDni());
    }

    /**
     * Finds a student by ID.
     *
     * @param id the ID of the student to find
     * @return the found student
     * @throws EntityNotFoundException if the student is not found
     */
    @Override
    public Student findById(Integer id) throws EntityNotFoundException, InvalidDataException {
        logger.info("Finding student by ID: {}", id);
        if (id == null) {
            logger.error("ID cannot be null");
            throw new InvalidDataException("ID cannot be null");
        }
        logger.info("Student found: {}", id);
        return super.findById(id);
    }

    /**
     * Deletes a student by ID.
     *
     * @param id the ID of the student to delete
     * @throws EntityNotFoundException if the student is not found
     */
    @Transactional
    @Override
    public void deleteById(Integer id) throws EntityNotFoundException, InvalidDataException {
        Student student = findById(id);
        logger.info("Deleting related history and membership for student ID {}", id);

        deleteStudentHistory(id);
        deleteStudentMembership(id);

        super.deleteById(id);
        logger.info("Student with ID {} deleted successfully", id);
    }


    /**
     * Adds a student history to a student.
     *
     * @param student        the student to add the history to
     * @param studentHistory the history to add
     */
    @Transactional
    public void addStudentHistoryToStudent(Student student, StudentHistory studentHistory) throws EntityNotFoundException, InvalidDataException {
        logger.info("Adding student history to student: {}", student);
        if (student == null || studentHistory == null) {
            logger.error("Student or student history cannot be null");
            throw new InvalidDataException("Student or student history cannot be null");
        }
        Student managedStudent = findById(student.getId());
        if (managedStudent.getHistories().contains(studentHistory)) {
            logger.warn("Student already has this history");
            return;
        }
        managedStudent.getHistories().add(studentHistory);
        super.update(managedStudent);
        logger.info("Student history successfully added to student: {}", student);
    }

    /**
     * Adds a membership to a student.
     *
     * @param student    the student to add the membership to
     * @param membership the membership to add
     */
    @Transactional
    public void addStudentToMembership(Student student, Membership membership) throws EntityNotFoundException, InvalidDataException {
        logger.info("Adding student to membership: {}", student);
        if (student == null || membership == null) {
            logger.error("Student or membership cannot be null");
            throw new InvalidDataException("Student or membership cannot be null");
        }
        Student managedStudent = findById(student.getId());
        if (managedStudent.getMembership() != null && managedStudent.getMembership().getId().equals(membership.getId())) {
            logger.warn("Student already has this membership");
            return;
        }
        managedStudent.setMembership(membership);
        super.update(managedStudent);
        logger.info("Student successfully added to membership: {}", membership);
    }

    @Transactional
    public void addAssistanceToStudent(Student student, Assistance assistance) throws EntityNotFoundException, InvalidDataException {
        logger.info("Adding assistance to student: {}", student);
        if (student == null || assistance == null) {
            throw new InvalidDataException("Student or assistance cannot be null");
        }

        Student managedStudent = findById(student.getId());
        assistance.setStudent(managedStudent);
        managedStudent.getAssistances().add(assistance);

        super.update(managedStudent);
        logger.info("Assistance successfully added to student.");
    }


    @Transactional
    public void addGroupToStudent(Integer studentId, TrainingGroup group) throws EntityNotFoundException, InvalidDataException {
        Student student = findById(studentId);
        if (student == null || group == null) {
            throw new InvalidDataException("Student or group cannot be null");
        }

        if (!student.getTrainingGroups().contains(group)) {
            student.getTrainingGroups().add(group);
            group.getStudents().add(student);
            super.update(student); // o studentRepository.save(student);
        }
    }

    /**
     * Removes a student history from a student.
     *
     * @param id the ID of the student
     */
    @Transactional
    public void deleteStudentHistory(Integer id) throws EntityNotFoundException, InvalidDataException {
        logger.info("Deleting all histories for student ID: {}", id);
        Student student = findById(id);

        if (student.getHistories() != null && !student.getHistories().isEmpty()) {
            student.getHistories().forEach(history -> history.setStudent(null));
            student.getHistories().clear();
            super.save(student);
            logger.info("All histories deleted for student ID: {}", id);
        } else {
            logger.warn("No histories found for student ID: {}", id);
        }
    }

    /**
     * Removes a membership from a student.
     *
     * @param id the ID of the student
     */
    @Transactional
    public void deleteStudentMembership(Integer id) throws EntityNotFoundException, InvalidDataException {
        logger.info("Removing membership for student ID: {}", id);
        Student student = findById(id);

        if (student.getMembership() != null) {
            student.setMembership(null);
            super.save(student);
            logger.info("Membership removed for student ID: {}", id);
        } else {
            logger.warn("No membership found for student ID: {}", id);
        }
    }

    @Transactional
    public void deleteSingleAssistanceFromStudent(Integer studentId, Integer assistanceId) throws EntityNotFoundException {
        logger.info("Deleting assistance ID {} from student ID {}", assistanceId, studentId);

        Student student = findById(studentId);

        Assistance toRemove = student.getAssistances().stream()
                .filter(a -> a.getId().equals(assistanceId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Assistance not found in student"));

        student.getAssistances().remove(toRemove);
        assistanceRepository.delete(toRemove);
        super.update(student);

        logger.info("Assistance ID {} removed from student ID {}", assistanceId, studentId);
    }

    @Transactional
    public void removeGroupFromStudent(Integer studentId, TrainingGroup group) throws EntityNotFoundException, InvalidDataException {
        Student student = findById(studentId);
        if (student == null || group == null) {
            throw new InvalidDataException("Student or group cannot be null");
        }

        if (student.getTrainingGroups().contains(group)) {
            student.getTrainingGroups().remove(group);
            group.getStudents().remove(student);
            super.update(student);
        }
    }


    public Student findByDni(String dni) throws EntityNotFoundException, InvalidDataException {
        logger.info("Finding student by DNI: {}", dni);
        if (dni == null) {
            logger.error("DNI cannot be null");
            throw new InvalidDataException("DNI cannot be null");
        }
        Student student = studentRepository.findByDni(dni);
        if (student == null) {
            logger.error("Student with DNI {} not found", dni);
            throw new EntityNotFoundException("Student with DNI " + dni + " not found");
        }
        logger.info("Student found: {}", student);
        return student;
    }

    public boolean existsByDni(String dni) {
        logger.info("Checking if student exists by DNI: {}", dni);
        return studentRepository.existsByDni(dni);
    }
}