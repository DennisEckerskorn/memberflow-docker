package com.denniseckerskorn.repositories.finance_repositories;

import com.denniseckerskorn.entities.finance.InvoiceLine;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing InvoiceLine entities.
 * Provides methods to perform CRUD operations on InvoiceLine entities.
 */
public interface InvoiceLineRepository extends JpaRepository<InvoiceLine, Integer> {
}
