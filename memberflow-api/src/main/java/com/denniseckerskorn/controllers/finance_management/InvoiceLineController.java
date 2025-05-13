package com.denniseckerskorn.controllers.finance_management;

import com.denniseckerskorn.dtos.finance_management_dtos.InvoiceLineDTO;
import com.denniseckerskorn.entities.finance.InvoiceLine;
import com.denniseckerskorn.exceptions.DuplicateEntityException;
import com.denniseckerskorn.exceptions.EntityNotFoundException;
import com.denniseckerskorn.exceptions.InvalidDataException;
import com.denniseckerskorn.services.finance_services.InvoiceLineService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * InvoiceLineController handles operations related to invoice lines (items in invoices).
 */
@RestController
@RequestMapping("/api/v1/invoice-lines")
@Tag(name = "Invoice Lines", description = "Operations related to invoice lines")
public class InvoiceLineController {

    private final InvoiceLineService invoiceLineService;

    public InvoiceLineController(InvoiceLineService invoiceLineService) {
        this.invoiceLineService = invoiceLineService;
    }

    @PostMapping("/create")
    @Operation(summary = "Create a new invoice line")
    public ResponseEntity<InvoiceLineDTO> create(@Valid @RequestBody InvoiceLineDTO dto) throws DuplicateEntityException {
        InvoiceLine saved = invoiceLineService.save(dto.toEntity());
        return new ResponseEntity<>(new InvoiceLineDTO(saved), HttpStatus.CREATED);
    }

    @PutMapping("/update")
    @Operation(summary = "Update an existing invoice line")
    public ResponseEntity<InvoiceLineDTO> update(@Valid @RequestBody InvoiceLineDTO dto) throws InvalidDataException, EntityNotFoundException {
        InvoiceLine updated = invoiceLineService.update(dto.toEntity());
        return new ResponseEntity<>(new InvoiceLineDTO(updated), HttpStatus.OK);
    }

    @GetMapping("/getById/{id}")
    @Operation(summary = "Get invoice line by ID")
    public ResponseEntity<InvoiceLineDTO> getById(@PathVariable Integer id) throws InvalidDataException, EntityNotFoundException {
        InvoiceLine line = invoiceLineService.findById(id);
        return new ResponseEntity<>(new InvoiceLineDTO(line), HttpStatus.OK);
    }

    @GetMapping("/getAll")
    @Operation(summary = "Get all invoice lines")
    public ResponseEntity<List<InvoiceLineDTO>> getAll() {
        List<InvoiceLineDTO> dtos = invoiceLineService.findAll()
                .stream().map(InvoiceLineDTO::new).collect(Collectors.toList());
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @DeleteMapping("/deleteById/{id}")
    @Operation(summary = "Delete invoice line by ID")
    public ResponseEntity<Void> delete(@PathVariable Integer id) throws InvalidDataException, EntityNotFoundException {
        invoiceLineService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
