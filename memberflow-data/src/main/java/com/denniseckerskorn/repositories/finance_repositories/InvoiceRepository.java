package com.denniseckerskorn.repositories.finance_repositories;

import com.denniseckerskorn.entities.finance.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceRepository extends JpaRepository<Invoice, Integer> {
}
