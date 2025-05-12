package com.denniseckerskorn.services.user_managment_services;

import com.denniseckerskorn.entities.user_managment.users.Admin;
import com.denniseckerskorn.entities.user_managment.users.User;
import com.denniseckerskorn.enums.StatusValues;
import com.denniseckerskorn.exceptions.DuplicateEntityException;
import com.denniseckerskorn.exceptions.EntityNotFoundException;
import com.denniseckerskorn.exceptions.InvalidDataException;
import com.denniseckerskorn.repositories.user_managment_repositories.AdminRepository;
import com.denniseckerskorn.repositories.user_managment_repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdminServiceTest {

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AdminService adminService;


    private Admin admin;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        User user = new User();
        user.setId(1);
        user.setName("Dennis");
        user.setSurname("Tester");
        user.setEmail("test@example.com");
        user.setPhoneNumber("600000000");
        user.setPassword("12345678");
        user.setStatus(StatusValues.ACTIVE);
        user.setRegisterDate(LocalDateTime.now());
        user.setAddress("Calle Test");

        admin = new Admin();
        admin.setId(1);
        admin.setUser(user);
    }

    @Test
    void givenValidAdmin_whenSave_thenReturnsSavedAdmin() {
        when(adminRepository.existsById(admin.getId())).thenReturn(false);
        when(userRepository.save(admin.getUser())).thenReturn(admin.getUser()); // <-- AÑADIDO
        when(adminRepository.save(admin)).thenReturn(admin);

        Admin savedAdmin = adminService.save(admin);

        assertNotNull(savedAdmin);
        assertEquals(admin.getId(), savedAdmin.getId());
        verify(userRepository, times(1)).save(admin.getUser()); // <-- AÑADIDO
        verify(adminRepository, times(1)).save(admin);
    }


    @Test
    void givenNullAdmin_whenSave_thenThrowsInvalidDataException() {
        InvalidDataException exception = assertThrows(InvalidDataException.class, () -> adminService.save(null));
        assertEquals("Admin cannot be null.", exception.getMessage());
    }

    @Test
    void givenAdminAlreadyExists_whenSave_thenThrowsDuplicateEntityException() {
        when(adminRepository.existsById(admin.getId())).thenReturn(true);
        assertThrows(DuplicateEntityException.class, () -> adminService.save(admin));
    }

    @Test
    void givenValidId_whenFindById_thenReturnsAdmin() {
        when(adminRepository.existsById(admin.getId())).thenReturn(true);
        when(adminRepository.findById(admin.getId())).thenReturn(Optional.of(admin));

        Admin found = adminService.findById(admin.getId());
        assertEquals(admin.getId(), found.getId());
    }

    @Test
    void givenNullId_whenFindById_thenThrowsInvalidDataException() {
        assertThrows(InvalidDataException.class, () -> adminService.findById(null));
    }

    @Test
    void givenNonExistingId_whenFindById_thenThrowsEntityNotFoundException() {
        when(adminRepository.existsById(99)).thenReturn(false);
        assertThrows(EntityNotFoundException.class, () -> adminService.findById(99));
    }

    @Test
    void givenValidId_whenDeleteById_thenDeletesAdmin() {
        when(adminRepository.existsById(admin.getId())).thenReturn(true);
        doNothing().when(adminRepository).deleteById(admin.getId());

        adminService.deleteById(admin.getId());
        verify(adminRepository, times(1)).deleteById(admin.getId());
    }

    @Test
    void givenNullId_whenDeleteById_thenThrowsInvalidDataException() {
        assertThrows(InvalidDataException.class, () -> adminService.deleteById(null));
    }

    @Test
    void givenNonExistingId_whenDeleteById_thenThrowsEntityNotFoundException() {
        when(adminRepository.existsById(99)).thenReturn(false);
        assertThrows(EntityNotFoundException.class, () -> adminService.deleteById(99));
    }
}
