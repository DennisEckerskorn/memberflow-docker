package com.denniseckerskorn.controllers.finance_management;


import com.denniseckerskorn.dtos.finance_management_dtos.CreateInvoiceLineDTO;
import com.denniseckerskorn.dtos.finance_management_dtos.CreateInvoiceRequestDTO;
import com.denniseckerskorn.dtos.finance_management_dtos.InvoiceDTO;
import com.denniseckerskorn.entities.finance.Invoice;
import com.denniseckerskorn.entities.finance.InvoiceLine;
import com.denniseckerskorn.entities.finance.ProductService;
import com.denniseckerskorn.entities.user_managment.users.User;
import com.denniseckerskorn.exceptions.DuplicateEntityException;
import com.denniseckerskorn.exceptions.EntityNotFoundException;
import com.denniseckerskorn.exceptions.InvalidDataException;
import com.denniseckerskorn.services.finance_service.InvoicePdfGenerator;
import com.denniseckerskorn.services.finance_services.InvoiceService;
import com.denniseckerskorn.services.finance_services.ProductServiceService;
import com.denniseckerskorn.services.user_managment_services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller for managing invoices.
 * Provides endpoints for creating, retrieving, updating, and deleting invoices,
 */
@RestController
@RequestMapping("/api/v1/invoices")
@Tag(name = "Invoices", description = "Operations related to invoice management")
public class InvoiceController {

    private final InvoiceService invoiceService;
    private final UserService userService;
    private final ProductServiceService productServiceService;
    private final InvoicePdfGenerator invoicePdfGenerator;

    /**
     * Constructor for InvoiceController.
     *
     * @param invoiceService        Service for handling invoice records.
     * @param userService           Service for handling user records.
     * @param productServiceService Service for handling product/service records.
     * @param invoicePdfGenerator   Service for generating PDF invoices.
     */
    public InvoiceController(InvoiceService invoiceService, UserService userService, ProductServiceService productServiceService, InvoicePdfGenerator invoicePdfGenerator) {
        this.invoiceService = invoiceService;
        this.userService = userService;
        this.productServiceService = productServiceService;
        this.invoicePdfGenerator = invoicePdfGenerator;
    }

    /**
     * Creates a new invoice with lines.
     *
     * @param dto The CreateInvoiceRequestDTO containing the details of the invoice and its lines.
     * @return ResponseEntity containing the created InvoiceDTO.
     * @throws EntityNotFoundException If the user or product/service is not found.
     * @throws InvalidDataException    If the provided data is invalid.
     */
    @PostMapping("/createInvoiceWithLines")
    @Operation(summary = "Create a new invoice with lines")
    public ResponseEntity<InvoiceDTO> createInvoiceWithLines(@Valid @RequestBody CreateInvoiceRequestDTO dto) throws EntityNotFoundException, InvalidDataException {
        User user = userService.findById(dto.getUserId());
        if (user == null) {
            throw new EntityNotFoundException("User not found");
        }

        Invoice invoice = dto.toEntity(user);
        invoiceService.save(invoice);

        if (dto.getLines() != null && !dto.getLines().isEmpty()) {
            for (CreateInvoiceLineDTO lineDTO : dto.getLines()) {
                ProductService product = productServiceService.findById(lineDTO.getProductServiceId());
                if (product == null) {
                    throw new EntityNotFoundException("Product/Service not found with ID: " + lineDTO.getProductServiceId());
                }
                InvoiceLine line = lineDTO.toEntity(invoice, product);
                invoiceService.addLineToInvoiceById(invoice.getId(), line);
            }
        }
        invoiceService.recalculateTotal(invoice);
        return new ResponseEntity<>(new InvoiceDTO(invoice), HttpStatus.CREATED);
    }

