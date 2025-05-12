package com.denniseckerskorn.services;

import com.denniseckerskorn.exceptions.DuplicateEntityException;
import com.denniseckerskorn.exceptions.EntityNotFoundException;
import com.denniseckerskorn.exceptions.InvalidDataException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Abstract service class providing common CRUD operations for entities.
 *
 * @param <T>  the type of the entity
 * @param <ID> the type of the entity's identifier
 */
public abstract class AbstractService<T, ID> {

    protected final JpaRepository<T, ID> repository;

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Constructor for AbstractService.
     *
     * @param repository the repository to be used for CRUD operations
     */
    public AbstractService(JpaRepository<T, ID> repository) {
        this.repository = repository;
    }

    /**
     * Saves a new entity in the database.
     *
     * @param entity the entity to save
     * @return the saved entity
     * @throws DuplicateEntityException if the entity already exists
     * @throws IllegalArgumentException if the entity is null
     */
    public T save(T entity) throws IllegalArgumentException, DuplicateEntityException {
        if (entity == null) {
            throw new IllegalArgumentException("Entity cannot be null");
        }

        ID id = getEntityId(entity);
        if (id != null && exists(entity)) {
            throw new DuplicateEntityException("Entity with ID " + id + " already exists");
        }

        return repository.save(entity);
    }

    /**
     * Updates an existing entity in the database.
     *
     * @param entity the entity to update
     * @return the updated entity
     * @throws EntityNotFoundException if the entity does not exist
     * @throws IllegalArgumentException if the entity is null
     * @throws InvalidDataException    if the entity is invalid
     */
    @Transactional
    public T update(T entity) throws IllegalArgumentException, InvalidDataException, EntityNotFoundException {
        if (entity == null) {
            throw new IllegalArgumentException("Entity cannot be null");
        }

        ID id = getEntityId(entity);
        if (id == null) {
            throw new InvalidDataException("Cannot update entity: ID is null");
        }

        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Entity with ID " + id + " does not exist");
        }

        return entityManager.merge(entity);
    }

    /**
     * Retrieves the ID of the entity.
     *
     * @param entity the entity from which to retrieve the ID
     * @return the ID of the entity
     * @throws IllegalStateException if the ID cannot be accessed
     */
    @SuppressWarnings("unchecked")
    protected ID getEntityId(T entity) throws IllegalStateException {
        try {
            var idField = entity.getClass().getDeclaredField("id");
            idField.setAccessible(true);
            return (ID) idField.get(entity);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new IllegalStateException("Unable to access entity ID", e);
        }
    }

    /**
     * Finds an entity by its ID.
     *
     * @param id the ID of the entity to find
     * @return the found entity
     * @throws EntityNotFoundException if the entity does not exist
     * @throws InvalidDataException    if the ID is null
     */
    public T findById(ID id) throws InvalidDataException, EntityNotFoundException {
        if (id == null) {
            throw new InvalidDataException("ID cannot be null");
        }
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Entity with ID " + id + " not found"));
    }

    /**
     * Deletes an entity by its ID.
     *
     * @param id the ID of the entity to delete
     * @throws EntityNotFoundException if the entity does not exist
     * @throws InvalidDataException    if the ID is null
     */
    public void deleteById(ID id) throws InvalidDataException, EntityNotFoundException {
        if (id == null) {
            throw new InvalidDataException("ID cannot be null");
        }
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Entity with ID " + id + " not found");
        }
        repository.deleteById(id);
    }

    /**
     * Finds all entities in the database.
     *
     * @return a list of all entities
     */
    public List<T> findAll() {
        return repository.findAll();
    }

    /**
     * Checks if an entity exists in the database.
     *
     * @param entity the entity to check
     * @return true if the entity exists, false otherwise
     */
    protected boolean exists(T entity) {
        ID id = getEntityId(entity);
        return id != null && repository.existsById(id);
    }
}
