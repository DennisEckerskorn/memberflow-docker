package com.denniseckerskorn.services.user_managment_services;

import com.denniseckerskorn.entities.user_managment.users.Student;
import com.denniseckerskorn.entities.user_managment.users.Teacher;
import com.denniseckerskorn.entities.user_managment.users.User;
import com.denniseckerskorn.exceptions.DuplicateEntityException;
import com.denniseckerskorn.exceptions.EntityNotFoundException;
import com.denniseckerskorn.exceptions.InvalidDataException;
import com.denniseckerskorn.repositories.user_managment_repositories.TeacherRepository;
import com.denniseckerskorn.repositories.user_managment_repositories.UserRepository;
import com.denniseckerskorn.services.AbstractService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service class for managing Teacher entities.
 * This class provides methods to perform CRUD operations on Teacher entities.
 */
@Service
public class TeacherService extends AbstractService<Teacher, Integer> {

    private final static Logger logger = LoggerFactory.getLogger(TeacherService.class);
    private final TeacherRepository teacherRepository;
    private final UserRepository userRepository;

    /**
     * Constructor for TeacherService.
     *
     * @param teacherRepository the teacher repository
     * @param userRepository    the user repository
     */
    public TeacherService(TeacherRepository teacherRepository, UserRepository userRepository) {
        super(teacherRepository);
        this.teacherRepository = teacherRepository;
        this.userRepository = userRepository;
    }

    /**
     * Saves a new teacher in the database.
     *
     * @param entity the entity to save
     * @return the saved entity
     * @throws DuplicateEntityException if the entity already exists
     * @throws InvalidDataException     if the entity is invalid
     */
    @Override
    public Teacher save(Teacher entity) throws DuplicateEntityException, InvalidDataException {
        logger.info("Saving teacher: {}", entity);
        if (entity == null || entity.getUser() == null || entity.getUser().getEmail() == null) {
            throw new InvalidDataException("Teacher or user email cannot be null.");
        }
        logger.info("Teacher saved: {}", entity);
        userRepository.save(entity.getUser());
        return super.save(entity);
    }


    /**
     * Updates an existing teacher in the database.
     *
     * @param entity the entity to update
     * @return the updated entity
     * @throws EntityNotFoundException if the entity is not found
     * @throws InvalidDataException    if the entity is invalid
     */
    @Transactional
    @Override
    public Teacher update(Teacher entity) throws EntityNotFoundException, InvalidDataException {
        logger.info("Updating teacher: {}", entity);
        if (entity == null || entity.getUser() == null || entity.getUser().getEmail() == null) {
            throw new InvalidDataException("Teacher or user email cannot be null.");
        }
        logger.info("Teacher updated: {}", entity);
        return super.update(entity);
    }

    /**
     * Checks if a teacher with the same email already exists in the database.
     *
     * @param entity the entity to check
     * @return true if the entity exists, false otherwise
     */
    @Override
    protected boolean exists(Teacher entity) {
        return entity != null && entity.getUser() != null &&
                entity.getUser().getEmail() != null &&
                teacherRepository.existsByUserEmail(entity.getUser().getEmail());
    }


    /**
     * Finds a teacher by their ID.
     *
     * @param id the ID of the entity to find
     * @return the found entity
     * @throws EntityNotFoundException if the entity is not found
     * @throws InvalidDataException    if the ID is invalid
     */
    @Override
    public Teacher findById(Integer id) throws EntityNotFoundException, InvalidDataException {
        logger.info("Fetching teacher by ID: {}", id);
        if (id == null) {
            throw new InvalidDataException("ID cannot be null");
        }
        logger.info("Teacher found: {}", id);
        return super.findById(id);
    }

    /**
     * Deletes a teacher by their ID.
     *
     * @param id the ID of the entity to delete
     * @throws EntityNotFoundException if the entity is not found
     * @throws InvalidDataException    if the ID is invalid
     */
    @Transactional
    @Override
    public void deleteById(Integer id) throws EntityNotFoundException, InvalidDataException {
        logger.info("Deleting teacher by ID: {}", id);
        if (id == null) {
            throw new InvalidDataException("ID cannot be null");
        }
        Teacher teacher = findById(id);
        if (!teacher.getTrainingGroups().isEmpty()) {
            logger.error("Cannot delete teacher with ID {} because they are associated with training groups", id);
            throw new InvalidDataException("Cannot delete teacher with ID " + id + " because they are associated with training groups");
        }
        super.deleteById(id);
    }

    /**
     * Finds all teachers in the database.
     *
     * @return a list of all teachers
     */
    @Override
    public List<Teacher> findAll() {
        logger.info("Fetching all teachers");
        return super.findAll();
    }
}
