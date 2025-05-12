package com.denniseckerskorn.services.finance_services;

import com.denniseckerskorn.entities.finance.InvoiceLine;
import com.denniseckerskorn.exceptions.DuplicateEntityException;
import com.denniseckerskorn.exceptions.EntityNotFoundException;
import com.denniseckerskorn.exceptions.InvalidDataException;
import com.denniseckerskorn.repositories.finance_repositories.InvoiceLineRepository;
import com.denniseckerskorn.services.AbstractService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * Service class for managing InvoiceLine entities.
 * This class provides methods to perform CRUD operations on InvoiceLine entities.
 */
@Service
public class InvoiceLineService extends AbstractService<InvoiceLine, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(InvoiceLineService.class);
    private final InvoiceLineRepository invoiceLineRepository;

    public InvoiceLineService(InvoiceLineRepository invoiceLineRepository) {
        super(invoiceLineRepository);
        this.invoiceLineRepository = invoiceLineRepository;
    }

    @Override
    public InvoiceLine save(InvoiceLine entity) throws IllegalArgumentException, DuplicateEntityException {
        logger.info("Saving invoice line: {}", entity);
        validateInvoiceLine(entity);
        return super.save(entity);
    }

    @Override
    public InvoiceLine update(InvoiceLine entity) throws IllegalArgumentException, InvalidDataException, EntityNotFoundException {
        logger.info("Updating invoice line: {}", entity);
        validateInvoiceLine(entity);
        return super.update(entity);
    }

    @Override
    public List<InvoiceLine> findAll() {
        logger.info("Retrieving all invoice lines");
        return super.findAll();
    }

    @Override
    protected boolean exists(InvoiceLine entity) {
        return entity != null && entity.getId() != null && invoiceLineRepository.existsById(entity.getId());
    }

    @Override
    protected Integer getEntityId(InvoiceLine entity) {
        return entity.getId();
    }

    private void validateInvoiceLine(InvoiceLine line) {
        if (line.getInvoice() == null) {
            throw new InvalidDataException("InvoiceLine must be linked to an invoice");
        }
        if (line.getProductService() == null) {
            throw new InvalidDataException("InvoiceLine must be linked to a product/service");
        }
        if (line.getQuantity() == null || line.getQuantity().intValue() <= 0) {
            throw new InvalidDataException("Quantity must be greater than 0");
        }
        if (line.getUnitPrice() == null || line.getUnitPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidDataException("Unit price must be greater than 0");
        }

        // Validación automática de subtotal
        BigDecimal expected = line.getUnitPrice().multiply(new BigDecimal(line.getQuantity()));
        line.setSubtotal(expected);
    }
}
