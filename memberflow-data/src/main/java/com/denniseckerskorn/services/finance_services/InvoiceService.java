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

@Service
public class InvoiceService extends AbstractService<Invoice, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(InvoiceService.class);
    private final InvoiceRepository invoiceRepository;

    public InvoiceService(InvoiceRepository invoiceRepository) {
        super(invoiceRepository);
        this.invoiceRepository = invoiceRepository;
    }

    @Override
    public Invoice save(Invoice entity) throws IllegalArgumentException, DuplicateEntityException {
        logger.info("Saving invoice: {}", entity);
        if (entity == null) {
            throw new IllegalArgumentException("Invoice cannot be null");
        }
        validateInvoice(entity);
        return super.save(entity);
    }

    @Override
    public Invoice update(Invoice entity) throws IllegalArgumentException, InvalidDataException, EntityNotFoundException {
        logger.info("Updating invoice: {}", entity);
        validateInvoice(entity);
        return super.update(entity);
    }

    @Override
    public Invoice findById(Integer id) throws InvalidDataException, EntityNotFoundException {
        logger.info("Finding invoice by ID: {}", id);
        return super.findById(id);
    }

    @Override
    public void deleteById(Integer id) throws InvalidDataException, EntityNotFoundException {
        logger.info("Deleting invoice by ID: {}", id);
        Invoice invoice = findById(id);
        if (invoice == null) {
            throw new EntityNotFoundException("Invoice not found");
        }
        super.deleteById(id);
    }

    @Override
    public List<Invoice> findAll() {
        logger.info("Retrieving all invoices");
        return super.findAll();
    }

    @Override
    protected boolean exists(Invoice entity) {
        return entity != null && entity.getId() != null && invoiceRepository.existsById(entity.getId());
    }

    @Override
    protected Integer getEntityId(Invoice entity) {
        return entity.getId();
    }

    /**
     * Obtiene todas las facturas de un usuario específico.
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
     * Validates the invoice before saving/updating.
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

    public void updateInvoiceTotal(Invoice invoice) {
        BigDecimal total = invoice.getInvoiceLines().stream()
                .map(InvoiceLine::getSubtotal)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        invoice.setTotal(total);
    }


    @Transactional
    public void addLineToInvoiceById(Integer invoiceId, InvoiceLine line) {
        Invoice invoice = findById(invoiceId);
        addLineToInvoice(invoice, line);
    }

    @Transactional
    public void removeLineFromInvoiceById(Integer invoiceId, Integer lineId) {
        Invoice invoice = findById(invoiceId);
        InvoiceLine line = invoice.getInvoiceLines().stream()
                .filter(l -> l.getId().equals(lineId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("InvoiceLine not found"));
        removeLineFromInvoice(invoice, line);
    }

    public void recalculateTotal(Invoice invoice) {
        updateInvoiceTotal(invoice);
        invoiceRepository.save(invoice);
    }


    @Transactional
    public void clearInvoiceLines(Invoice invoice) {
        invoice.getInvoiceLines().clear();
        updateInvoiceTotal(invoice);
        invoiceRepository.save(invoice);
    }
}
