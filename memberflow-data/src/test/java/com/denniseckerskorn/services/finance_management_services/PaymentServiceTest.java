package com.denniseckerskorn.services.finance_management_services;

import com.denniseckerskorn.entities.finance.Invoice;
import com.denniseckerskorn.entities.finance.Payment;
import com.denniseckerskorn.enums.PaymentMethodValues;
import com.denniseckerskorn.enums.StatusValues;
import com.denniseckerskorn.exceptions.DuplicateEntityException;
import com.denniseckerskorn.exceptions.InvalidDataException;
import com.denniseckerskorn.repositories.finance_repositories.PaymentRepository;
import com.denniseckerskorn.services.finance_services.InvoiceService;
import com.denniseckerskorn.services.finance_services.PaymentService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private InvoiceService invoiceService;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private PaymentService paymentService;

    private Invoice invoice;
    private Payment payment;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        invoice = new Invoice();
        invoice.setId(1);
        invoice.setTotal(new BigDecimal("59.99"));
        invoice.setStatus(StatusValues.NOT_PAID);

        payment = new Payment();
        payment.setId(101);
        payment.setInvoice(invoice);
        payment.setAmount(new BigDecimal("59.99"));
        payment.setPaymentDate(LocalDateTime.now().minusDays(1));
        payment.setPaymentMethod(PaymentMethodValues.CREDIT_CARD);
        payment.setStatus(StatusValues.PAID);

        Field emField = PaymentService.class.getSuperclass().getDeclaredField("entityManager");
        emField.setAccessible(true);
        emField.set(paymentService, entityManager);
    }

    @Test
    void save_ShouldUpdateInvoiceStatusAndSavePayment() {
        when(paymentRepository.existsByInvoiceId(1)).thenReturn(false);
        when(paymentRepository.save(any())).thenReturn(payment);

        Payment saved = paymentService.save(payment);
        assertEquals(StatusValues.PAID, payment.getInvoice().getStatus());
        verify(invoiceService).update(invoice);
    }

    @Test
    void save_DuplicatePayment_ShouldThrow() {
        when(paymentRepository.existsByInvoiceId(1)).thenReturn(true);
        assertThrows(DuplicateEntityException.class, () -> paymentService.save(payment));
    }

    @Test
    void save_InvalidAmount_ShouldThrow() {
        payment.setAmount(BigDecimal.ZERO);
        assertThrows(InvalidDataException.class, () -> paymentService.save(payment));
    }

    @Test
    void save_InvalidPaymentDate_ShouldThrow() {
        payment.setPaymentDate(LocalDateTime.now().plusDays(1));
        assertThrows(InvalidDataException.class, () -> paymentService.save(payment));
    }

    @Test
    void save_InvalidMethod_ShouldThrow() {
        payment.setPaymentMethod(null);
        assertThrows(InvalidDataException.class, () -> paymentService.save(payment));
    }

    @Test
    void save_InvoiceNull_ShouldThrow() {
        payment.setInvoice(null);
        assertThrows(InvalidDataException.class, () -> paymentService.save(payment));
    }
}
