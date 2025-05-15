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

@Service
public class ProductServiceService extends AbstractService<ProductService, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(ProductServiceService.class);
    private final ProductServiceRepository productServiceRepository;
    private final IVATypeService ivaTypeService;

    public ProductServiceService(ProductServiceRepository productServiceRepository, IVATypeService ivaTypeService) {
        super(productServiceRepository);
        this.productServiceRepository = productServiceRepository;
        this.ivaTypeService = ivaTypeService;
    }

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
        if (product.getStatus() == null) {
            throw new InvalidDataException("Product status cannot be null");
        }
        if (product.getIvaType() == null || product.getIvaType().getId() == null) {
            throw new IllegalArgumentException("Product must have an IVA type assigned");
        }
    }
}
