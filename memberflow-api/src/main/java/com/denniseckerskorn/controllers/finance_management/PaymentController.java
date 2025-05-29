package com.denniseckerskorn.controllers.finance_management;

import com.denniseckerskorn.dtos.finance_management_dtos.PaymentDTO;
import com.denniseckerskorn.entities.finance.Invoice;
import com.denniseckerskorn.entities.finance.Payment;
import com.denniseckerskorn.exceptions.DuplicateEntityException;
import com.denniseckerskorn.exceptions.EntityNotFoundException;
import com.denniseckerskorn.exceptions.InvalidDataException;
import com.denniseckerskorn.services.finance_services.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * PaymentController handles requests related to payment management.
 * It provides endpoints for creating, updating, retrieving, and deleting payments.
 */
@RestController
@RequestMapping("/api/v1/payments")
@Tag(name = "Payments", description = "Operations related to payment management")
public class PaymentController {

    private final PaymentService paymentService;

    /**
     * Constructor for PaymentController.
     *
     * @param paymentService Service for handling payment records.
     */
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    /**
     * Creates a new payment and marks the associated invoice as PAID.
     *
     * @param dto The PaymentDTO containing the details of the payment to be created.
     * @return ResponseEntity containing the created PaymentDTO.
     * @throws DuplicateEntityException if a payment already exists for the given invoice ID.
     * @throws EntityNotFoundException  if the invoice with the given ID does not exist.
     */
    @PostMapping("/create")
    @Operation(summary = "Create a new payment and mark invoice as PAID")
    public ResponseEntity<PaymentDTO> create(@Valid @RequestBody PaymentDTO dto)
            throws DuplicateEntityException, EntityNotFoundException {

        Invoice invoice = paymentService.getInvoiceById(dto.getInvoiceId());
        if (invoice == null) {
            throw new EntityNotFoundException("Invoice not found with ID: " + dto.getInvoiceId());
        }

        Payment saved = paymentService.save(dto.toEntityWithInvoice(invoice));

        if (saved == null) {
            throw new DuplicateEntityException("Payment already exists for invoice ID: " + dto.getInvoiceId());
        }
        return new ResponseEntity<>(new PaymentDTO(saved), HttpStatus.CREATED);
    }

    /**
     * Updates an existing payment and adjusts the invoice status accordingly.
     *
     * @param dto The PaymentDTO containing the updated details of the payment.
     * @return ResponseEntity containing the updated PaymentDTO.
     * @throws EntityNotFoundException if the invoice with the given ID does not exist.
     * @throws InvalidDataException    if the provided data is invalid.
     */
    @PutMapping("/update")
    @Operation(summary = "Update an existing payment and adjust invoice status")
    public ResponseEntity<PaymentDTO> update(@Valid @RequestBody PaymentDTO dto)
            throws EntityNotFoundException, InvalidDataException {

        Invoice invoice = paymentService.getInvoiceById(dto.getInvoiceId());
        if (invoice == null) {
            throw new EntityNotFoundException("Invoice not found with ID: " + dto.getInvoiceId());
        }
        Payment updated = paymentService.update(dto.toEntityWithInvoice(invoice));

        if (updated == null) {
            throw new InvalidDataException("Payment update failed for invoice ID: " + dto.getInvoiceId());
        }

        return new ResponseEntity<>(new PaymentDTO(updated), HttpStatus.OK);
    }

    /**
     * Retrieves a payment by its ID.
     *
     * @param id The ID of the payment to retrieve.
     * @return ResponseEntity containing the PaymentDTO if found.
     * @throws EntityNotFoundException if the payment with the given ID does not exist.
     * @throws InvalidDataException    if the provided ID is invalid.
     */
    @GetMapping("/getById/{id}")
    @Operation(summary = "Get payment by ID")
    public ResponseEntity<PaymentDTO> getById(@PathVariable Integer id) throws EntityNotFoundException, InvalidDataException {
        Payment payment = paymentService.findById(id);
        return new ResponseEntity<>(new PaymentDTO(payment), HttpStatus.OK);
    }

    /**
     * Retrieves all payments.
     *
     * @return ResponseEntity containing a list of PaymentDTOs.
     */
    @GetMapping("/getAll")
    @Operation(summary = "Get all payments")
    public ResponseEntity<List<PaymentDTO>> getAll() {
        List<PaymentDTO> dtos = paymentService.findAll()
                .stream().map(PaymentDTO::new).collect(Collectors.toList());
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    /**
     * Deletes a payment by its ID and sets the associated invoice status to NOT_PAID.
     *
     * @param id The ID of the payment to delete.
     * @return ResponseEntity with no content status.
     * @throws EntityNotFoundException if the payment with the given ID does not exist.
     * @throws InvalidDataException    if the provided ID is invalid.
     */
    @DeleteMapping("/deleteById/{id}")
    @Operation(summary = "Remove a payment and set invoice status to NOT_PAID")
    public ResponseEntity<Void> removePayment(@PathVariable Integer id) throws EntityNotFoundException, InvalidDataException {
        paymentService.removePayment(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Retrieves all payments made by a specific user.
     *
     * @param userId The ID of the user whose payments are to be retrieved.
     * @return ResponseEntity containing a list of PaymentDTOs for the specified user.
     */
    @GetMapping("/getAllByUserId/{userId}")
    @Operation(summary = "Get all payments by user ID")
    public ResponseEntity<List<PaymentDTO>> getAllByUserId(@PathVariable Integer userId) {
        List<PaymentDTO> dtos = paymentService.findAllByUserId(userId)
                .stream().map(PaymentDTO::new).collect(Collectors.toList());
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

}
