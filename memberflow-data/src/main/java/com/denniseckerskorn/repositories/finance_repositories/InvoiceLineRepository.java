package com.denniseckerskorn.repositories.finance_repositories;

import com.denniseckerskorn.entities.finance.InvoiceLine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceLineRepository extends JpaRepository<InvoiceLine, Integer> {
}
