package com.denniseckerskorn.services.finance_management_services;

import com.denniseckerskorn.entities.finance.Invoice;
import com.denniseckerskorn.entities.user_managment.users.User;
import com.denniseckerskorn.enums.StatusValues;
import com.denniseckerskorn.exceptions.InvalidDataException;
import com.denniseckerskorn.repositories.finance_repositories.InvoiceRepository;
import com.denniseckerskorn.services.finance_services.InvoiceService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InvoiceServiceTest {

    @Mock
    private InvoiceRepository invoiceRepository;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private InvoiceService invoiceService;

    private Invoice invoice;
    private User user;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1);

        invoice = new Invoice();
        invoice.setId(10);
        invoice.setUser(user);
        invoice.setDate(LocalDateTime.now().minusDays(1));
        invoice.setTotal(new BigDecimal("200.00"));
        invoice.setStatus(StatusValues.NOT_PAID);

        Field emField = InvoiceService.class.getSuperclass().getDeclaredField("entityManager");
        emField.setAccessible(true);
        emField.set(invoiceService, entityManager);
    }

    @Test
    void save_ValidInvoice_ShouldReturnInvoice() {
        when(invoiceRepository.save(invoice)).thenReturn(invoice);
        Invoice saved = invoiceService.save(invoice);
        assertEquals(10, saved.getId());
    }

    @Test
    void save_InvalidTotal_ShouldThrow() {
        invoice.setTotal(BigDecimal.ZERO);
        assertThrows(InvalidDataException.class, () -> invoiceService.save(invoice));
    }

    @Test
    void save_InvalidUser_ShouldThrow() {
        invoice.setUser(null);
        assertThrows(InvalidDataException.class, () -> invoiceService.save(invoice));
    }

    @Test
    void save_InvalidStatus_ShouldThrow() {
        invoice.setStatus(null);
        assertThrows(InvalidDataException.class, () -> invoiceService.save(invoice));
    }

    @Test
    void findById_ValidId_ShouldReturnInvoice() {
        when(invoiceRepository.existsById(10)).thenReturn(true);
        when(invoiceRepository.findById(10)).thenReturn(Optional.of(invoice));
        Invoice found = invoiceService.findById(10);
        assertEquals(10, found.getId());
    }

    @Test
    void deleteById_ValidId_ShouldCallRepository() {
        when(invoiceRepository.existsById(10)).thenReturn(true);
        when(invoiceRepository.findById(10)).thenReturn(Optional.of(invoice));
        doNothing().when(invoiceRepository).deleteById(10);

        assertDoesNotThrow(() -> invoiceService.deleteById(10));
        verify(invoiceRepository).deleteById(10);
    }
}
