package com.denniseckerskorn.services.class_managment_services;

import com.denniseckerskorn.entities.class_managment.TrainingSession;
import com.denniseckerskorn.entities.user_managment.users.Student;
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

    public TrainingSessionService(TrainingSessionRepository trainingSessionRepository) {
        super(trainingSessionRepository);
        this.trainingSessionRepository = trainingSessionRepository;
    }

    @Override
    public TrainingSession save(TrainingSession entity) throws IllegalArgumentException, DuplicateEntityException {
        logger.info("Saving training session: {}", entity);
        if (entity == null) {
            throw new IllegalArgumentException("TrainingSession cannot be null");
        }
        validateTrainingSession(entity);
        return super.save(entity);
    }

    @Override
    public TrainingSession update(TrainingSession entity) throws IllegalArgumentException, InvalidDataException, EntityNotFoundException {
        logger.info("Updating training session: {}", entity);
        validateTrainingSession(entity);
        return super.update(entity);
    }

    @Override
    protected Integer getEntityId(TrainingSession entity) throws IllegalStateException {
        return super.getEntityId(entity);
    }

    @Override
    public TrainingSession findById(Integer id) throws InvalidDataException, EntityNotFoundException {
        logger.info("Finding training session by ID: {}", id);
        return super.findById(id);
    }

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

    @Override
    public List<TrainingSession> findAll() {
        logger.info("Finding all training sessions");
        return super.findAll();
    }

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

    public Set<Student> getStudentsFromSession(TrainingSession session) throws EntityNotFoundException, InvalidDataException {
        if (session == null || session.getId() == null) {
            throw new InvalidDataException("Session cannot be null");
        }

        TrainingSession managedSession = findById(session.getId());
        return managedSession.getTrainingGroup().getStudents();
    }

    @Transactional
    public void deleteAllAssistancesBySession(Integer sessionId) {
        TrainingSession session = findById(sessionId);

        if (session.getAssistances() != null && !session.getAssistances().isEmpty()) {
            session.getAssistances().clear();
            update(session);
        }
    }


}
