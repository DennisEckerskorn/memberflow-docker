package com.denniseckerskorn.services.finance_services;

import com.denniseckerskorn.entities.finance.IVAType;
import com.denniseckerskorn.entities.finance.ProductService;
import com.denniseckerskorn.exceptions.DuplicateEntityException;
import com.denniseckerskorn.exceptions.EntityNotFoundException;
import com.denniseckerskorn.exceptions.InvalidDataException;
import com.denniseckerskorn.repositories.finance_repositories.ProductServiceRepository;
import com.denniseckerskorn.services.AbstractService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * Service class for managing ProductService entities.
 * This class provides methods to perform CRUD operations on ProductService entities.
 */
@Service
public class ProductServiceService extends AbstractService<ProductService, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(ProductServiceService.class);
    private final ProductServiceRepository productServiceRepository;
    private final IVATypeService ivaTypeService;

    /**
     * Constructor for ProductServiceService.
     *
     * @param productServiceRepository the repository for ProductService entities
     * @param ivaTypeService           the service for IVAType entities
     */
    public ProductServiceService(ProductServiceRepository productServiceRepository, IVATypeService ivaTypeService) {
        super(productServiceRepository);
        this.productServiceRepository = productServiceRepository;
        this.ivaTypeService = ivaTypeService;
    }

    /**
     * Saves a new ProductService entity.
     *
     * @param entity the product/service to save
     * @return the saved product/service
     * @throws IllegalArgumentException if the product/service is invalid
     * @throws DuplicateEntityException if a product/service with the same name already exists
     */
    @Override
    @Transactional
    public ProductService save(ProductService entity) throws IllegalArgumentException, DuplicateEntityException {
        logger.info("Saving product/service: {}", entity);

        if (entity.getIvaType() == null || entity.getIvaType().getId() == null) {
            throw new IllegalArgumentException("Product must have an IVA type assigned");
        }

        IVAType ivaType = ivaTypeService.findById(entity.getIvaType().getId());
        entity.setIvaType(ivaType);

        validateProduct(entity);

        ProductService existingProduct = productServiceRepository.findByName(entity.getName());
        if (existingProduct != null && productServiceRepository.existsByName(existingProduct.getName())) {
            throw new DuplicateEntityException("Product/Service with this name already exists");
        }

        return super.save(entity);
    }


    /**
     * Updates an existing ProductService entity.
     *
     * @param entity the product/service to update
     * @return the updated product/service
     * @throws IllegalArgumentException if the product/service is invalid
     * @throws InvalidDataException     if the product/service data is invalid
     * @throws EntityNotFoundException  if the product/service does not exist
     */
    @Override
    @Transactional
    public ProductService update(ProductService entity) throws IllegalArgumentException, InvalidDataException, EntityNotFoundException {
        logger.info("Updating product/service: {}", entity);

        if (entity.getIvaType() == null || entity.getIvaType().getId() == null) {
            throw new IllegalArgumentException("Product must have an IVA type assigned");
        }

        IVAType ivaType = ivaTypeService.findById(entity.getIvaType().getId());
        entity.setIvaType(ivaType);

        validateProduct(entity);

        return super.update(entity);
    }

    /**
     * Finds all ProductService entities.
     *
     * @return a list of all products/services
     */
    @Override
    public List<ProductService> findAll() {
        logger.info("Retrieving all products/services");
        return super.findAll();
    }

    /**
     * Exists method to check if a ProductService entity exists in the repository.
     *
     * @param entity the entity to check
     * @return true if the entity exists, false otherwise
     */
    @Override
    protected boolean exists(ProductService entity) {
        return entity != null && entity.getId() != null && productServiceRepository.existsById(entity.getId());
    }

    /**
     * Gets the ID of a ProductService entity.
     *
     * @param entity the entity from which to retrieve the ID
     * @return the ID of the ProductService entity
     */
    @Override
    protected Integer getEntityId(ProductService entity) {
        return entity.getId();
    }

    /**
     * Validates the ProductService entity before saving or updating.
     *
     * @param product the ProductService to validate
     */
    private void validateProduct(ProductService product) {
        if (product.getName() == null || product.getName().isBlank()) {
            throw new InvalidDataException("Product name cannot be null or blank");
        }
        if (product.getType() == null || product.getType().isBlank()) {
            throw new InvalidDataException("Product type cannot be null or blank");
        }
        if (product.getPrice() == null || product.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidDataException("Product price must be greater than 0");
        }
        if (product.getStatus() == null) {
            throw new InvalidDataException("Product status cannot be null");
        }
        if (product.getIvaType() == null || product.getIvaType().getId() == null) {
            throw new IllegalArgumentException("Product must have an IVA type assigned");
        }
    }
}
