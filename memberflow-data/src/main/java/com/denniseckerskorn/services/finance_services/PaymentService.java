package com.denniseckerskorn.services.finance_services;

import com.denniseckerskorn.entities.finance.Invoice;
import com.denniseckerskorn.entities.finance.Payment;
import com.denniseckerskorn.enums.StatusValues;
import com.denniseckerskorn.exceptions.DuplicateEntityException;
import com.denniseckerskorn.exceptions.EntityNotFoundException;
import com.denniseckerskorn.exceptions.InvalidDataException;
import com.denniseckerskorn.repositories.finance_repositories.PaymentRepository;
import com.denniseckerskorn.services.AbstractService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Service class for managing Payment entities.
 * This class provides methods to perform CRUD operations on Payment entities.
 */
@Service
public class PaymentService extends AbstractService<Payment, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);
    private final PaymentRepository paymentRepository;
    private final InvoiceService invoiceService;

    /**
     * Constructor for PaymentService.
     *
     * @param paymentRepository the repository for Payment entities
     * @param invoiceService    the service for Invoice entities
     */
    public PaymentService(PaymentRepository paymentRepository, InvoiceService invoiceService) {
        super(paymentRepository);
        this.paymentRepository = paymentRepository;
        this.invoiceService = invoiceService;
    }

    /**
     * Saves a new Payment entity.
     *
     * @param payment the payment to save
     * @return the saved payment
     * @throws DuplicateEntityException if a payment already exists for the invoice
     * @throws EntityNotFoundException  if the associated invoice does not exist
     * @throws InvalidDataException     if the payment data is invalid
     */
    @Override
    @Transactional
    public Payment save(Payment payment) throws DuplicateEntityException {
        logger.info("Saving payment: {}", payment);
        validate(payment);

        Integer invoiceId = payment.getInvoice().getId();
        if (paymentRepository.existsByInvoiceId(invoiceId)) {
            throw new DuplicateEntityException("A payment already exists for this invoice");
        }

        Invoice invoice = invoiceService.findById(invoiceId);
        if (invoice == null) {
            throw new EntityNotFoundException("Invoice not found");
        }

        payment.setInvoice(invoice);

        Payment savedPayment = super.save(payment);

        invoice.setStatus(StatusValues.PAID);
        invoiceService.update(invoice);

        return savedPayment;
    }

    /**
     * Updates an existing Payment entity.
     *
     * @param payment the entity to update
     * @return the updated payment
     * @throws EntityNotFoundException if the payment does not exist
     * @throws InvalidDataException    if the payment data is invalid
     */
    @Override
    @Transactional
    public Payment update(Payment payment) throws EntityNotFoundException, InvalidDataException {
        logger.info("Updating payment: {}", payment);
        validate(payment);

        Payment existingPayment = findById(payment.getId());

        if (existingPayment == null) {
            throw new EntityNotFoundException("Payment not found");
        }

        Invoice invoice = payment.getInvoice();

        if (invoice == null || invoiceService.findById(invoice.getId()) == null) {
            throw new EntityNotFoundException("Invoice not found for the payment");
        }

        invoice.setStatus(StatusValues.PAID);
        invoiceService.update(invoice);

        return super.update(payment);
    }

    /**
     * Verifies if a Payment entity exists in the database.
     *
     * @param entity the entity to check
     * @return true if the entity exists, false otherwise
     */
    @Override
    protected boolean exists(Payment entity) {
        return entity != null && entity.getId() != null && paymentRepository.existsById(entity.getId());
    }

    /**
     * Obtains the ID of a Payment entity.
     *
     * @param entity the entity from which to retrieve the ID
     * @return the ID of the Payment entity
     */
    @Override
    protected Integer getEntityId(Payment entity) {
        return entity.getId();
    }

    /**
     * Obtains all Payment entities.
     *
     * @return a list of all Payment entities
     */
    @Override
    public List<Payment> findAll() {
        logger.info("Retrieving all payments");
        return super.findAll();
    }

    /**
     * Validates the Payment entity before saving or updating.
     *
     * @param payment the Payment to validate
     */
    private void validate(Payment payment) {
        if (payment.getInvoice() == null) {
            throw new InvalidDataException("Payment must be linked to an invoice");
        }
        if (payment.getAmount() == null || payment.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidDataException("Payment amount must be greater than 0");
        }
        if (payment.getPaymentDate() == null || payment.getPaymentDate().isAfter(LocalDateTime.now())) {
            throw new InvalidDataException("Payment date must not be in the future");
        }
        if (payment.getStatus() == null) {
            throw new InvalidDataException("Payment status must not be null");
        }
        if (payment.getPaymentMethod() == null) {
            throw new InvalidDataException("Payment method must be specified");
        }
    }

    /**
     * Removes a Payment entity by its ID.
     *
     * @param paymentId the ID of the payment to remove
     */
    @Transactional
    public void removePayment(Integer paymentId) {
        Payment payment = findById(paymentId);
        if (payment == null) {
            throw new EntityNotFoundException("The payment doesn't exist.");
        }

        Invoice invoice = payment.getInvoice();

        super.deleteById(paymentId);

        invoice.setStatus(StatusValues.NOT_PAID);
        invoiceService.update(invoice);
    }

    /**
     * Finds all Payment entities associated with a specific user ID.
     *
     * @param userId the ID of the user
     * @return a list of payments associated with the user
     */
    public List<Payment> findAllByUserId(Integer userId) {
        return paymentRepository.findByInvoice_User_Id(userId);
    }

    /**
     * Retrieves an Invoice by its ID.
     *
     * @param id the ID of the invoice
     * @return the Invoice entity
     */
    public Invoice getInvoiceById(Integer id) {
        return invoiceService.findById(id);
    }


}
