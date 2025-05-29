package com.denniseckerskorn.services.class_managment_services;

import com.denniseckerskorn.entities.class_managment.TrainingGroup;
import com.denniseckerskorn.entities.class_managment.TrainingSession;
import com.denniseckerskorn.entities.user_managment.users.Student;
import com.denniseckerskorn.enums.StatusValues;
import com.denniseckerskorn.exceptions.DuplicateEntityException;
import com.denniseckerskorn.exceptions.EntityNotFoundException;
import com.denniseckerskorn.exceptions.InvalidDataException;
import com.denniseckerskorn.repositories.class_managment_repositories.TrainingSessionRepository;
import com.denniseckerskorn.services.AbstractService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * Service class for managing training sessions.
 */
@Service
public class TrainingSessionService extends AbstractService<TrainingSession, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(TrainingSessionService.class);
    private final TrainingSessionRepository trainingSessionRepository;

    /**
     * Constructor for TrainingSessionService.
     *
     * @param trainingSessionRepository the repository for training sessions
     */
    public TrainingSessionService(TrainingSessionRepository trainingSessionRepository) {
        super(trainingSessionRepository);
        this.trainingSessionRepository = trainingSessionRepository;
    }

    /**
     * Saves a new TrainingSession entity.
     *
     * @param entity the training session to save
     * @return the saved training session
     * @throws IllegalArgumentException if the training session is null
     * @throws DuplicateEntityException if the training session already exists
     */
    @Override
    public TrainingSession save(TrainingSession entity) throws IllegalArgumentException, DuplicateEntityException {
        logger.info("Saving training session: {}", entity);
        if (entity == null) {
            throw new IllegalArgumentException("TrainingSession cannot be null");
        }
        validateTrainingSession(entity);
        return super.save(entity);
    }

    /**
     * Updates an existing TrainingSession entity.
     *
     * @param entity the training session to update
     * @return the updated training session
     * @throws IllegalArgumentException if the training session is null
     * @throws InvalidDataException     if the training session data is invalid
     * @throws EntityNotFoundException  if the training session does not exist
     */
    @Override
    public TrainingSession update(TrainingSession entity) throws IllegalArgumentException, InvalidDataException, EntityNotFoundException {
        logger.info("Updating training session: {}", entity);
        validateTrainingSession(entity);
        return super.update(entity);
    }

    /**
     * Gets the ID of the training session entity.
     *
     * @param entity the training session entity
     * @return the ID of the training session
     * @throws IllegalStateException if the entity is not managed
     */
    @Override
    protected Integer getEntityId(TrainingSession entity) throws IllegalStateException {
        return super.getEntityId(entity);
    }

    /**
     * Finds a training session by its ID.
     *
     * @param id the ID of the training session
     * @return the found training session
     * @throws InvalidDataException    if the ID is invalid
     * @throws EntityNotFoundException if the training session does not exist
     */
    @Override
    public TrainingSession findById(Integer id) throws InvalidDataException, EntityNotFoundException {
        logger.info("Finding training session by ID: {}", id);
        return super.findById(id);
    }

    /**
     * Deletes a training session by its ID.
     *
     * @param id the ID of the training session to delete
     * @throws InvalidDataException    if the ID is invalid
     * @throws EntityNotFoundException if the training session does not exist
     */
    @Override
    public void deleteById(Integer id) throws InvalidDataException, EntityNotFoundException {
        logger.info("Deleting training session by ID: {}", id);
        TrainingSession session = findById(id);
        if (session == null) {
            logger.error("Training session with ID {} not found", id);
            throw new EntityNotFoundException("Training session with ID " + id + " not found");
        }
        super.deleteById(id);
    }

    /**
     * Finds all training sessions.
     *
     * @return a list of all training sessions
     */
    @Override
    public List<TrainingSession> findAll() {
        logger.info("Finding all training sessions");
        return super.findAll();
    }

    /**
     * Checks if a training session exists in the database.
     *
     * @param entity the training session to check
     * @return true if the training session exists, false otherwise
     */
    @Override
    protected boolean exists(TrainingSession entity) {
        logger.info("Checking if training session exists: {}", entity);
        return super.exists(entity);
    }

    /**
     * Validates the training session entity.
     *
     * @param session the training session to validate
     * @throws InvalidDataException if the session is invalid
     */
    private void validateTrainingSession(TrainingSession session) throws InvalidDataException {
        if (session.getTrainingGroup() == null) {
            throw new InvalidDataException("Training group cannot be null");
        }
        if (session.getStatus() == null) {
            throw new InvalidDataException("Status cannot be null");
        }
    }

    /**
     * Retrieves all students from a specific training session.
     *
     * @param session the training session
     * @return a set of students enrolled in the session
     * @throws EntityNotFoundException if the session does not exist
     * @throws InvalidDataException    if the session is null or has no ID
     */
    public Set<Student> getStudentsFromSession(TrainingSession session) throws EntityNotFoundException, InvalidDataException {
        if (session == null || session.getId() == null) {
            throw new InvalidDataException("Session cannot be null");
        }

        TrainingSession managedSession = findById(session.getId());
        return managedSession.getTrainingGroup().getStudents();
    }

    /**
     * Deletes all assistances for a specific training session.
     *
     * @param sessionId the ID of the training session
     * @throws EntityNotFoundException if the session does not exist
     * @throws InvalidDataException    if the session ID is null
     */
    @Transactional
    public void deleteAllAssistancesBySession(Integer sessionId) {
        logger.info("Deleting all assistances for session ID: {}", sessionId);
        TrainingSession session = findById(sessionId);

        if (session.getAssistances() != null && !session.getAssistances().isEmpty()) {
            session.getAssistances().clear();
            update(session);
        }
        logger.info("All assistances for session ID: {} have been deleted", sessionId);
    }

    /**
     * Generates recurring training sessions for a given training group.
     *
     * @param group  the training group for which to generate sessions
     * @param months the number of months for which to generate sessions
     * @throws InvalidDataException if the group is null or has no schedule
     */
    @Transactional
    public void generateRecurringSession(TrainingGroup group, int months) {
        logger.info("Generating recurring sessions for group: {} for {} months", group.getName(), months);
        LocalDateTime baseDate = group.getSchedule();

        for (int i = 0; i < months * 4; i++) {
            LocalDateTime sessionDate = baseDate.plusWeeks(i);
            TrainingSession newSession = new TrainingSession();
            newSession.setDate(sessionDate);
            newSession.setStatus(StatusValues.ACTIVE);
            newSession.setTrainingGroup(group);
            save(newSession);
            logger.info("Created new session: {} for group: {}", newSession.getId(), group.getName());
        }
    }
}
