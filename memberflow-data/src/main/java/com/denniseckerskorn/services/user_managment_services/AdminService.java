package com.denniseckerskorn.services.user_managment_services;

import com.denniseckerskorn.entities.user_managment.users.Admin;
import com.denniseckerskorn.exceptions.DuplicateEntityException;
import com.denniseckerskorn.exceptions.EntityNotFoundException;
import com.denniseckerskorn.exceptions.InvalidDataException;
import com.denniseckerskorn.repositories.user_managment_repositories.AdminRepository;
import com.denniseckerskorn.repositories.user_managment_repositories.UserRepository;
import com.denniseckerskorn.services.AbstractService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing Admin entities.
 * This class provides methods to perform CRUD operations on Admin entities.
 */
@Service
public class AdminService extends AbstractService<Admin, Integer> {

    private final static Logger logger = LoggerFactory.getLogger(AdminService.class);
    private final AdminRepository adminRepository;
    private final UserRepository userRepository;

    /**
     * Constructor for AdminService.
     *
     * @param adminRepository the admin repository
     */
    public AdminService(AdminRepository adminRepository, UserRepository userRepository) {
        super(adminRepository);
        this.adminRepository = adminRepository;
        this.userRepository = userRepository;
    }

    /**
     * Checks if an admin exists in the database.
     *
     * @param entity the entity to check
     * @return true if the admin exists, false otherwise
     */
    @Override
    protected boolean exists(Admin entity) {
        return entity != null && entity.getId() != null && adminRepository.existsById(entity.getId());
    }

    /**
     * Saves a new admin in the database.
     *
     * @param admin the entity to save
     * @return the saved entity
     * @throws DuplicateEntityException if the admin already exists
     * @throws InvalidDataException     if the admin or its user is null
     */
    @Override
    public Admin save(Admin admin) throws DuplicateEntityException, InvalidDataException {
        logger.info("Saving admin: {}", admin);
        if (admin == null) {
            throw new InvalidDataException("Admin cannot be null.");
        } else if (admin.getUser() == null) {
            throw new InvalidDataException("Admin must be associated with a user.");
        }

        userRepository.save(admin.getUser());

        return super.save(admin);
    }

    /**
     * Updates an existing admin in the database.
     *
     * @param entity the entity to update
     * @return the updated entity
     * @throws EntityNotFoundException if the entity is not found
     * @throws InvalidDataException    if the entity is invalid
     */
    @Transactional
    @Override
    public Admin update(Admin entity) throws EntityNotFoundException, InvalidDataException {
        logger.info("Updating admin: {}", entity);
        if (entity == null) {
            throw new InvalidDataException("Admin cannot be null.");
        }
        logger.info("Admin updated successfully: {}", entity);
        return super.update(entity);
    }

    /**
     * Finds an admin by ID.
     *
     * @param id the ID of the entity to find
     * @return the found entity
     * @throws EntityNotFoundException if the entity is not found
     */
    @Override
    public Admin findById(Integer id) throws EntityNotFoundException {
        logger.info("Fetching admin by ID: {}", id);
        return super.findById(id);
    }

    /**
     * Deletes an admin by ID.
     *
     * @param id the ID of the entity to delete
     * @throws EntityNotFoundException if the entity is not found
     */
    @Transactional
    @Override
    public void deleteById(Integer id) throws EntityNotFoundException {
        logger.info("Deleting admin by ID: {}", id);
        super.deleteById(id);
    }

    /**
     * Finds all admins in the database.
     *
     * @return a list of all admins
     */
    @Override
    public List<Admin> findAll() {
        List<Admin> admins = super.findAll();
        logger.info("Found {} admins", admins.size());
        return admins;
    }
}
