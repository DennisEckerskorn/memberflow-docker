package com.denniseckerskorn.controllers.finance_management;

import com.denniseckerskorn.dtos.finance_management_dtos.ProductServiceDTO;
import com.denniseckerskorn.entities.finance.ProductService;
import com.denniseckerskorn.exceptions.DuplicateEntityException;
import com.denniseckerskorn.exceptions.EntityNotFoundException;
import com.denniseckerskorn.exceptions.InvalidDataException;
import com.denniseckerskorn.services.finance_services.ProductServiceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/products-services")
@Tag(name = "Product Services", description = "Operations related to product/service management")
public class ProductServiceController {

    private final ProductServiceService productServiceService;

    public ProductServiceController(ProductServiceService productServiceService) {
        this.productServiceService = productServiceService;
    }

    @PostMapping("/create")
    @Operation(summary = "Create a new product/service")
    public ResponseEntity<ProductServiceDTO> create(@Valid @RequestBody ProductServiceDTO dto) throws DuplicateEntityException {
        ProductService saved = productServiceService.save(dto.toEntity());
        return new ResponseEntity<>(new ProductServiceDTO(saved), HttpStatus.CREATED);
    }

    @PutMapping("/update")
    @Operation(summary = "Update an existing product/service")
    public ResponseEntity<ProductServiceDTO> update(@Valid @RequestBody ProductServiceDTO dto) throws EntityNotFoundException, InvalidDataException {
        ProductService updated = productServiceService.update(dto.toEntity());
        return new ResponseEntity<>(new ProductServiceDTO(updated), HttpStatus.OK);
    }

    @GetMapping("/getById/{id}")
    @Operation(summary = "Get product/service by ID")
    public ResponseEntity<ProductServiceDTO> getById(@PathVariable Integer id) throws EntityNotFoundException, InvalidDataException {
        ProductService entity = productServiceService.findById(id);
        return new ResponseEntity<>(new ProductServiceDTO(entity), HttpStatus.OK);
    }

    @GetMapping("/getAll")
    @Operation(summary = "Get all products/services")
    public ResponseEntity<List<ProductServiceDTO>> getAll() {
        List<ProductServiceDTO> dtos = productServiceService.findAll()
                .stream().map(ProductServiceDTO::new).collect(Collectors.toList());
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @DeleteMapping("/deleteById/{id}")
    @Operation(summary = "Delete product/service by ID")
    public ResponseEntity<Void> delete(@PathVariable Integer id) throws EntityNotFoundException, InvalidDataException {
        productServiceService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
