package com.denniseckerskorn.services.class_managment_services;

import com.denniseckerskorn.entities.class_managment.Membership;
import com.denniseckerskorn.enums.MembershipTypeValues;
import com.denniseckerskorn.enums.StatusValues;
import com.denniseckerskorn.exceptions.InvalidDataException;
import com.denniseckerskorn.repositories.class_managment_repositories.MembershipRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MembershipServiceTest {

    @Mock
    private MembershipRepository membershipRepository;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private MembershipService membershipService;

    private Membership membership;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        membership = new Membership();
        membership.setId(1);
        membership.setType(MembershipTypeValues.BASIC);
        membership.setStartDate(LocalDate.now());
        membership.setEndDate(LocalDate.now().plusMonths(1));
        membership.setStatus(StatusValues.ACTIVE);

        // Inject mock EntityManager using reflection
        Field emField = MembershipService.class.getSuperclass().getDeclaredField("entityManager");
        emField.setAccessible(true);
        emField.set(membershipService, entityManager);
    }

    @Test
    void save_ValidMembership_ReturnsSavedMembership() {
        when(membershipRepository.findByType(MembershipTypeValues.BASIC)).thenReturn(null);
        when(membershipRepository.save(membership)).thenReturn(membership);

        Membership saved = membershipService.save(membership);
        assertEquals(MembershipTypeValues.BASIC, saved.getType());
    }

    @Test
    void save_InvalidMembership_ThrowsException() {
        membership.setType(null);
        assertThrows(InvalidDataException.class, () -> membershipService.save(membership));
    }

    @Test
    void update_ValidMembership_ReturnsUpdatedMembership() {
        when(membershipRepository.existsById(1)).thenReturn(true);
        when(entityManager.merge(membership)).thenReturn(membership);

        Membership updated = membershipService.update(membership);
        assertEquals(MembershipTypeValues.BASIC, updated.getType());
    }

    @Test
    void findById_ValidId_ReturnsMembership() {
        when(membershipRepository.existsById(1)).thenReturn(true);
        when(membershipRepository.findById(1)).thenReturn(Optional.of(membership));

        Membership found = membershipService.findById(1);
        assertEquals(MembershipTypeValues.BASIC, found.getType());
    }

    @Test
    void deleteById_NotAssignedToStudent_Successful() {
        when(membershipRepository.existsById(1)).thenReturn(true);
        when(membershipRepository.findById(1)).thenReturn(Optional.of(membership));
        doNothing().when(membershipRepository).deleteById(1);

        assertDoesNotThrow(() -> membershipService.deleteById(1));
    }

    @Test
    void exists_ReturnsTrueIfFound() {
        when(membershipRepository.findByType(MembershipTypeValues.BASIC)).thenReturn(membership);
        assertTrue(membershipService.exists(membership));
    }

    @Test
    void findAll_ReturnsMembershipList() {
        when(membershipRepository.findAll()).thenReturn(List.of(membership));
        List<Membership> list = membershipService.findAll();
        assertEquals(1, list.size());
    }
}
