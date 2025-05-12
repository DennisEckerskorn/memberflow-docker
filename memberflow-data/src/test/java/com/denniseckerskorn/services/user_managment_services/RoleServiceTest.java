package com.denniseckerskorn.services.user_managment_services;

import com.denniseckerskorn.entities.user_managment.Permission;
import com.denniseckerskorn.entities.user_managment.Role;
import com.denniseckerskorn.exceptions.EntityNotFoundException;
import com.denniseckerskorn.exceptions.InvalidDataException;
import com.denniseckerskorn.repositories.user_managment_repositories.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleService roleService;

    private Role role;
    private Permission permission;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        role = new Role();
        role.setId(1);
        role.setName("Admin");
        role.setPermissions(new HashSet<>());

        permission = new Permission();
        permission.setId(1);
    }

    @Test
    void save_ValidRole_ReturnsSavedRole() {
        when(roleRepository.save(role)).thenReturn(role);
        Role saved = roleService.save(role);
        assertNotNull(saved);
        assertEquals("Admin", saved.getName());
    }

    @Test
    void save_NullRoleName_ThrowsInvalidDataException() {
        role.setName(null);
        assertThrows(InvalidDataException.class, () -> roleService.save(role));
    }

    @Test
    void findById_ValidId_ReturnsRole() {
        when(roleRepository.existsById(1)).thenReturn(true);
        when(roleRepository.findById(1)).thenReturn(Optional.of(role));
        Role found = roleService.findById(1);
        assertEquals("Admin", found.getName());
    }

    @Test
    void findById_InvalidId_ThrowsEntityNotFoundException() {
        when(roleRepository.existsById(99)).thenReturn(false);
        assertThrows(EntityNotFoundException.class, () -> roleService.findById(99));
    }

    @Test
    void deleteById_ValidId_DeletesRole() {
        when(roleRepository.existsById(1)).thenReturn(true);
        doNothing().when(roleRepository).deleteById(1);
        assertDoesNotThrow(() -> roleService.deleteById(1));
    }

    @Test
    void findAll_ReturnsListOfRoles() {
        when(roleRepository.findAll()).thenReturn(List.of(role));
        List<Role> roles = roleService.findAll();
        assertEquals(1, roles.size());
    }

    @Test
    void addPermissionToRole_AddsSuccessfully() {
        when(roleRepository.findById(1)).thenReturn(Optional.of(role));
        when(roleRepository.existsById(1)).thenReturn(true);
        when(roleRepository.save(any())).thenReturn(role);
        roleService.addPermissionToRole(role, permission);
        verify(roleRepository).save(any());
    }

    @Test
    void addPermissionToRole_NullInputs_ThrowsInvalidDataException() {
        assertThrows(InvalidDataException.class, () -> roleService.addPermissionToRole(null, permission));
        assertThrows(InvalidDataException.class, () -> roleService.addPermissionToRole(role, null));
    }

    @Test
    void removePermissionFromRole_RemovesSuccessfully() {
        Set<Permission> mutablePermissions = new HashSet<>();
        mutablePermissions.add(permission);
        role.setPermissions(mutablePermissions);

        when(roleRepository.findById(1)).thenReturn(Optional.of(role));
        when(roleRepository.existsById(1)).thenReturn(true);
        when(roleRepository.save(any())).thenReturn(role);

        roleService.removePermissionFromRole(role, permission);

        verify(roleRepository).save(any());
    }


    @Test
    void removePermissionFromRole_NullInputs_ThrowsInvalidDataException() {
        assertThrows(InvalidDataException.class, () -> roleService.removePermissionFromRole(null, permission));
        assertThrows(InvalidDataException.class, () -> roleService.removePermissionFromRole(role, null));
    }
}