    /**
     * Generates a PDF for an invoice by its ID.
     *
     * @param id The ID of the invoice to generate the PDF for.
     * @return ResponseEntity containing the PDF file as a byte array.
     * @throws EntityNotFoundException If the invoice is not found.
     */
    @GetMapping("/generatePDFById/{id}")
    @Operation(summary = "Generate PDF for invoice by ID")
    public ResponseEntity<byte[]> downloadInvoicePdf(@PathVariable Integer id) throws EntityNotFoundException {
        Invoice invoice = invoiceService.findById(id);

        byte[] pdf = invoicePdfGenerator.generateInvoicePdf(invoice);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=factura_" + id + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    /**
     * Creates a new invoice.
     *
     * @param dto The InvoiceDTO containing the details of the invoice to be created.
     * @return ResponseEntity containing the created InvoiceDTO.
     * @throws DuplicateEntityException If an invoice with the same details already exists.
     */
    @PostMapping("/create")
    @Operation(summary = "Create a new invoice")
    public ResponseEntity<InvoiceDTO> createInvoice(@Valid @RequestBody InvoiceDTO dto) throws DuplicateEntityException {
        Invoice saved = invoiceService.save(dto.toEntity());
        return new ResponseEntity<>(new InvoiceDTO(saved), HttpStatus.CREATED);
    }

    /**
     * Updates an existing invoice.
     *
     * @param dto The InvoiceDTO containing the updated details of the invoice.
     * @return ResponseEntity containing the updated InvoiceDTO.
     * @throws EntityNotFoundException If the invoice to update is not found.
     * @throws InvalidDataException    If the provided data is invalid.
     */
    @PutMapping("/update")
    @Operation(summary = "Update an existing invoice")
    public ResponseEntity<InvoiceDTO> updateInvoice(@Valid @RequestBody InvoiceDTO dto) throws EntityNotFoundException, InvalidDataException {
        Invoice updated = invoiceService.update(dto.toEntity());
        return new ResponseEntity<>(new InvoiceDTO(updated), HttpStatus.OK);
    }

    /**
     * Retrieves an invoice by its ID.
     *
     * @param id The ID of the invoice to retrieve.
     * @return ResponseEntity containing the InvoiceDTO if found, or 404 Not Found if not found.
     * @throws EntityNotFoundException If the invoice is not found.
     * @throws InvalidDataException    If the provided data is invalid.
     */
    @GetMapping("/getById/{id}")
    @Operation(summary = "Get invoice by ID")
    public ResponseEntity<InvoiceDTO> getInvoiceById(@PathVariable Integer id) throws EntityNotFoundException, InvalidDataException {
        Invoice invoice = invoiceService.findById(id);
        return new ResponseEntity<>(new InvoiceDTO(invoice), HttpStatus.OK);
    }

    /**
     * Retrieves all invoices.
     *
     * @return ResponseEntity containing a list of InvoiceDTOs.
     */
    @GetMapping("/getAll")
    @Operation(summary = "Get all invoices")
    public ResponseEntity<List<InvoiceDTO>> getAllInvoices() {
        List<InvoiceDTO> dtos = invoiceService.findAll()
                .stream().map(InvoiceDTO::new).collect(Collectors.toList());
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    /**
     * Deletes an invoice by its ID.
     *
     * @param id The ID of the invoice to delete.
     * @return ResponseEntity with status NO_CONTENT if successful.
     * @throws EntityNotFoundException If the invoice to delete is not found.
     * @throws InvalidDataException    If the provided data is invalid.
     */
    @DeleteMapping("/deleteById/{id}")
    @Operation(summary = "Delete invoice by ID")
    public ResponseEntity<Void> deleteInvoice(@PathVariable Integer id) throws EntityNotFoundException, InvalidDataException {
        invoiceService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Retrieves all invoices associated with a specific user ID.
     *
     * @param userId The ID of the user whose invoices are to be retrieved.
     * @return ResponseEntity containing a list of InvoiceDTOs for the specified user.
     * @throws InvalidDataException If the provided data is invalid.
     */
    @GetMapping("/getAllInvoicesByUserId/{userId}")
    @Operation(summary = "Get all invoices by user ID")
    public ResponseEntity<List<InvoiceDTO>> getInvoicesByUserId(@PathVariable Integer userId) throws InvalidDataException {
        List<InvoiceDTO> dtos = invoiceService.findAllInvoicesByUserId(userId)
                .stream().map(InvoiceDTO::new).collect(Collectors.toList());
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    /**
     * Adds a line to an invoice by its ID.
     *
     * @param invoiceId The ID of the invoice to add the line to.
     * @param line      The InvoiceLine to be added to the invoice.
     * @return ResponseEntity with status OK if successful.
     */
    @PostMapping("/addLinesByInvoiceId/{invoiceId}")
    @Operation(summary = "Add a line to an invoice by invoice ID")
    public ResponseEntity<Void> addLineToInvoice(@PathVariable Integer invoiceId, @RequestBody @Valid InvoiceLine line) {
        invoiceService.addLineToInvoiceById(invoiceId, line);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Removes a line from an invoice by its ID.
     *
     * @param invoiceId The ID of the invoice to remove the line from.
     * @param lineId    The ID of the line to be removed.
     * @return ResponseEntity with status NO_CONTENT if successful.
     * @throws EntityNotFoundException If the invoice or line is not found.
     */
    @DeleteMapping("/removeLineFromInvoiceById/{invoiceId}")
    @Operation(summary = "Remove a line from an invoice by IDs")
    public ResponseEntity<Void> removeLineFromInvoice(@PathVariable Integer invoiceId, @PathVariable Integer lineId) {
        invoiceService.removeLineFromInvoiceById(invoiceId, lineId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Recalculates the total of an invoice by its ID.
     *
     * @param invoiceId The ID of the invoice to recalculate the total for.
     * @return ResponseEntity with status OK if successful.
     * @throws EntityNotFoundException If the invoice is not found.
     * @throws InvalidDataException    If the provided data is invalid.
     */
    @PutMapping("/recalculateTotalOfInvoiceById/{invoiceId}")
    @Operation(summary = "Recalculate the total of an invoice")
    public ResponseEntity<Void> recalculateTotal(@PathVariable Integer invoiceId) throws EntityNotFoundException, InvalidDataException {
        Invoice invoice = invoiceService.findById(invoiceId);
        invoiceService.recalculateTotal(invoice);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Clears all lines from an invoice by its ID.
     *
     * @param invoiceId The ID of the invoice to clear lines from.
     * @return ResponseEntity with status NO_CONTENT if successful.
     * @throws EntityNotFoundException If the invoice is not found.
     * @throws InvalidDataException    If the provided data is invalid.
     */
    @DeleteMapping("/clearAllLinesFromInvoiceById/{invoiceId}")
    @Operation(summary = "Clear all invoice lines from an invoice")
    public ResponseEntity<Void> clearInvoiceLines(@PathVariable Integer invoiceId) throws EntityNotFoundException, InvalidDataException {
        Invoice invoice = invoiceService.findById(invoiceId);
        invoiceService.clearInvoiceLines(invoice);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
