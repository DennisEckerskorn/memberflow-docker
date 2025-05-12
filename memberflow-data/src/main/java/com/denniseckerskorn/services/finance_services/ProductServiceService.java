package com.denniseckerskorn.services.finance_services;

import com.denniseckerskorn.entities.finance.ProductService;
import com.denniseckerskorn.exceptions.DuplicateEntityException;
import com.denniseckerskorn.exceptions.EntityNotFoundException;
import com.denniseckerskorn.exceptions.InvalidDataException;
import com.denniseckerskorn.repositories.finance_repositories.ProductServiceRepository;
import com.denniseckerskorn.services.AbstractService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductServiceService extends AbstractService<ProductService, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(ProductServiceService.class);
    private final ProductServiceRepository productServiceRepository;

    public ProductServiceService(ProductServiceRepository productServiceRepository) {
        super(productServiceRepository);
        this.productServiceRepository = productServiceRepository;
    }

    @Override
    public ProductService save(ProductService entity) throws IllegalArgumentException, DuplicateEntityException {
        logger.info("Saving product/service: {}", entity);
        validateProduct(entity);
        if (productServiceRepository.existsByName(entity.getName())) {
            throw new DuplicateEntityException("Product/Service with this name already exists");
        }
        return super.save(entity);
    }

    @Override
    public ProductService update(ProductService entity) throws IllegalArgumentException, InvalidDataException, EntityNotFoundException {
        logger.info("Updating product/service: {}", entity);
        validateProduct(entity);
        return super.update(entity);
    }

    @Override
    public List<ProductService> findAll() {
        logger.info("Retrieving all products/services");
        return super.findAll();
    }

    @Override
    protected boolean exists(ProductService entity) {
        return entity != null && entity.getId() != null && productServiceRepository.existsById(entity.getId());
    }

    @Override
    protected Integer getEntityId(ProductService entity) {
        return entity.getId();
    }

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
        if (product.getIvaType() == null) {
            throw new InvalidDataException("Product must have an IVA type assigned");
        }
        if (product.getStatus() == null) {
            throw new InvalidDataException("Product status cannot be null");
        }
    }
}
