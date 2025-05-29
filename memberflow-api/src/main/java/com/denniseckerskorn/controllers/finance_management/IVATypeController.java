package com.denniseckerskorn.controllers.finance_management;

import com.denniseckerskorn.dtos.finance_management_dtos.IVATypeDTO;
import com.denniseckerskorn.entities.finance.IVAType;
import com.denniseckerskorn.exceptions.DuplicateEntityException;
import com.denniseckerskorn.exceptions.EntityNotFoundException;
import com.denniseckerskorn.exceptions.InvalidDataException;
import com.denniseckerskorn.services.finance_services.IVATypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller for managing IVA types.
 * Provides endpoints for creating, retrieving, updating, and deleting IVA types.
 */
@RestController
@RequestMapping("/api/v1/iva-types")
@Tag(name = "IVA Types", description = "Operations related to IVA type management")
public class IVATypeController {

    private final IVATypeService ivaTypeService;

    /**
     * Constructor for IVATypeController.
     *
     * @param ivaTypeService Service for handling IVA type records.
     */
    public IVATypeController(IVATypeService ivaTypeService) {
        this.ivaTypeService = ivaTypeService;
    }

    /**
     * Creates a new IVA type.
     *
     * @param dto The IVATypeDTO containing the details of the IVA type to be created.
     * @return ResponseEntity containing the created IVATypeDTO.
     * @throws DuplicateEntityException if an IVA type with the same name already exists.
     */
    @PostMapping("/create")
    @Operation(summary = "Create a new IVA type")
    public ResponseEntity<IVATypeDTO> create(@Valid @RequestBody IVATypeDTO dto) throws DuplicateEntityException {
        IVAType saved = ivaTypeService.save(dto.toEntity());
        return new ResponseEntity<>(new IVATypeDTO(saved), HttpStatus.CREATED);
    }

    /**
     * Updates an existing IVA type.
     *
     * @param dto The IVATypeDTO containing the updated details of the IVA type.
     * @return ResponseEntity containing the updated IVATypeDTO.
     * @throws EntityNotFoundException if the IVA type to update does not exist.
     * @throws InvalidDataException    if the provided data is invalid.
     */
    @PutMapping("/update")
    @Operation(summary = "Update an existing IVA type")
    public ResponseEntity<IVATypeDTO> update(@Valid @RequestBody IVATypeDTO dto) throws EntityNotFoundException, InvalidDataException {
        IVAType updated = ivaTypeService.update(dto.toEntity());
        return new ResponseEntity<>(new IVATypeDTO(updated), HttpStatus.OK);
    }

    /**
     * Retrieves an IVA type by its ID.
     *
     * @param id The ID of the IVA type to retrieve.
     * @return ResponseEntity containing the IVATypeDTO if found.
     * @throws EntityNotFoundException if the IVA type with the specified ID does not exist.
     * @throws InvalidDataException    if the provided ID is invalid.
     */
    @GetMapping("/getById/{id}")
    @Operation(summary = "Get IVA type by ID")
    public ResponseEntity<IVATypeDTO> getById(@PathVariable Integer id) throws EntityNotFoundException, InvalidDataException {
        IVAType entity = ivaTypeService.findById(id);
        return new ResponseEntity<>(new IVATypeDTO(entity), HttpStatus.OK);
    }

    /**
     * Retrieves all IVA types.
     *
     * @return ResponseEntity containing a list of IVATypeDTOs.
     */
    @GetMapping("/getAll")
    @Operation(summary = "Get all IVA types")
    public ResponseEntity<List<IVATypeDTO>> getAll() {
        List<IVATypeDTO> dtos = ivaTypeService.findAll()
                .stream().map(IVATypeDTO::new).collect(Collectors.toList());
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    /**
     * Deletes an IVA type by its ID.
     *
     * @param id The ID of the IVA type to delete.
     * @return ResponseEntity with no content if deletion is successful.
     * @throws EntityNotFoundException if the IVA type with the specified ID does not exist.
     * @throws InvalidDataException    if the IVA type is in use and cannot be deleted.
     */
    @DeleteMapping("/deleteById/{id}")
    @Operation(summary = "Delete IVA type by ID (if not in use)")
    public ResponseEntity<Void> delete(@PathVariable Integer id) throws EntityNotFoundException, InvalidDataException {
        ivaTypeService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
