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

@RestController
@RequestMapping("/api/v1/iva-types")
@Tag(name = "IVA Types", description = "Operations related to IVA type management")
public class IVATypeController {

    private final IVATypeService ivaTypeService;

    public IVATypeController(IVATypeService ivaTypeService) {
        this.ivaTypeService = ivaTypeService;
    }

    @PostMapping("/create")
    @Operation(summary = "Create a new IVA type")
    public ResponseEntity<IVATypeDTO> create(@Valid @RequestBody IVATypeDTO dto) throws DuplicateEntityException {
        IVAType saved = ivaTypeService.save(dto.toEntity());
        return new ResponseEntity<>(new IVATypeDTO(saved), HttpStatus.CREATED);
    }

    @PutMapping("/update")
    @Operation(summary = "Update an existing IVA type")
    public ResponseEntity<IVATypeDTO> update(@Valid @RequestBody IVATypeDTO dto) throws EntityNotFoundException, InvalidDataException {
        IVAType updated = ivaTypeService.update(dto.toEntity());
        return new ResponseEntity<>(new IVATypeDTO(updated), HttpStatus.OK);
    }

    @GetMapping("/getById/{id}")
    @Operation(summary = "Get IVA type by ID")
    public ResponseEntity<IVATypeDTO> getById(@PathVariable Integer id) throws EntityNotFoundException, InvalidDataException {
        IVAType entity = ivaTypeService.findById(id);
        return new ResponseEntity<>(new IVATypeDTO(entity), HttpStatus.OK);
    }

    @GetMapping("/getAll")
    @Operation(summary = "Get all IVA types")
    public ResponseEntity<List<IVATypeDTO>> getAll() {
        List<IVATypeDTO> dtos = ivaTypeService.findAll()
                .stream().map(IVATypeDTO::new).collect(Collectors.toList());
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @DeleteMapping("/deleteById/{id}")
    @Operation(summary = "Delete IVA type by ID (if not in use)")
    public ResponseEntity<Void> delete(@PathVariable Integer id) throws EntityNotFoundException, InvalidDataException {
        ivaTypeService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
