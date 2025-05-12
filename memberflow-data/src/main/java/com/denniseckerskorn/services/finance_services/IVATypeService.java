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

@Service
public class IVATypeService extends AbstractService<IVAType, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(IVATypeService.class);
    private final IVATypeRepository ivaTypeRepository;
    private final ProductServiceRepository productServiceRepository;

    public IVATypeService(IVATypeRepository ivaTypeRepository, ProductServiceRepository productServiceRepository) {
        super(ivaTypeRepository);
        this.ivaTypeRepository = ivaTypeRepository;
        this.productServiceRepository = productServiceRepository;
    }

    @Override
    public IVAType save(IVAType entity) throws DuplicateEntityException {
        logger.info("Saving IVAType: {}", entity);
        validate(entity);
        if (ivaTypeRepository.existsByPercentage(entity.getPercentage())) {
            throw new DuplicateEntityException("IVA type with this percentage already exists");
        }
        return super.save(entity);
    }

    @Override
    public IVAType update(IVAType entity) throws InvalidDataException, EntityNotFoundException {
        logger.info("Updating IVAType: {}", entity);
        validate(entity);
        return super.update(entity);
    }

    @Override
    protected boolean exists(IVAType entity) {
        return entity != null && entity.getId() != null && ivaTypeRepository.existsById(entity.getId());
    }

    @Override
    protected Integer getEntityId(IVAType entity) {
        return entity.getId();
    }

    @Override
    public List<IVAType> findAll() {
        logger.info("Retrieving all IVA types");
        return super.findAll();
    }

    @Override
    public void deleteById(Integer id) throws InvalidDataException, EntityNotFoundException {
        if (productServiceRepository.existsByIvaTypeId(id)) {
            throw new InvalidDataException("Cannot delete IVA type in use by a product");
        }
        super.deleteById(id);
    }

    private void validate(IVAType ivaType) {
        if (ivaType.getPercentage() == null || ivaType.getPercentage().compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidDataException("IVA percentage must be >= 0");
        }
        if (ivaType.getDescription() != null && ivaType.getDescription().length() > 50) {
            throw new InvalidDataException("Description is too long");
        }
    }
}
