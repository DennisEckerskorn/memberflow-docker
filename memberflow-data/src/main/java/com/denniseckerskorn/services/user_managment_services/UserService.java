package com.denniseckerskorn.services.user_managment_services;

import com.denniseckerskorn.entities.finance.Invoice;
import com.denniseckerskorn.entities.user_managment.Role;
import com.denniseckerskorn.entities.user_managment.users.User;
import com.denniseckerskorn.exceptions.DuplicateEntityException;
import com.denniseckerskorn.exceptions.EntityNotFoundException;
import com.denniseckerskorn.exceptions.InvalidDataException;
import com.denniseckerskorn.repositories.user_managment_repositories.UserRepository;
import com.denniseckerskorn.services.AbstractService;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Service class for managing User entities.
 * This class provides methods to perform CRUD operations on User entities.
 */
@Service
@Primary
public class UserService extends AbstractService<User, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;

    /**
     * Constructor for UserService.
     *
     * @param userRepository the user repository
     */
    public UserService(UserRepository userRepository) {
        super(userRepository);
        this.userRepository = userRepository;


    }

    /**
     * Checks if a user exists in the database.
     *
     * @param entity the entity to check
     * @return true if the user exists, false otherwise
     */
    @Override
    protected boolean exists(User entity) {
        return userRepository.existsByEmail(entity.getEmail());
    }

    /**
     * Saves a new user in the database.
     *
     * @param entity the entity to save
     * @return the saved entity
     * @throws DuplicateEntityException if the entity already exists
     * @throws InvalidDataException     if the entity is invalid
     */
    @Override
    public User save(User entity) throws DuplicateEntityException, InvalidDataException {
        logger.info("Saving user: {}", entity);
        validateUser(entity);
        return super.save(entity);
    }

    /**
     * Updates an existing user in the database.
     *
     * @param entity the entity to update
     * @return the updated entity
     * @throws EntityNotFoundException if the entity is not found
     * @throws InvalidDataException    if the entity is invalid
     */
    @Transactional
    @Override
    public User update(User entity) throws EntityNotFoundException, InvalidDataException {
        logger.info("Updating user: {}", entity);
        validateUser(entity);
        return super.update(entity);
    }

    /**
     * Finds a user by ID.
     *
     * @param userId the ID of the user to find
     * @throws EntityNotFoundException if the user is not found
     * @throws InvalidDataException    if the user ID is invalid
     */
    @Transactional
    @Override
    public void deleteById(Integer userId) throws EntityNotFoundException, InvalidDataException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));

        logger.info("Deleting user with ID: {}", userId);
        super.deleteById(userId);
    }

    /**
     * Finds all user by role name.
     *
     * @param roleName the name of the role
     * @return a list of users with the specified role
     */
    public List<User> findAllUserByRoleName(String roleName) {
        logger.info("Fetching all users with role: {}", roleName);
        return userRepository.findAllByRoleName(roleName);
    }

    /**
     * Finds a user by email.
     *
     * @param email the email of the user to find
     * @return the found user
     * @throws EntityNotFoundException if the user is not found
     */
    public User findByEmail(String email) throws EntityNotFoundException, InvalidDataException {
        logger.info("Fetching user by email: {}", email);
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new EntityNotFoundException("User not found with email: " + email);
        }
        return user;
    }

    /**
     * Assigns a role to a user.
     *
     * @param userId the ID of the user
     * @param role   the role to assign
     * @throws EntityNotFoundException if the user is not found
     * @throws InvalidDataException    if the role is null
     */
    @Transactional
    public void assignRoleToUser(Integer userId, Role role) throws EntityNotFoundException {
        if (role == null) {
            throw new InvalidDataException("Role cannot be null when assigning to user");
        }
        User user = findById(userId);
        logger.info("Assigning role {} to user with ID: {}", role, userId);
        user.setRole(role);
        userRepository.save(user);
    }

    /**
     * Checks if a user exists by email.
     *
     * @param email the email of the user to check
     * @return true if the user exists, false otherwise
     */
    public boolean existsByEmail(String email) {
        logger.info("Checking if user exists with email: {}", email);
        return userRepository.existsByEmail(email);
    }

    /**
     * Adds an invoice to a user.
     *
     * @param user    the user to whom the invoice will be added
     * @param invoice the invoice to add
     * @throws InvalidDataException if the user or invoice is null
     */
    @Transactional
    public void addInvoiceToUser(User user, Invoice invoice) throws InvalidDataException {
        if (user == null || invoice == null) {
            throw new InvalidDataException("User and invoice cannot be null");
        }

        invoice.setUser(user);
        user.getInvoices().add(invoice);
        userRepository.save(user);
    }

    /**
     * Removes an invoice from a user.
     *
     * @param user    the user from whom the invoice will be removed
     * @param invoice the invoice to remove
     * @throws InvalidDataException if the user or invoice is null
     */
    @Transactional
    public void removeInvoiceFromUser(User user, Invoice invoice) throws InvalidDataException {
        if (user == null || invoice == null) {
            throw new InvalidDataException("User and invoice cannot be null");
        }

        user.getInvoices().remove(invoice);
        invoice.setUser(null);
        userRepository.save(user);
    }


    /**
     * Validates the user data.
     *
     * @param user the user to validate
     * @throws InvalidDataException if the user data is invalid
     */
    private void validateUser(User user) throws InvalidDataException {
        if (user == null) {
            throw new InvalidDataException("User cannot be null");
        }
        if (user.getRole() == null) {
            throw new InvalidDataException("User role cannot be null");
        }
        if (user.getName() == null || user.getName().isEmpty()) {
            throw new InvalidDataException("User name cannot be null or empty");
        }
        if (user.getSurname() == null || user.getSurname().isEmpty()) {
            throw new InvalidDataException("User surname cannot be null or empty");
        }
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new InvalidDataException("User email cannot be null or empty");
        }
        if (!user.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new InvalidDataException("Invalid email format");
        }
        if (user.getPhoneNumber() == null || user.getPhoneNumber().isEmpty()) {
            throw new InvalidDataException("User phone number cannot be null or empty");
        }
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new InvalidDataException("User password cannot be null or empty");
        }
        if (user.getPassword().length() < 8) {
            throw new InvalidDataException("User password must be at least 8 characters long");
        }
        if (user.getStatus() == null) {
            throw new InvalidDataException("User status cannot be null");
        }
    }
}