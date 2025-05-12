package com.denniseckerskorn.repositories.finance_repositories;

import com.denniseckerskorn.entities.finance.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    boolean existsByInvoiceId(Integer invoiceId);
}
