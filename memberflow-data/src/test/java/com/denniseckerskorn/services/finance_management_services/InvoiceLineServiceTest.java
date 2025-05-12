package com.denniseckerskorn.services.finance_management_services;

import com.denniseckerskorn.entities.finance.Invoice;
import com.denniseckerskorn.entities.finance.InvoiceLine;
import com.denniseckerskorn.entities.finance.ProductService;
import com.denniseckerskorn.enums.StatusValues;
import com.denniseckerskorn.exceptions.InvalidDataException;
import com.denniseckerskorn.repositories.finance_repositories.InvoiceLineRepository;
import com.denniseckerskorn.services.finance_services.InvoiceLineService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.lang.reflect.Field;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InvoiceLineServiceTest {

    @Mock
    private InvoiceLineRepository invoiceLineRepository;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private InvoiceLineService invoiceLineService;

    private InvoiceLine line;
    private Invoice invoice;
    private ProductService product;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        invoice = new Invoice();
        invoice.setId(1);
        invoice.setTotal(BigDecimal.ZERO);
        invoice.setStatus(StatusValues.NOT_PAID);

        product = new ProductService();
        product.setId(1);
        product.setName("Clases JiuJitsu");
        product.setPrice(new BigDecimal("50.00"));

        line = new InvoiceLine();
        line.setId(100);
        line.setInvoice(invoice);
        line.setProductService(product);
        line.setQuantity(2);
        line.setUnitPrice(new BigDecimal("50.00"));

        Field emField = InvoiceLineService.class.getSuperclass().getDeclaredField("entityManager");
        emField.setAccessible(true);
        emField.set(invoiceLineService, entityManager);
    }

    @Test
    void save_ValidInvoiceLine_ShouldCalculateSubtotal() {
        when(invoiceLineRepository.save(any())).thenReturn(line);
        InvoiceLine saved = invoiceLineService.save(line);
        assertNotNull(saved);
        assertEquals(new BigDecimal("100.00"), saved.getSubtotal());
    }

    @Test
    void save_InvalidQuantity_ShouldThrow() {
        line.setQuantity(0);
        assertThrows(InvalidDataException.class, () -> invoiceLineService.save(line));
    }

    @Test
    void save_InvalidProduct_ShouldThrow() {
        line.setProductService(null);
        assertThrows(InvalidDataException.class, () -> invoiceLineService.save(line));
    }

    @Test
    void save_InvalidInvoice_ShouldThrow() {
        line.setInvoice(null);
        assertThrows(InvalidDataException.class, () -> invoiceLineService.save(line));
    }

    @Test
    void save_InvalidPrice_ShouldThrow() {
        line.setUnitPrice(BigDecimal.ZERO);
        assertThrows(InvalidDataException.class, () -> invoiceLineService.save(line));
    }
}
