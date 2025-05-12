package com.denniseckerskorn.services.finance_management_services;

import com.denniseckerskorn.entities.finance.IVAType;
import com.denniseckerskorn.entities.finance.ProductService;
import com.denniseckerskorn.enums.StatusValues;
import com.denniseckerskorn.exceptions.DuplicateEntityException;
import com.denniseckerskorn.exceptions.InvalidDataException;
import com.denniseckerskorn.repositories.finance_repositories.ProductServiceRepository;
import com.denniseckerskorn.services.finance_services.ProductServiceService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.lang.reflect.Field;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceServiceTest {

    @Mock
    private ProductServiceRepository productServiceRepository;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private ProductServiceService productServiceService;

    private ProductService product;
    private IVAType ivaType;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        ivaType = new IVAType();
        ivaType.setId(1);
        ivaType.setPercentage(new BigDecimal("21.00"));
        ivaType.setDescription("IVA General");

        product = new ProductService();
        product.setId(10);
        product.setName("Clase Avanzada");
        product.setType("Servicio");
        product.setPrice(new BigDecimal("30.00"));
        product.setIvaType(ivaType);
        product.setStatus(StatusValues.ACTIVE);

        Field emField = ProductServiceService.class.getSuperclass().getDeclaredField("entityManager");
        emField.setAccessible(true);
        emField.set(productServiceService, entityManager);
    }

    @Test
    void save_ValidProduct_ShouldReturnSaved() {
        when(productServiceRepository.existsByName(product.getName())).thenReturn(false);
        when(productServiceRepository.save(any())).thenReturn(product);

        ProductService saved = productServiceService.save(product);
        assertEquals("Clase Avanzada", saved.getName());
    }

    @Test
    void save_DuplicateName_ShouldThrow() {
        when(productServiceRepository.existsByName(product.getName())).thenReturn(true);
        assertThrows(DuplicateEntityException.class, () -> productServiceService.save(product));
    }

    @Test
    void save_NullName_ShouldThrow() {
        product.setName(null);
        assertThrows(InvalidDataException.class, () -> productServiceService.save(product));
    }

    @Test
    void save_InvalidPrice_ShouldThrow() {
        product.setPrice(BigDecimal.ZERO);
        assertThrows(InvalidDataException.class, () -> productServiceService.save(product));
    }

    @Test
    void save_NoIVAType_ShouldThrow() {
        product.setIvaType(null);
        assertThrows(InvalidDataException.class, () -> productServiceService.save(product));
    }

    @Test
    void save_NullStatus_ShouldThrow() {
        product.setStatus(null);
        assertThrows(InvalidDataException.class, () -> productServiceService.save(product));
    }
}
