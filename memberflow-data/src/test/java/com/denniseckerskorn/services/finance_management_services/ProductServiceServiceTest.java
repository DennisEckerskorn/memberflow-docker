package com.denniseckerskorn.services.finance_services;

import com.denniseckerskorn.entities.finance.IVAType;
import com.denniseckerskorn.entities.finance.ProductService;
import com.denniseckerskorn.enums.StatusValues;
import com.denniseckerskorn.exceptions.DuplicateEntityException;
import com.denniseckerskorn.exceptions.InvalidDataException;
import com.denniseckerskorn.repositories.finance_repositories.ProductServiceRepository;
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
    private IVATypeService ivaTypeService;

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
        ivaType.setPercentage(BigDecimal.valueOf(21));
        ivaType.setDescription("IVA General");

        product = new ProductService();
        product.setId(1);
        product.setName("Producto Test");
        product.setPrice(BigDecimal.valueOf(100));
        product.setType("PRODUCT");
        product.setStatus(StatusValues.ACTIVE);
        product.setIvaType(ivaType);

        // Set EntityManager in abstract service
        Field emField = ProductServiceService.class.getSuperclass().getDeclaredField("entityManager");
        emField.setAccessible(true);
        emField.set(productServiceService, entityManager);
    }

    @Test
    void save_ValidProduct_ShouldReturnSaved() {
        when(ivaTypeService.findById(ivaType.getId())).thenReturn(ivaType);
        when(productServiceRepository.findByName(product.getName())).thenReturn(null);
        when(productServiceRepository.existsByName(product.getName())).thenReturn(false);
        when(productServiceRepository.save(any())).thenReturn(product);

        ProductService saved = productServiceService.save(product);
        assertEquals(product.getName(), saved.getName());
        assertEquals(product.getPrice(), saved.getPrice());
    }

    @Test
    void save_DuplicateName_ShouldThrow() {
        when(ivaTypeService.findById(ivaType.getId())).thenReturn(ivaType);
        when(productServiceRepository.findByName(product.getName())).thenReturn(product);
        when(productServiceRepository.existsByName(product.getName())).thenReturn(true);

        assertThrows(DuplicateEntityException.class, () -> productServiceService.save(product));
    }

    @Test
    void save_InvalidPrice_ShouldThrow() {
        product.setPrice(BigDecimal.ZERO);
        assertThrows(InvalidDataException.class, () -> productServiceService.save(product));
    }

    @Test
    void save_NullName_ShouldThrow() {
        product.setName(null);
        assertThrows(InvalidDataException.class, () -> productServiceService.save(product));
    }

    @Test
    void save_NoIVAType_ShouldThrow() {
        product.setIvaType(null);
        assertThrows(IllegalArgumentException.class, () -> productServiceService.save(product));
    }

    @Test
    void save_NullStatus_ShouldThrow() {
        product.setStatus(null);
        assertThrows(InvalidDataException.class, () -> productServiceService.save(product));
    }
}
