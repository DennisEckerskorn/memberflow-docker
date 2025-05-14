package com.denniseckerskorn.repositories.finance_repositories;

import com.denniseckerskorn.entities.finance.Invoice;
import com.denniseckerskorn.entities.finance.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    boolean existsByInvoiceId(Integer invoiceId);

    List<Payment> findByInvoice_User_Id(Integer userId);
}
