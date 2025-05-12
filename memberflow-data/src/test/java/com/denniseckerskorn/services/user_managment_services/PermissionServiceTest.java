package com.denniseckerskorn.services.user_managment_services;

import com.denniseckerskorn.entities.user_managment.Permission;
import com.denniseckerskorn.enums.PermissionValues;
import com.denniseckerskorn.exceptions.EntityNotFoundException;
import com.denniseckerskorn.exceptions.InvalidDataException;
import com.denniseckerskorn.repositories.user_managment_repositories.PermissionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PermissionServiceTest {

    @Mock
    private PermissionRepository permissionRepository;

    @InjectMocks
    private PermissionService permissionService;

    private Permission permission;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        permission = new Permission();
        permission.setId(1);
        permission.setPermissionName(PermissionValues.FULL_ACCESS);
    }

    @Test
    void save_ValidPermission_ReturnsSavedPermission() {
        when(permissionRepository.save(permission)).thenReturn(permission);
        Permission saved = permissionService.save(permission);
        assertNotNull(saved);
        assertEquals(PermissionValues.FULL_ACCESS, saved.getPermissionName());
    }

    @Test
    void save_NullPermission_ThrowsInvalidDataException() {
        assertThrows(InvalidDataException.class, () -> permissionService.save(null));
    }

    @Test
    void findById_ValidId_ReturnsPermission() {
        when(permissionRepository.existsById(1)).thenReturn(true);
        when(permissionRepository.findById(1)).thenReturn(Optional.of(permission));
        Permission found = permissionService.findById(1);
        assertEquals(permission.getPermissionName(), found.getPermissionName());
    }

    @Test
    void findById_NotFound_ThrowsEntityNotFoundException() {
        when(permissionRepository.existsById(99)).thenReturn(false);
        assertThrows(EntityNotFoundException.class, () -> permissionService.findById(99));
    }

    @Test
    void deleteById_ValidId_DeletesPermission() {
        when(permissionRepository.existsById(1)).thenReturn(true);
        doNothing().when(permissionRepository).deleteById(1);
        assertDoesNotThrow(() -> permissionService.deleteById(1));
    }

    @Test
    void findPermissionByName_Valid_ReturnsPermission() {
        when(permissionRepository.findByPermissionName(PermissionValues.FULL_ACCESS)).thenReturn(permission);
        Permission found = permissionService.findPermissionByName(PermissionValues.FULL_ACCESS);
        assertEquals(permission.getPermissionName(), found.getPermissionName());
    }

    @Test
    void findPermissionByName_NotFound_ThrowsEntityNotFoundException() {
        when(permissionRepository.findByPermissionName(PermissionValues.VIEW_OWN_DATA)).thenReturn(null);
        assertThrows(EntityNotFoundException.class,
                () -> permissionService.findPermissionByName(PermissionValues.VIEW_OWN_DATA));
    }

    @Test
    void findAll_ReturnsList() {
        when(permissionRepository.findAll()).thenReturn(List.of(permission));
        List<Permission> result = permissionService.findAll();
        assertEquals(1, result.size());
    }
}
