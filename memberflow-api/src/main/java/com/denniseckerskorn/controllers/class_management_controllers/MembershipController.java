package com.denniseckerskorn.controllers.class_management_controllers;

import com.denniseckerskorn.dtos.class_managment_dtos.MembershipDTO;
import com.denniseckerskorn.entities.class_managment.Membership;
import com.denniseckerskorn.services.class_managment_services.MembershipService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * MembershipController handles requests related to membership management.
 * It provides endpoints for creating, retrieving, updating, and deleting memberships.
 */
@RestController
@RequestMapping("/api/v1/memberships")
@Tag(name = "Memberships", description = "Operations related to membership management")
public class MembershipController {

    private final MembershipService membershipService;

    /**
     * Constructor for MembershipController.
     *
     * @param membershipService Service for handling membership records.
     */
    public MembershipController(MembershipService membershipService) {
        this.membershipService = membershipService;
    }

    /**
     * Creates a new membership.
     *
     * @param membershipDTO The MembershipDTO containing the details of the membership to be created.
     * @return ResponseEntity containing the created MembershipDTO.
     */
    @Operation(summary = "Create a new membership")
    @PostMapping("/create")
    public ResponseEntity<MembershipDTO> createMembership(@RequestBody MembershipDTO membershipDTO) {
        Membership membership = membershipDTO.toEntity();
        Membership savedMembership = membershipService.save(membership);
        return ResponseEntity.status(HttpStatus.CREATED).body(MembershipDTO.fromEntity(savedMembership));
    }

    /**
     * Retrieves all memberships.
     *
     * @return ResponseEntity containing a list of MembershipDTOs.
     */
    @Operation(summary = "Get all memberships", description = "Retrieve a list of all memberships")
    @GetMapping("/getAll")
    public ResponseEntity<List<MembershipDTO>> getAllMemberships() {
        List<Membership> memberships = membershipService.findAll();
        if (memberships.isEmpty()) return ResponseEntity.noContent().build();

        List<MembershipDTO> dtos = memberships.stream()
                .map(MembershipDTO::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    /**
     * Retrieves a membership by its ID.
     *
     * @param id The ID of the membership to retrieve.
     * @return ResponseEntity containing the MembershipDTO if found, or 404 Not Found if not found.
     */
    @Operation(summary = "Get a membership by ID", description = "Retrieve a membership by its ID")
    @GetMapping("/getById/{id}")
    public ResponseEntity<MembershipDTO> getMembershipById(@PathVariable Integer id) {
        Membership membership = membershipService.findById(id);
        return ResponseEntity.ok(MembershipDTO.fromEntity(membership));
    }

    /**
     * Updates an existing membership.
     *
     * @param id  The ID of the membership to update.
     * @param dto The MembershipDTO containing the updated details of the membership.
     * @return ResponseEntity containing the updated MembershipDTO.
     */
    @Operation(summary = "Update a membership", description = "Update an existing membership")
    @Transactional
    @PutMapping("/update/{id}")
    public ResponseEntity<MembershipDTO> updateMembership(@PathVariable Integer id, @RequestBody MembershipDTO dto) {
        dto.setId(id);
        Membership updated = membershipService.update(dto.toEntity());
        return ResponseEntity.ok(MembershipDTO.fromEntity(updated));
    }

    /**
     * Deletes a membership by its ID.
     *
     * @param id The ID of the membership to delete.
     * @return ResponseEntity indicating the result of the deletion operation.
     */
    @Operation(summary = "Delete a membership", description = "Delete a membership by its ID")
    @Transactional
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteMembership(@PathVariable Integer id) {
        membershipService.deleteById(id);
        return ResponseEntity.ok("Membership deleted successfully");
    }

}
