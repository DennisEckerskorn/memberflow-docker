package com.denniseckerskorn.services.user_managment_services;

import com.denniseckerskorn.entities.user_managment.Permission;
import com.denniseckerskorn.entities.user_managment.Role;
import com.denniseckerskorn.exceptions.EntityNotFoundException;
import com.denniseckerskorn.exceptions.InvalidDataException;
import com.denniseckerskorn.repositories.user_managment_repositories.RoleRepository;
import com.denniseckerskorn.services.AbstractService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing Role entities.
 * This class provides methods to perform CRUD operations on Role entities.
 */
@Service
public class RoleService extends AbstractService<Role, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(RoleService.class);
    private final RoleRepository roleRepository;

    /**
     * Constructor for RoleService.
     *
     * @param roleRepository the role repository
     */
    public RoleService(RoleRepository roleRepository) {
        super(roleRepository);
        this.roleRepository = roleRepository;
    }

    /**
     * Saves a new role in the database.
     *
     * @param role the role to save
     * @return the saved role
     * @throws InvalidDataException if the role already exists
     */
    @Override
    public Role save(Role role) throws InvalidDataException {
        logger.info("Saving role: {}", role);
        validateRole(role);
        return super.save(role);
    }


    /**
     * Finds a role by its ID.
     *
     * @param id the ID of the role to find
     * @return the found role
     * @throws EntityNotFoundException if the role is not found
     */
    @Override
    public Role findById(Integer id) throws EntityNotFoundException {
        return super.findById(id);
    }

    /**
     * Finds a role by its name.
     *
     * @param roleName the name of the role to find
     * @return the found role
     */
    public Role findRoleByName(String roleName) {
        return roleRepository.findByName(roleName);
    }

    /**
     * Deletes a role by its ID.
     *
     * @param id the ID of the role to delete
     * @throws EntityNotFoundException if the role is not found
     * @throws InvalidDataException if the ID is null
     */
    @Transactional
    @Override
    public void deleteById(Integer id) throws EntityNotFoundException, InvalidDataException {
        logger.info("Deleting role by ID: {}", id);
        if (id == null) {
            logger.error("Role ID cannot be null");
            throw new InvalidDataException("Role ID cannot be null");
        }
        logger.info("Role with ID {} deleted successfully", id);
        super.deleteById(id);
    }

    /**
     * Finds all roles in the database.
     *
     * @return a list of all roles
     */
    @Override
    public List<Role> findAll() {
        return super.findAll();
    }

    /**
     * Checks if a role already exists in the database.
     *
     * @param role the role to check
     * @return true if the role already exists, false otherwise
     */
    @Override
    protected boolean exists(Role role) {
        return roleRepository.findByName(role.getName()) != null;
    }

    /**
     * Validates the role data.
     *
     * @param role the role to validate
     * @throws InvalidDataException if the role data is invalid
     */
    private void validateRole(Role role) throws InvalidDataException {
        if (role.getName() == null || role.getName().isEmpty()) {
            logger.error("Role name cannot be null or empty");
            throw new InvalidDataException("Role name cannot be null or empty");
        }
    }

    /**
     * Adds a permission to a role.
     *
     * @param role       the role to which the permission will be added
     * @param permission the permission to add
     */
    @Transactional
    public void addPermissionToRole(Role role, Permission permission) {
        logger.info("Adding permission {} to role {}", permission, role);

        if (role == null || permission == null) {
            logger.error("Role or Permission cannot be null");
            throw new InvalidDataException("Role or Permission cannot be null");
        }

        Role managedRole = findById(role.getId());

        if (managedRole.getPermissions().contains(permission)) {
            logger.warn("Role already has this permission");
            return;
        }

        managedRole.addPermission(permission);
        roleRepository.save(managedRole);
        logger.info("Permission successfully added to role");
    }

    /**
     * Removes a permission from a role.
     *
     * @param role       the role from which the permission will be removed
     * @param permission the permission to remove
     */
    @Transactional
    public void removePermissionFromRole(Role role, Permission permission) {
        logger.info("Removing permission {} from role {}", permission, role);

        if (role == null || permission == null) {
            logger.error("Role or Permission cannot be null");
            throw new InvalidDataException("Role or Permission cannot be null");
        }
        Role managedRole = findById(role.getId());

        if (!managedRole.getPermissions().contains(permission)) {
            logger.warn("Role does not have this permission");
            return;
        }

        managedRole.removePermission(permission);
        roleRepository.save(managedRole);
        logger.info("Permission successfully removed from role");
    }

    /**
     * Updates an existing role in the database.
     *
     * @param entity the entity to update
     * @return the updated entity
     * @throws EntityNotFoundException if the entity is not found
     */
    @Transactional
    @Override
    public Role update(Role entity) throws EntityNotFoundException {
        return super.update(entity);
    }
}