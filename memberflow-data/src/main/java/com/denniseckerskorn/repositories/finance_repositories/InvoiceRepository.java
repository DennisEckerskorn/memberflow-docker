package com.denniseckerskorn.repositories.finance_repositories;

import com.denniseckerskorn.entities.finance.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing Invoice entities.
 * Provides methods to perform CRUD operations on Invoice entities.
 */
public interface InvoiceRepository extends JpaRepository<Invoice, Integer> {
}
