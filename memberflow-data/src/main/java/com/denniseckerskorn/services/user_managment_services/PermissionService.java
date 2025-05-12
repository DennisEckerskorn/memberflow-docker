package com.denniseckerskorn.services.user_managment_services;

import com.denniseckerskorn.entities.user_managment.Permission;
import com.denniseckerskorn.entities.user_managment.Role;
import com.denniseckerskorn.enums.PermissionValues;
import com.denniseckerskorn.exceptions.DuplicateEntityException;
import com.denniseckerskorn.exceptions.EntityNotFoundException;
import com.denniseckerskorn.exceptions.InvalidDataException;
import com.denniseckerskorn.repositories.user_managment_repositories.PermissionRepository;
import com.denniseckerskorn.services.AbstractService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing Permission entities.
 * This class provides methods to perform CRUD operations on Permission entities.
 */
@Service
public class PermissionService extends AbstractService<Permission, Integer> {
    private final static Logger logger = LoggerFactory.getLogger(PermissionService.class);
    private final PermissionRepository permissionRepository;

    /**
     * Constructor for PermissionService.
     *
     * @param permissionRepository the JPA repository
     */
    public PermissionService(PermissionRepository permissionRepository) {
        super(permissionRepository);
        this.permissionRepository = permissionRepository;
    }

    /**
     * Saves a new permission in the database.
     *
     * @param entity the entity to save
     * @return the saved entity
     * @throws DuplicateEntityException if the permission already exists
     * @throws InvalidDataException     if the permission is null or invalid
     */
    @Override
    public Permission save(Permission entity) throws DuplicateEntityException, InvalidDataException {
        logger.info("Attempting to save permission: {}", entity);

        if (entity == null) {
            logger.error("Permission entity is null");
            throw new InvalidDataException("Permission entity cannot be null");
        }

        validatePermission(entity);

        if (permissionRepository.existsByPermissionName(entity.getPermissionName())) {
            logger.error("Permission {} already exists", entity.getPermissionName());
            throw new DuplicateEntityException("Permission already exists: " + entity.getPermissionName());
        }

        Permission saved = permissionRepository.save(entity);
        logger.info("Permission saved successfully: {}", saved);
        return saved;
    }


    /**
     * Updates an existing permission in the database.
     *
     * @param entity the entity to update
     * @return the updated entity
     * @throws EntityNotFoundException if the permission is not found
     */
    @Transactional
    @Override
    public Permission update(Permission entity) throws EntityNotFoundException {
        logger.info("Updating permission: {}", entity);
        if (entity == null) {
            throw new InvalidDataException("Permission cannot be null");
        }
        validatePermission(entity);
        return super.update(entity);
    }

    /**
     * Finds a permission by its ID.
     *
     * @param id the ID of the permission
     * @return the found permission
     * @throws EntityNotFoundException if the permission is not found
     * @throws InvalidDataException    if the ID is null
     */
    @Override
    public Permission findById(Integer id) throws EntityNotFoundException {
        logger.info("Finding permission by ID: {}", id);
        return super.findById(id);
    }

    /**
     * Deletes a permission by its ID.
     *
     * @param id the ID of the permission to delete
     * @throws EntityNotFoundException if the permission is not found
     * @throws InvalidDataException    if the ID is null
     */
    @Override
    public void deleteById(Integer id) throws EntityNotFoundException, InvalidDataException {
        logger.info("Deleting permission by ID: {}", id);
        if (id == null) {
            logger.error("Permission ID cannot be null");
            throw new InvalidDataException("Permission ID cannot be null");
        }
        logger.info("Permission with ID {} deleted successfully", id);
        super.deleteById(id);
    }

    /**
     * Finds all permissions in the database.
     *
     * @return a list of all permissions
     */
    @Override
    public List<Permission> findAll() {
        logger.info("Finding all permissions");
        return super.findAll();
    }

    /**
     * Checks if a permission exists in the database.
     *
     * @param entity the permission entity to check
     * @return true if the permission exists, false otherwise
     */
    @Override
    protected boolean exists(Permission entity) {
        return permissionRepository.existsByPermissionName(entity.getPermissionName());
    }

    /**
     * Finds a permission by its name.
     *
     * @param permissionName the name of the permission
     * @return the found permission
     * @throws EntityNotFoundException if the permission is not found
     * @throws InvalidDataException    if the permission name is null
     */
    public Permission findPermissionByName(PermissionValues permissionName) throws EntityNotFoundException {
        if (permissionName == null) {
            logger.error("Permission name cannot be null");
            throw new InvalidDataException("Permission name cannot be null");
        }

        Permission permission = permissionRepository.findByPermissionName(permissionName);
        if (permission == null) {
            logger.error("Permission not found: {}", permissionName);
            throw new EntityNotFoundException("Permission not found");
        }
        return permission;
    }


    /**
     * Validates the permission entity.
     *
     * @param permission the permission entity to validate
     * @throws InvalidDataException if the permission is invalid
     */
    private void validatePermission(Permission permission) throws InvalidDataException {
        if (permission.getPermissionName() == null || permission.getPermissionName().toString().isEmpty()) {
            logger.error("Permission name cannot be null or empty");
            throw new InvalidDataException("Permission name cannot be null or empty");
        }
    }
}
