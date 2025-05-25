package com.denniseckerskorn.services.class_managment_services;

import com.denniseckerskorn.entities.class_managment.TrainingGroup;
import com.denniseckerskorn.entities.class_managment.TrainingSession;
import com.denniseckerskorn.entities.user_managment.users.Student;
import com.denniseckerskorn.exceptions.DuplicateEntityException;
import com.denniseckerskorn.exceptions.EntityNotFoundException;
import com.denniseckerskorn.exceptions.InvalidDataException;
import com.denniseckerskorn.repositories.class_managment_repositories.TrainingGroupRepository;
import com.denniseckerskorn.services.AbstractService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainingGroupService extends AbstractService<TrainingGroup, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(TrainingGroupService.class);
    private final TrainingGroupRepository trainingGroupRepository;

    public TrainingGroupService(TrainingGroupRepository trainingGroupRepository) {
        super(trainingGroupRepository);
        this.trainingGroupRepository = trainingGroupRepository;
    }

    @Override
    public TrainingGroup save(TrainingGroup entity) throws IllegalArgumentException, DuplicateEntityException {
        logger.info("Saving training group: {}", entity);
        if (entity == null) {
            throw new IllegalArgumentException("TrainingGroup cannot be null");
        }
        validateTrainingGroup(entity);
        return super.save(entity);
    }

    @Override
    public TrainingGroup update(TrainingGroup entity) throws IllegalArgumentException, InvalidDataException, EntityNotFoundException {
        logger.info("Updating training group: {}", entity);
        validateTrainingGroup(entity);
        return super.update(entity);
    }

    @Override
    protected Integer getEntityId(TrainingGroup entity) {
        return super.getEntityId(entity);
    }

    @Override
    public TrainingGroup findById(Integer id) throws InvalidDataException, EntityNotFoundException {
        logger.info("Finding training group by ID: {}", id);
        return super.findById(id);
    }

    @Override
    public void deleteById(Integer id) throws InvalidDataException, EntityNotFoundException {
        logger.info("Deleting training group by ID: {}", id);
        TrainingGroup group = findById(id);
        if (group == null) {
            logger.error("Training group with ID {} not found", id);
            throw new EntityNotFoundException("Training group with ID " + id + " not found");
        }
        super.deleteById(id);
    }

    @Override
    public List<TrainingGroup> findAll() {
        logger.info("Finding all training groups");
        return super.findAll();
    }

    @Override
    protected boolean exists(TrainingGroup entity) {
        logger.info("Checking if training group exists: {}", entity);
        return super.exists(entity);
    }

    private void validateTrainingGroup(TrainingGroup group) throws InvalidDataException {
        if (group.getTeacher() == null) {
            throw new InvalidDataException("Teacher cannot be null");
        }
        if (group.getName() == null || group.getName().trim().isEmpty()) {
            throw new InvalidDataException("Group name is required");
        }
        if (group.getSchedule() == null) {
            throw new InvalidDataException("Schedule cannot be null");
        }
    }

    @Transactional
    public void addStudentToGroup(TrainingGroup group, Student student) throws InvalidDataException, EntityNotFoundException {
        if (group == null || student == null) {
            throw new InvalidDataException("Group and student cannot be null");
        }

        TrainingGroup managedGroup = findById(group.getId());

        if (!managedGroup.getStudents().contains(student)) {
            managedGroup.getStudents().add(student);
            student.getTrainingGroups().add(managedGroup); // bidireccional
            trainingGroupRepository.save(managedGroup);
        }
    }

    @Transactional
    public void removeStudentFromGroup(TrainingGroup group, Student student) throws InvalidDataException, EntityNotFoundException {
        if (group == null || student == null) {
            throw new InvalidDataException("Group and student cannot be null");
        }

        TrainingGroup managedGroup = findById(group.getId());

        if (managedGroup.getStudents().contains(student)) {
            managedGroup.getStudents().remove(student);
            student.getTrainingGroups().remove(managedGroup);
            trainingGroupRepository.save(managedGroup);
        }
    }

    @Transactional
    public void addTrainingSessionToGroup(TrainingGroup group, TrainingSession session) throws InvalidDataException, EntityNotFoundException {
        if (group == null || session == null) {
            throw new InvalidDataException("Group and session cannot be null");
        }

        TrainingGroup managedGroup = findById(group.getId());
        session.setTrainingGroup(managedGroup);
        managedGroup.getTrainingSessions().add(session);
        trainingGroupRepository.save(managedGroup);
    }

    @Transactional
    public void removeTrainingSessionFromGroup(TrainingGroup group, TrainingSession session) throws InvalidDataException, EntityNotFoundException {
        if (group == null || session == null) {
            throw new InvalidDataException("Group and session cannot be null");
        }

        TrainingGroup managedGroup = findById(group.getId());
        if (managedGroup.getTrainingSessions().contains(session)) {
            managedGroup.getTrainingSessions().remove(session);
            session.setTrainingGroup(null);
            trainingGroupRepository.save(managedGroup);
        }
    }
}
