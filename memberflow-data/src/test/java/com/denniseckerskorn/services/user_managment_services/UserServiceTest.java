package com.denniseckerskorn.services.user_managment_services;

import com.denniseckerskorn.entities.finance.Invoice;
import com.denniseckerskorn.entities.user_managment.Role;
import com.denniseckerskorn.entities.user_managment.users.User;
import com.denniseckerskorn.enums.StatusValues;
import com.denniseckerskorn.exceptions.DuplicateEntityException;
import com.denniseckerskorn.exceptions.EntityNotFoundException;
import com.denniseckerskorn.exceptions.InvalidDataException;
import com.denniseckerskorn.repositories.user_managment_repositories.UserRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private UserService userService;

    private User user;
    private Role role;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        role = new Role();
        role.setId(1);
        role.setName("Admin");

        user = new User();
        user.setId(1);
        user.setName("John");
        user.setSurname("Doe");
        user.setEmail("john@example.com");
        user.setPhoneNumber("123456789");
        user.setPassword("password123");
        user.setStatus(StatusValues.ACTIVE);
        user.setRegisterDate(LocalDateTime.now());
        user.setRole(role);

        // Inyectar el EntityManager en AbstractService
        Field emField = UserService.class.getSuperclass().getDeclaredField("entityManager");
        emField.setAccessible(true);
        emField.set(userService, entityManager);
    }

    @Test
    void save_ValidUser_ReturnsSavedUser() {
        when(userRepository.save(user)).thenReturn(user);
        User saved = userService.save(user);
        assertNotNull(saved);
        assertEquals("john@example.com", saved.getEmail());
    }

    @Test
    void save_InvalidUser_ThrowsException() {
        user.setEmail("invalid");
        assertThrows(InvalidDataException.class, () -> userService.save(user));
    }

    @Test
    void update_ValidUser_ReturnsUpdatedUser() {
        when(userRepository.existsById(1)).thenReturn(true);
        when(entityManager.merge(user)).thenReturn(user);
        User updated = userService.update(user);
        assertNotNull(updated);
    }

    @Test
    void deleteById_ValidId_DeletesUser() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(userRepository.existsById(1)).thenReturn(true);
        doNothing().when(userRepository).deleteById(1);

        assertDoesNotThrow(() -> userService.deleteById(1));
        verify(userRepository).deleteById(1);
    }


    @Test
    void findAllUserByRoleName_ReturnsList() {
        when(userRepository.findAllByRoleName("Admin")).thenReturn(List.of(user));
        List<User> users = userService.findAllUserByRoleName("Admin");
        assertEquals(1, users.size());
    }

    @Test
    void findByEmail_ValidEmail_ReturnsUser() {
        when(userRepository.findByEmail("john@example.com")).thenReturn(user);
        User found = userService.findByEmail("john@example.com");
        assertEquals("John", found.getName());
    }

    @Test
    void findByEmail_NotFound_ThrowsException() {
        when(userRepository.findByEmail("notfound@example.com")).thenReturn(null);
        assertThrows(EntityNotFoundException.class, () -> userService.findByEmail("notfound@example.com"));
    }

    @Test
    void assignRoleToUser_AssignsSuccessfully() {
        when(userRepository.existsById(1)).thenReturn(true);
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);
        assertDoesNotThrow(() -> userService.assignRoleToUser(1, role));
        assertEquals("Admin", user.getRole().getName());
    }

    @Test
    void existsByEmail_ReturnsTrue() {
        when(userRepository.existsByEmail("john@example.com")).thenReturn(true);
        assertTrue(userService.existsByEmail("john@example.com"));
    }

    @Test
    void addInvoiceToUser_ShouldLinkInvoiceCorrectly() {
        Invoice invoice = new Invoice();
        invoice.setId(1);
        invoice.setDate(LocalDateTime.now().minusDays(1));
        invoice.setTotal(new BigDecimal("100.00"));
        invoice.setStatus(StatusValues.NOT_PAID);

        assertDoesNotThrow(() -> userService.addInvoiceToUser(user, invoice));

        assertTrue(user.getInvoices().contains(invoice));
        assertEquals(user, invoice.getUser());
    }


}
