package com.denniseckerskorn.services.finance_services;

import com.denniseckerskorn.entities.finance.IVAType;
import com.denniseckerskorn.exceptions.DuplicateEntityException;
import com.denniseckerskorn.exceptions.EntityNotFoundException;
import com.denniseckerskorn.exceptions.InvalidDataException;
import com.denniseckerskorn.repositories.finance_repositories.IVATypeRepository;
import com.denniseckerskorn.repositories.finance_repositories.ProductServiceRepository;
import com.denniseckerskorn.services.AbstractService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * Service class for managing IVA types.
 * This class provides methods to perform CRUD operations on IVAType entities.
 */
@Service
public class IVATypeService extends AbstractService<IVAType, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(IVATypeService.class);
    private final IVATypeRepository ivaTypeRepository;
    private final ProductServiceRepository productServiceRepository;

    /**
     * Constructor for IVATypeService.
     *
     * @param ivaTypeRepository        the repository for IVA types
     * @param productServiceRepository the repository for product services
     */
    public IVATypeService(IVATypeRepository ivaTypeRepository, ProductServiceRepository productServiceRepository) {
        super(ivaTypeRepository);
        this.ivaTypeRepository = ivaTypeRepository;
        this.productServiceRepository = productServiceRepository;
    }

    /**
     * Saves a new IVAType entity.
     *
     * @param entity the IVAType to save
     * @return the saved IVAType
     * @throws DuplicateEntityException if an entity with the same percentage already exists
     */
    @Override
    public IVAType save(IVAType entity) throws DuplicateEntityException {
        logger.info("Saving IVAType: {}", entity);
        validate(entity);
        if (ivaTypeRepository.existsByPercentage(entity.getPercentage())) {
            throw new DuplicateEntityException("IVA type with this percentage already exists");
        }
        return super.save(entity);
    }

    /**
     * Updates an existing IVAType entity.
     *
     * @param entity the IVAType to update
     * @return the updated IVAType
     * @throws InvalidDataException    if the entity data is invalid
     * @throws EntityNotFoundException if the entity does not exist
     */
    @Override
    public IVAType update(IVAType entity) throws InvalidDataException, EntityNotFoundException {
        logger.info("Updating IVAType: {}", entity);
        validate(entity);
        return super.update(entity);
    }

    /**
     * Verifies if an IVAType entity exists in the database.
     *
     * @param entity the entity to check
     * @return true if the entity exists, false otherwise
     */
    @Override
    protected boolean exists(IVAType entity) {
        return entity != null && entity.getId() != null && ivaTypeRepository.existsById(entity.getId());
    }

    /**
     * Gets the ID of the IVAType entity.
     *
     * @param entity the IVAType entity
     * @return the ID of the IVAType
     */
    @Override
    protected Integer getEntityId(IVAType entity) {
        return entity.getId();
    }

    /**
     * Finds all IVAType entities.
     *
     * @return a list of all IVAType entities
     */
    @Override
    public List<IVAType> findAll() {
        logger.info("Retrieving all IVA types");
        return super.findAll();
    }

    /**
     * Deletes an IVAType entity by its ID.
     *
     * @param id the ID of the entity to delete
     * @throws InvalidDataException    if the ID is null or invalid
     * @throws EntityNotFoundException if the entity does not exist
     */
    @Override
    public void deleteById(Integer id) throws InvalidDataException, EntityNotFoundException {
        if (productServiceRepository.existsByIvaTypeId(id)) {
            throw new InvalidDataException("Cannot delete IVA type in use by a product");
        }
        super.deleteById(id);
    }

    /**
     * Validates the IVAType entity before saving or updating.
     *
     * @param ivaType the IVAType to validate
     * @throws InvalidDataException if the IVAType data is invalid
     */
    private void validate(IVAType ivaType) {
        if (ivaType.getPercentage() == null || ivaType.getPercentage().compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidDataException("IVA percentage must be >= 0");
        }
        if (ivaType.getDescription() != null && ivaType.getDescription().length() > 50) {
            throw new InvalidDataException("Description is too long");
        }
    }
}
