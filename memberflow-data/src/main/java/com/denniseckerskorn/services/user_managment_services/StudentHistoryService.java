package com.denniseckerskorn.services.user_managment_services;

import com.denniseckerskorn.entities.user_managment.StudentHistory;
import com.denniseckerskorn.exceptions.DuplicateEntityException;
import com.denniseckerskorn.exceptions.EntityNotFoundException;
import com.denniseckerskorn.exceptions.InvalidDataException;
import com.denniseckerskorn.repositories.user_managment_repositories.StudentHistoryRepository;
import com.denniseckerskorn.services.AbstractService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing StudentHistory entities.
 * This class provides methods to perform CRUD operations on StudentHistory entities.
 */
@Service
public class StudentHistoryService extends AbstractService<StudentHistory, Integer> {
    private final Logger logger = LoggerFactory.getLogger(StudentHistoryService.class);
    private final StudentHistoryRepository studentHistoryRepository;

    /**
     * Constructor for StudentHistoryService.
     *
     * @param studentHistoryRepository the student history repository
     */
    public StudentHistoryService(StudentHistoryRepository studentHistoryRepository) {
        super(studentHistoryRepository);
        this.studentHistoryRepository = studentHistoryRepository;
    }

    /**
     * Saves a new student history in the database.
     *
     * @param entity the entity to save
     * @return the saved entity
     */
    @Override
    public StudentHistory save(StudentHistory entity) throws DuplicateEntityException {
        logger.info("Saving student history: {}", entity);
        if (entity == null || entity.getEventType() == null) {
            throw new InvalidDataException("Student history or event type cannot be null.");
        }
        logger.info("Student history saved: {}", entity);
        return super.save(entity);
    }

    /**
     * Updates an existing student history in the database.
     *
     * @param entity the entity to update
     * @return the updated entity
     * @throws EntityNotFoundException if the entity is not found
     */
    @Transactional
    @Override
    public StudentHistory update(StudentHistory entity) throws EntityNotFoundException, InvalidDataException {
        logger.info("Updating student history: {}", entity);
        if (entity == null || entity.getEventType() == null) {
            throw new InvalidDataException("Student history or event type cannot be null.");
        }
        logger.info("Student history updated: {}", entity);
        return super.update(entity);
    }


    /**
     * Finds a student history by its ID.
     *
     * @param id the ID of the entity to find
     * @return the found entity
     * @throws EntityNotFoundException if the entity is not found
     */
    @Override
    public StudentHistory findById(Integer id) throws EntityNotFoundException {
        logger.info("Finding student history by ID: {}", id);
        return super.findById(id);
    }

    /**
     * Deletes a student history by its ID.
     *
     * @param id the ID of the entity to delete
     * @throws EntityNotFoundException if the entity is not found
     * @throws InvalidDataException    if the ID is null
     */
    @Transactional
    @Override
    public void deleteById(Integer id) throws EntityNotFoundException, InvalidDataException {
        logger.info("Deleting student history by ID: {}", id);
        if (id == null) {
            logger.error("ID cannot be null");
            throw new InvalidDataException("ID cannot be null");
        }
        logger.info("Student history with ID {} deleted successfully", id);
        super.deleteById(id);
    }

    /**
     * Finds all student histories in the database.
     *
     * @return a list of all student histories
     */
    @Override
    public List<StudentHistory> findAll() {
        logger.info("Finding all student histories");
        return super.findAll();
    }

    /**
     * Checks if a student history exists in the database.
     *
     * @param entity the entity to check
     * @return true if the entity exists, false otherwise
     */
    @Override
    protected boolean exists(StudentHistory entity) {
        logger.info("Checking if student history exists: {}", entity);
        return studentHistoryRepository.existsByEventType(entity.getEventType());
    }

}
