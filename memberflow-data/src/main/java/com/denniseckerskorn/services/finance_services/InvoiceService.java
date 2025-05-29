package com.denniseckerskorn.services.finance_services;

import com.denniseckerskorn.entities.finance.Invoice;
import com.denniseckerskorn.entities.finance.InvoiceLine;
import com.denniseckerskorn.exceptions.DuplicateEntityException;
import com.denniseckerskorn.exceptions.EntityNotFoundException;
import com.denniseckerskorn.exceptions.InvalidDataException;
import com.denniseckerskorn.repositories.finance_repositories.InvoiceRepository;
import com.denniseckerskorn.services.AbstractService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * Service class for managing invoices.
 * This class provides methods to perform CRUD operations on Invoice entities.
 */
@Service
public class InvoiceService extends AbstractService<Invoice, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(InvoiceService.class);
    private final InvoiceRepository invoiceRepository;

    /**
     * Constructor for InvoiceService.
     *
     * @param invoiceRepository the repository for Invoice entities
     */
    public InvoiceService(InvoiceRepository invoiceRepository) {
        super(invoiceRepository);
        this.invoiceRepository = invoiceRepository;
    }

    /**
     * Saves a new invoice in the database.
     *
     * @param entity the invoice to save
     * @return the saved invoice
     * @throws IllegalArgumentException if the invoice is null
     * @throws DuplicateEntityException if an invoice with the same ID already exists
     */
    @Override
    public Invoice save(Invoice entity) throws IllegalArgumentException, DuplicateEntityException {
        logger.info("Saving invoice: {}", entity);
        if (entity == null) {
            throw new IllegalArgumentException("Invoice cannot be null");
        }
        validateInvoice(entity);
        return super.save(entity);
    }

    /**
     * Updates an existing invoice in the database.
     *
     * @param entity the invoice to update
     * @return the updated invoice
     * @throws IllegalArgumentException if the invoice is null
     * @throws InvalidDataException     if the invoice data is invalid
     * @throws EntityNotFoundException  if the invoice does not exist
     */
    @Override
    public Invoice update(Invoice entity) throws IllegalArgumentException, InvalidDataException, EntityNotFoundException {
        logger.info("Updating invoice: {}", entity);
        validateInvoice(entity);
        return super.update(entity);
    }

    /**
     * Finds an invoice by its ID.
     *
     * @param id the ID of the entity to find
     * @return the found invoice
     * @throws InvalidDataException    if the ID is null or invalid
     * @throws EntityNotFoundException if the invoice does not exist
     */
    @Override
    public Invoice findById(Integer id) throws InvalidDataException, EntityNotFoundException {
        logger.info("Finding invoice by ID: {}", id);
        return super.findById(id);
    }

    /**
     * Deletes an invoice by its ID.
     *
     * @param id the ID of the invoice to delete
     * @throws InvalidDataException    if the ID is null or invalid
     * @throws EntityNotFoundException if the invoice does not exist
     */
    @Override
    public void deleteById(Integer id) throws InvalidDataException, EntityNotFoundException {
        logger.info("Deleting invoice by ID: {}", id);
        Invoice invoice = findById(id);
        if (invoice == null) {
            throw new EntityNotFoundException("Invoice not found");
        }
        super.deleteById(id);
    }

    /**
     * Retrieves all invoices.
     *
     * @return a list of all invoices
     */
    @Override
    public List<Invoice> findAll() {
        logger.info("Retrieving all invoices");
        return super.findAll();
    }

    /**
     * Checks if an invoice exists in the database.
     *
     * @param entity the invoice to check
     * @return true if the invoice exists, false otherwise
     */
    @Override
    protected boolean exists(Invoice entity) {
        return entity != null && entity.getId() != null && invoiceRepository.existsById(entity.getId());
    }

    /**
     * Gets the ID of the invoice entity.
     *
     * @param entity the invoice entity
     * @return the ID of the invoice
     */
    @Override
    protected Integer getEntityId(Invoice entity) {
        return entity.getId();
    }

    /**
     * Finds all invoices associated with a specific user ID.
     *
     * @param userId the ID of the user
     * @return a list of invoices associated with the user
     * @throws InvalidDataException if the user ID is null
     */
    public List<Invoice> findAllInvoicesByUserId(Integer userId) throws InvalidDataException {
        if (userId == null) {
            throw new InvalidDataException("User ID cannot be null");
        }
        return invoiceRepository.findAll().stream()
                .filter(invoice -> invoice.getUser() != null && invoice.getUser().getId().equals(userId))
                .toList();
    }


    /**
     * Validates the invoice entity.
     *
     * @param invoice the invoice to validate
     * @throws InvalidDataException if the invoice data is invalid
     */
    private void validateInvoice(Invoice invoice) throws InvalidDataException {
        if (invoice.getUser() == null) {
            throw new InvalidDataException("Invoice must be linked to a user");
        }
        if (invoice.getDate() == null || invoice.getDate().isAfter(LocalDateTime.now())) {
            throw new InvalidDataException("Invoice date must be valid and not in the future");
        }
        if (invoice.getTotal() == null || invoice.getTotal().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidDataException("Invoice total must be greater than 0");
        }
        if (invoice.getStatus() == null) {
            throw new InvalidDataException("Invoice status must not be null");
        }
    }

    /**
     * Adds a line to an invoice.
     *
     * @param invoice the invoice to which the line will be added
     * @param line    the line to add
     * @throws InvalidDataException if the invoice or line is null
     */
    @Transactional
    public void addLineToInvoice(Invoice invoice, InvoiceLine line) {
        if (invoice == null || line == null) {
            throw new InvalidDataException("Invoice and line must not be null");
        }
        line.setInvoice(invoice);
        invoice.getInvoiceLines().add(line);
        updateInvoiceTotal(invoice);
        invoiceRepository.save(invoice);
    }

    /**
     * Removes a line from an invoice.
     *
     * @param invoice the invoice from which the line will be removed
     * @param line    the line to remove
     * @throws InvalidDataException if the invoice or line is null
     */
    @Transactional
    public void removeLineFromInvoice(Invoice invoice, InvoiceLine line) {
        if (invoice == null || line == null) {
            throw new InvalidDataException("Invoice and line must not be null");
        }
        invoice.getInvoiceLines().remove(line);
        line.setInvoice(null);
        updateInvoiceTotal(invoice);
        invoiceRepository.save(invoice);
    }

    /**
     * Updates the total amount of an invoice based on its lines.
     *
     * @param invoice the invoice to update
     */
    public void updateInvoiceTotal(Invoice invoice) {
        BigDecimal total = BigDecimal.ZERO;

        for (InvoiceLine line : invoice.getInvoiceLines()) {
            if (line.getUnitPrice() != null && line.getQuantity() != null) {
                BigDecimal quantity = BigDecimal.valueOf(line.getQuantity());
                BigDecimal base = line.getUnitPrice().multiply(quantity);

                BigDecimal ivaMultiplier = BigDecimal.ONE;
                if (line.getProductService() != null && line.getProductService().getIvaType() != null) {
                    BigDecimal iva = line.getProductService().getIvaType().getPercentage();
                    ivaMultiplier = ivaMultiplier.add(iva.divide(BigDecimal.valueOf(100)));
                }

                BigDecimal lineTotal = base.multiply(ivaMultiplier);
                total = total.add(lineTotal);
            }
        }

        invoice.setTotal(total);
    }

    /**
     * Adds a line to an invoice by its ID.
     *
     * @param invoiceId the ID of the invoice to which the line will be added
     * @param line      the line to add
     */
    @Transactional
    public void addLineToInvoiceById(Integer invoiceId, InvoiceLine line) {
        Invoice invoice = findById(invoiceId);
        addLineToInvoice(invoice, line);
    }

    /**
     * Removes a line from an invoice by its ID.
     *
     * @param invoiceId the ID of the invoice from which the line will be removed
     * @param lineId    the ID of the line to remove
     * @throws EntityNotFoundException if the line does not exist in the invoice
     */
    @Transactional
    public void removeLineFromInvoiceById(Integer invoiceId, Integer lineId) {
        Invoice invoice = findById(invoiceId);
        InvoiceLine line = invoice.getInvoiceLines().stream()
                .filter(l -> l.getId().equals(lineId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("InvoiceLine not found"));
        removeLineFromInvoice(invoice, line);
    }

    /**
     * Recalculates the total of an invoice and saves it.
     *
     * @param invoice the invoice to recalculate
     */
    public void recalculateTotal(Invoice invoice) {
        updateInvoiceTotal(invoice);
        invoiceRepository.save(invoice);
    }
    
    /**
     * Clears all lines from an invoice and updates the total.
     *
     * @param invoice the invoice to clear lines from
     */
    @Transactional
    public void clearInvoiceLines(Invoice invoice) {
        invoice.getInvoiceLines().clear();
        updateInvoiceTotal(invoice);
        invoiceRepository.save(invoice);
    }
}
