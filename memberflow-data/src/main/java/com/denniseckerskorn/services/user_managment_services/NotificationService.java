package com.denniseckerskorn.services.user_managment_services;

import com.denniseckerskorn.entities.user_managment.Notification;
import com.denniseckerskorn.entities.user_managment.users.User;
import com.denniseckerskorn.exceptions.DuplicateEntityException;
import com.denniseckerskorn.exceptions.EntityNotFoundException;
import com.denniseckerskorn.exceptions.InvalidDataException;
import com.denniseckerskorn.repositories.user_managment_repositories.NotificationRepository;
import com.denniseckerskorn.services.AbstractService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing Notification entities.
 * This class provides methods to perform CRUD operations on Notification entities.
 */
@Service
public class NotificationService extends AbstractService<Notification, Integer> {
    private final static Logger logger = LoggerFactory.getLogger(NotificationService.class);
    private final NotificationRepository notificationRepository;

    /**
     * Constructor for AbstractService.
     *
     * @param notificationRepository the JPA repository
     */
    public NotificationService(NotificationRepository notificationRepository) {
        super(notificationRepository);
        this.notificationRepository = notificationRepository;
    }

    /**
     * Saves a new notification in the database.
     *
     * @param entity the entity to save
     * @return the saved entity
     * @throws DuplicateEntityException if the notification already exists
     */
    @Override
    public Notification save(Notification entity) throws DuplicateEntityException, InvalidDataException {
        logger.info("Saving notification: {}", entity);
        if (entity == null) {
            throw new InvalidDataException("Notification cannot be null");
        }
        if (entity.getTitle() == null || entity.getShippingDate() == null) {
            throw new InvalidDataException("Notification title and shipping date cannot be null");
        }
        return super.save(entity);
    }


    /**
     * Finds a notification by its ID.
     *
     * @param id the ID of the notification
     * @return the found notification
     * @throws EntityNotFoundException if the notification is not found
     * @throws InvalidDataException    if the ID is null
     */
    @Override
    public Notification findById(Integer id) throws EntityNotFoundException, InvalidDataException {
        logger.info("Finding notification by ID: {}", id);
        return super.findById(id);
    }

    /**
     * Updates an existing notification.
     *
     * @param entity the notification to update
     * @return the updated notification
     * @throws EntityNotFoundException if the notification is not found
     * @throws InvalidDataException    if the entity data is invalid
     */
    @Transactional
    @Override
    public Notification update(Notification entity) throws EntityNotFoundException {
        logger.info("Updating notification: {}", entity);
        if (entity == null) {
            logger.error("Notification entity cannot be null");
            throw new EntityNotFoundException("Notification entity cannot be null");
        }
        logger.info("Notification updated: {}", entity);
        return super.update(entity);
    }

    /**
     * Deletes a notification by its ID.
     *
     * @param id the ID of the notification to delete
     * @throws EntityNotFoundException if the notification is not found
     * @throws InvalidDataException    if the ID is null
     */
    @Transactional
    @Override
    public void deleteById(Integer id) throws EntityNotFoundException, InvalidDataException {
        logger.info("Deleting notification by ID: {}", id);
        if (id == null) {
            logger.error("Notification ID cannot be null");
            throw new EntityNotFoundException("Notification ID cannot be null");
        }
        if (!notificationRepository.existsById(id)) {
            logger.error("Notification with ID {} not found", id);
            throw new EntityNotFoundException("Notification with ID " + id + " not found");
        }
        logger.info("Notification with ID {} deleted", id);
        super.deleteById(id);
    }

    /**
     * Finds all notifications.
     *
     * @return a list of all notifications
     */
    @Override
    public List<Notification> findAll() {
        logger.info("Finding all notifications");
        return super.findAll();
    }

    /**
     * Finds notifications by user ID.
     *
     * @param id the ID of the user
     * @return a list of notifications for the user
     */
    public List<Notification> findNotificationsByUserId(Integer id) throws InvalidDataException {
        logger.info("Finding notifications by user ID: {}", id);
        if (id == null) {
            throw new InvalidDataException("User ID cannot be null");
        }
        return notificationRepository.findNotificationsById(id);
    }

    /**
     * Checks if a notification exists.
     *
     * @param entity the notification to check
     * @return true if the notification exists, false otherwise
     */
    @Override
    protected boolean exists(Notification entity) {
        return entity != null &&
                entity.getTitle() != null &&
                entity.getShippingDate() != null &&
                notificationRepository.existsByTitleAndShippingDate(entity.getTitle(), entity.getShippingDate());
    }

    /**
     * Adds a notification to a user.
     *
     * @param notification the notification to add
     * @param user         the user to add the notification to
     */
    @Transactional
    public void addNotificationToUser(Notification notification, User user) {
        logger.info("Adding notification to user: {}", user);
        if (notification == null || user == null) {
            throw new InvalidDataException("Notification and user cannot be null");
        }

        Notification managedNotification = findById(notification.getId());
        if (!managedNotification.getUsers().contains(user)) {
            managedNotification.addUser(user);
            notificationRepository.save(managedNotification);
            logger.info("Notification added to user");
        } else {
            logger.warn("User already has this notification");
        }
    }

    /**
     * Removes a notification from a user.
     *
     * @param notification the notification to remove
     * @param user         the user to remove the notification from
     */
    @Transactional
    public void removeNotificationFromUser(Notification notification, User user) {
        logger.info("Removing notification from user: {}", user);
        if (notification == null || user == null) {
            throw new InvalidDataException("Notification and user cannot be null");
        }

        Notification managedNotification = findById(notification.getId());
        if (managedNotification.getUsers().contains(user)) {
            managedNotification.removeUser(user);
            notificationRepository.save(managedNotification);
            logger.info("Notification removed from user");
        } else {
            logger.warn("User does not have this notification");
        }
    }

}
