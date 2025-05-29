package com.denniseckerskorn.controllers.finance_management;

import com.denniseckerskorn.dtos.finance_management_dtos.ProductServiceDTO;
import com.denniseckerskorn.entities.finance.IVAType;
import com.denniseckerskorn.entities.finance.ProductService;
import com.denniseckerskorn.exceptions.DuplicateEntityException;
import com.denniseckerskorn.exceptions.EntityNotFoundException;
import com.denniseckerskorn.exceptions.InvalidDataException;
import com.denniseckerskorn.services.finance_services.IVATypeService;
import com.denniseckerskorn.services.finance_services.ProductServiceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller for managing products and services.
 * Provides endpoints for creating, updating, retrieving, and deleting products/services.
 */
@RestController
@RequestMapping("/api/v1/products-services")
@Tag(name = "Product Services", description = "Operations related to product/service management")
public class ProductServiceController {

    private final ProductServiceService productServiceService;
    private final IVATypeService ivaTypeService;

    /**
     * Constructor for ProductServiceController.
     *
     * @param productServiceService Service for handling product/service records.
     * @param ivaTypeService        Service for handling IVA type records.
     */
    public ProductServiceController(ProductServiceService productServiceService, IVATypeService ivaTypeService) {
        this.productServiceService = productServiceService;
        this.ivaTypeService = ivaTypeService;
    }

    /**
     * Creates a new product/service.
     *
     * @param dto The ProductServiceDTO containing the details of the product/service to be created.
     * @return ResponseEntity containing the created ProductServiceDTO.
     * @throws DuplicateEntityException if a product/service with the same name already exists.
     */
    @PostMapping("/create")
    @Operation(summary = "Create a new product/service")
    public ResponseEntity<ProductServiceDTO> create(@Valid @RequestBody ProductServiceDTO dto) throws DuplicateEntityException {
        IVAType ivaType = ivaTypeService.findById(dto.getIvaTypeId());
        ProductService saved = productServiceService.save(dto.toEntityWithIVA(ivaType));
        return new ResponseEntity<>(new ProductServiceDTO(saved), HttpStatus.CREATED);
    }

    /**
     * Updates an existing product/service.
     *
     * @param dto The ProductServiceDTO containing the updated details of the product/service.
     * @return ResponseEntity containing the updated ProductServiceDTO.
     * @throws EntityNotFoundException if the product/service to update does not exist.
     * @throws InvalidDataException    if the provided data is invalid.
     */
    @PutMapping("/update")
    @Operation(summary = "Update an existing product/service")
    public ResponseEntity<ProductServiceDTO> update(@Valid @RequestBody ProductServiceDTO dto) throws EntityNotFoundException, InvalidDataException {
        IVAType ivaType = ivaTypeService.findById(dto.getIvaTypeId());
        ProductService updated = productServiceService.update(dto.toEntityWithIVA(ivaType));
        return new ResponseEntity<>(new ProductServiceDTO(updated), HttpStatus.OK);
    }

    /**
     * Retrieves a product/service by its ID.
     *
     * @param id The ID of the product/service to retrieve.
     * @return ResponseEntity containing the ProductServiceDTO if found.
     * @throws EntityNotFoundException if the product/service with the specified ID does not exist.
     * @throws InvalidDataException    if the provided ID is invalid.
     */
    @GetMapping("/getById/{id}")
    @Operation(summary = "Get product/service by ID")
    public ResponseEntity<ProductServiceDTO> getById(@PathVariable Integer id) throws EntityNotFoundException, InvalidDataException {
        ProductService entity = productServiceService.findById(id);
        return new ResponseEntity<>(new ProductServiceDTO(entity), HttpStatus.OK);
    }

    /**
     * Retrieves all products/services.
     *
     * @return ResponseEntity containing a list of ProductServiceDTOs.
     */
    @GetMapping("/getAll")
    @Operation(summary = "Get all products/services")
    public ResponseEntity<List<ProductServiceDTO>> getAll() {
        List<ProductServiceDTO> dtos = productServiceService.findAll()
                .stream().map(ProductServiceDTO::new).collect(Collectors.toList());
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    /**
     * Deletes a product/service by its ID.
     *
     * @param id The ID of the product/service to delete.
     * @return ResponseEntity with no content if deletion is successful.
     * @throws EntityNotFoundException if the product/service with the specified ID does not exist.
     * @throws InvalidDataException    if the provided ID is invalid.
     */
    @DeleteMapping("/deleteById/{id}")
    @Operation(summary = "Delete product/service by ID")
    public ResponseEntity<Void> delete(@PathVariable Integer id) throws EntityNotFoundException, InvalidDataException {
        productServiceService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
