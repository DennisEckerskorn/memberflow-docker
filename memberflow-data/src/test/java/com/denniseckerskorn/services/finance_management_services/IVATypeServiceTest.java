package com.denniseckerskorn.services.finance_management_services;

import com.denniseckerskorn.entities.finance.IVAType;
import com.denniseckerskorn.exceptions.DuplicateEntityException;
import com.denniseckerskorn.exceptions.InvalidDataException;
import com.denniseckerskorn.repositories.finance_repositories.IVATypeRepository;
import com.denniseckerskorn.services.finance_services.IVATypeService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.lang.reflect.Field;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class IVATypeServiceTest {

    @Mock
    private IVATypeRepository ivaTypeRepository;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private IVATypeService ivaTypeService;

    private IVAType ivaType;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        ivaType = new IVAType();
        ivaType.setId(1);
        ivaType.setPercentage(new BigDecimal("21.00"));
        ivaType.setDescription("IVA General");

        Field emField = IVATypeService.class.getSuperclass().getDeclaredField("entityManager");
        emField.setAccessible(true);
        emField.set(ivaTypeService, entityManager);
    }

    @Test
    void save_ValidIVAType_ShouldReturnSaved() {
        when(ivaTypeRepository.existsByPercentage(ivaType.getPercentage())).thenReturn(false);
        when(ivaTypeRepository.save(any())).thenReturn(ivaType);

        IVAType saved = ivaTypeService.save(ivaType);
        assertEquals(new BigDecimal("21.00"), saved.getPercentage());
    }

    @Test
    void save_DuplicatePercentage_ShouldThrow() {
        when(ivaTypeRepository.existsByPercentage(ivaType.getPercentage())).thenReturn(true);
        assertThrows(DuplicateEntityException.class, () -> ivaTypeService.save(ivaType));
    }

    @Test
    void save_NegativePercentage_ShouldThrow() {
        ivaType.setPercentage(new BigDecimal("-5.00"));
        assertThrows(InvalidDataException.class, () -> ivaTypeService.save(ivaType));
    }

    @Test
    void save_TooLongDescription_ShouldThrow() {
        ivaType.setDescription("A".repeat(60)); // Más de 50 caracteres
        assertThrows(InvalidDataException.class, () -> ivaTypeService.save(ivaType));
    }

    @Test
    void save_NullPercentage_ShouldThrow() {
        ivaType.setPercentage(null);
        assertThrows(InvalidDataException.class, () -> ivaTypeService.save(ivaType));
    }
}
