package com.denniseckerskorn.services.class_managment_services;

import com.denniseckerskorn.entities.class_managment.Assistance;
import com.denniseckerskorn.exceptions.DuplicateEntityException;
import com.denniseckerskorn.exceptions.EntityNotFoundException;
import com.denniseckerskorn.exceptions.InvalidDataException;
import com.denniseckerskorn.repositories.class_managment_repositories.AssistanceRepository;
import com.denniseckerskorn.services.AbstractService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service class for managing assistance records.
 */
@Service
public class AssistanceService extends AbstractService<Assistance, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(MembershipService.class);
    private final AssistanceRepository assistanceRepository;

    /**
     * Constructor for AssitanceService.
     *
     * @param assistanceRepository the assistance repository
     */
    public AssistanceService(AssistanceRepository assistanceRepository) {
        super(assistanceRepository);
        this.assistanceRepository = assistanceRepository;
    }

    @Override
    public Assistance save(Assistance entity) throws IllegalArgumentException, DuplicateEntityException {
        logger.info("Saving assistance: {}", entity);
        if (entity == null) {
            throw new IllegalArgumentException("Entity cannot be null");
        }
        validateAssistance(entity);
        return super.save(entity);
    }

    @Override
    public Assistance update(Assistance entity) throws IllegalArgumentException, InvalidDataException, EntityNotFoundException {
        logger.info("Updating assistance: {}", entity);
        validateAssistance(entity);
        return super.update(entity);
    }

    @Override
    protected Integer getEntityId(Assistance entity) throws IllegalStateException {
        return super.getEntityId(entity);
    }

    @Override
    public Assistance findById(Integer id) throws InvalidDataException, EntityNotFoundException {
        logger.info("Finding assistance by ID: {}", id);
        if (id == null) {
            logger.error("Assistance ID cannot be null");
            throw new InvalidDataException("Assistance ID cannot be null");
        }
        return super.findById(id);
    }

    @Override
    public void deleteById(Integer id) throws InvalidDataException, EntityNotFoundException {
        logger.info("Deleting assistance by ID: {}", id);
        Assistance assistance = findById(id);
        if (assistance == null) {
            logger.error("Assistance with ID {} not found", id);
            throw new EntityNotFoundException("Assistance with ID " + id + " not found");
        }
        super.deleteById(id);
    }

    @Override
    public List<Assistance> findAll() {
        logger.info("Finding all assistance records");
        return super.findAll();
    }

    @Override
    protected boolean exists(Assistance entity) {
        logger.info("Checking if assistance exists: {}", entity);
        return super.exists(entity);
    }

    /**
     * Validates the assistance entity.
     *
     * @param assistance the assistance to validate
     * @throws InvalidDataException if the assistance is invalid
     */
    private void validateAssistance(Assistance assistance) throws InvalidDataException {
        if (assistance.getStudent() == null) {
            throw new InvalidDataException("Student cannot be null");
        }
        if (assistance.getTrainingSession() == null) {
            throw new InvalidDataException("Training session cannot be null");
        }
        if (assistance.getDate() == null) {
            throw new InvalidDataException("Date cannot be null");
        }
    }
}
