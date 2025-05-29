package com.denniseckerskorn.repositories.finance_repositories;

import com.denniseckerskorn.entities.finance.ProductService;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing ProductService entities.
 * Provides methods to perform CRUD operations on ProductService entities.
 */
public interface ProductServiceRepository extends JpaRepository<ProductService, Integer> {
    boolean existsByName(String name);

    boolean existsByIvaTypeId(Integer ivaTypeId);

    ProductService findByName(String name);
}
