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

    @InjectMocks
    private PaymentService paymentService;

    private Invoice invoice;
    private Payment payment;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        invoice = new Invoice();
        invoice.setId(1);
        invoice.setTotal(new BigDecimal("140.00"));
        invoice.setStatus(StatusValues.NOT_PAID);

        payment = new Payment();
        payment.setId(101);
        payment.setInvoice(invoice);
        payment.setAmount(new BigDecimal("140.00"));
        payment.setPaymentDate(LocalDateTime.now().minusDays(1));
        payment.setPaymentMethod(PaymentMethodValues.CASH);
        payment.setStatus(StatusValues.PAID);
    }

    @Test
    void save_ShouldUpdateInvoiceStatusAndSavePayment() {
        when(paymentRepository.existsByInvoiceId(1)).thenReturn(false);
        when(invoiceService.findById(1)).thenReturn(invoice);
        when(paymentRepository.save(any())).thenReturn(payment);

        Payment saved = paymentService.save(payment);

        assertEquals(StatusValues.PAID, saved.getInvoice().getStatus());
        verify(invoiceService).update(invoice);
        verify(paymentRepository).save(payment);
    }

    @Test
    void save_WhenDuplicate_ShouldThrow() {
        when(paymentRepository.existsByInvoiceId(1)).thenReturn(true);

        assertThrows(DuplicateEntityException.class, () -> paymentService.save(payment));
        verify(invoiceService, never()).update(any());
    }

    @Test
    void save_WhenInvoiceNull_ShouldThrow() {
        payment.setInvoice(null);

        assertThrows(InvalidDataException.class, () -> paymentService.save(payment));
    }

    @Test
    void save_WhenInvalidAmount_ShouldThrow() {
        payment.setAmount(BigDecimal.ZERO);

        assertThrows(InvalidDataException.class, () -> paymentService.save(payment));
    }

    @Test
    void save_WhenFutureDate_ShouldThrow() {
        payment.setPaymentDate(LocalDateTime.now().plusDays(1));

        assertThrows(InvalidDataException.class, () -> paymentService.save(payment));
    }

    @Test
    void save_WhenMissingMethod_ShouldThrow() {
        payment.setPaymentMethod(null);

        assertThrows(InvalidDataException.class, () -> paymentService.save(payment));
    }
}
