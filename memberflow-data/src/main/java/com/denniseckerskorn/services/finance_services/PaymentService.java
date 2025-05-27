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

@Service
public class PaymentService extends AbstractService<Payment, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);
    private final PaymentRepository paymentRepository;
    private final InvoiceService invoiceService;

    public PaymentService(PaymentRepository paymentRepository, InvoiceService invoiceService) {
        super(paymentRepository);
        this.paymentRepository = paymentRepository;
        this.invoiceService = invoiceService;
    }

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

        //invoice.setPayment(savedPayment);
        invoice.setStatus(StatusValues.PAID);
        invoiceService.update(invoice);

        return savedPayment;
    }


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

    @Override
    protected boolean exists(Payment entity) {
        return entity != null && entity.getId() != null && paymentRepository.existsById(entity.getId());
    }

    @Override
    protected Integer getEntityId(Payment entity) {
        return entity.getId();
    }

    @Override
    public List<Payment> findAll() {
        logger.info("Retrieving all payments");
        return super.findAll();
    }

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

    public List<Payment> findAllByUserId(Integer userId) {
        return paymentRepository.findByInvoice_User_Id(userId);
    }

    public Invoice getInvoiceById(Integer id) {
        return invoiceService.findById(id);
    }


}
