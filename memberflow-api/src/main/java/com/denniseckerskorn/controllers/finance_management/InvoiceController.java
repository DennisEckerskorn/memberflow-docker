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

@RestController
@RequestMapping("/api/v1/invoices")
@Tag(name = "Invoices", description = "Operations related to invoice management")
public class InvoiceController {

    private final InvoiceService invoiceService;
    private final UserService userService;
    private final ProductServiceService productServiceService;
    private final InvoicePdfGenerator invoicePdfGenerator;

    public InvoiceController(InvoiceService invoiceService, UserService userService, ProductServiceService productServiceService, InvoicePdfGenerator invoicePdfGenerator) {
        this.invoiceService = invoiceService;
        this.userService = userService;
        this.productServiceService = productServiceService;
        this.invoicePdfGenerator = invoicePdfGenerator;
    }

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
                    throw new EntityNotFoundException("Product/Service not found");
                }
                InvoiceLine line = lineDTO.toEntity(invoice, product);
                invoiceService.addLineToInvoiceById(invoice.getId(), line);
            }
        }
        invoiceService.recalculateTotal(invoice);
        return new ResponseEntity<>(new InvoiceDTO(invoice), HttpStatus.CREATED);
    }

    @GetMapping("/generatePDFById/{id}")
    @Operation(summary = "Generar y descargar factura en PDF")
    public ResponseEntity<byte[]> downloadInvoicePdf(@PathVariable Integer id) throws EntityNotFoundException {
        Invoice invoice = invoiceService.findById(id);

        byte[] pdf = invoicePdfGenerator.generateInvoicePdf(invoice);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=factura_" + id + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }


    @PostMapping("/create")
    @Operation(summary = "Create a new invoice")
    public ResponseEntity<InvoiceDTO> createInvoice(@Valid @RequestBody InvoiceDTO dto) throws DuplicateEntityException {
        Invoice saved = invoiceService.save(dto.toEntity());
        return new ResponseEntity<>(new InvoiceDTO(saved), HttpStatus.CREATED);
    }

    @PutMapping("/update")
    @Operation(summary = "Update an existing invoice")
    public ResponseEntity<InvoiceDTO> updateInvoice(@Valid @RequestBody InvoiceDTO dto) throws EntityNotFoundException, InvalidDataException {
        Invoice updated = invoiceService.update(dto.toEntity());
        return new ResponseEntity<>(new InvoiceDTO(updated), HttpStatus.OK);
    }

    @GetMapping("/getById/{id}")
    @Operation(summary = "Get invoice by ID")
    public ResponseEntity<InvoiceDTO> getInvoiceById(@PathVariable Integer id) throws EntityNotFoundException, InvalidDataException {
        Invoice invoice = invoiceService.findById(id);
        return new ResponseEntity<>(new InvoiceDTO(invoice), HttpStatus.OK);
    }

    @GetMapping("/getAll")
    @Operation(summary = "Get all invoices")
    public ResponseEntity<List<InvoiceDTO>> getAllInvoices() {
        List<InvoiceDTO> dtos = invoiceService.findAll()
                .stream().map(InvoiceDTO::new).collect(Collectors.toList());
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @DeleteMapping("/deleteById/{id}")
    @Operation(summary = "Delete invoice by ID")
    public ResponseEntity<Void> deleteInvoice(@PathVariable Integer id) throws EntityNotFoundException, InvalidDataException {
        invoiceService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/getAllInvoicesByUserId/{userId}")
    @Operation(summary = "Get all invoices by user ID")
    public ResponseEntity<List<InvoiceDTO>> getInvoicesByUserId(@PathVariable Integer userId) throws InvalidDataException {
        List<InvoiceDTO> dtos = invoiceService.findAllInvoicesByUserId(userId)
                .stream().map(InvoiceDTO::new).collect(Collectors.toList());
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @PostMapping("/addLinesByInvoiceId/{invoiceId}")
    @Operation(summary = "Add a line to an invoice by invoice ID")
    public ResponseEntity<Void> addLineToInvoice(@PathVariable Integer invoiceId, @RequestBody @Valid InvoiceLine line) {
        invoiceService.addLineToInvoiceById(invoiceId, line);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/removeLineFromInvoiceById/{invoiceId}")
    @Operation(summary = "Remove a line from an invoice by IDs")
    public ResponseEntity<Void> removeLineFromInvoice(@PathVariable Integer invoiceId, @PathVariable Integer lineId) {
        invoiceService.removeLineFromInvoiceById(invoiceId, lineId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/recalculateTotalOfInvoiceById/{invoiceId}")
    @Operation(summary = "Recalculate the total of an invoice")
    public ResponseEntity<Void> recalculateTotal(@PathVariable Integer invoiceId) throws EntityNotFoundException, InvalidDataException {
        Invoice invoice = invoiceService.findById(invoiceId);
        invoiceService.recalculateTotal(invoice);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/clearAllLinesFromInvoiceById/{invoiceId}")
    @Operation(summary = "Clear all invoice lines from an invoice")
    public ResponseEntity<Void> clearInvoiceLines(@PathVariable Integer invoiceId) throws EntityNotFoundException, InvalidDataException {
        Invoice invoice = invoiceService.findById(invoiceId);
        invoiceService.clearInvoiceLines(invoice);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
