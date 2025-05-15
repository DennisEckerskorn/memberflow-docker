package com.denniseckerskorn.dtos.finance_management_dtos;

import com.denniseckerskorn.entities.finance.Invoice;
import com.denniseckerskorn.entities.finance.Payment;
import com.denniseckerskorn.enums.PaymentMethodValues;
import com.denniseckerskorn.enums.StatusValues;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PaymentDTO {

    private Integer id;

    @NotNull
    private Integer invoiceId;

    @NotNull
    private LocalDateTime paymentDate;

    @NotNull
    private BigDecimal amount;

    @NotNull
    private PaymentMethodValues paymentMethod;

    @NotNull
    private StatusValues status;

    public PaymentDTO() {
    }

    public PaymentDTO(Payment entity) {
        this.id = entity.getId();
        this.invoiceId = entity.getInvoice() != null ? entity.getInvoice().getId() : null;
        this.paymentDate = entity.getPaymentDate();
        this.amount = entity.getAmount();
        this.paymentMethod = entity.getPaymentMethod();
        this.status = entity.getStatus();
    }

    public static PaymentDTO fromEntity(Payment entity) {
        return new PaymentDTO(entity);
    }

    public Payment toEntity() {
        Payment payment = new Payment();
        if (this.id != null) {
            payment.setId(this.id);
        }
        payment.setId(this.id);
        payment.setPaymentDate(this.paymentDate);
        payment.setAmount(this.amount);
        payment.setPaymentMethod(this.paymentMethod);
        payment.setStatus(this.status);
        return payment;
    }

    public Payment toEntityWithInvoice(Invoice invoice) {
        Payment payment = new Payment();
        payment.setPaymentDate(this.paymentDate);
        payment.setAmount(this.amount);
        payment.setPaymentMethod(this.paymentMethod);
        payment.setStatus(this.status);
        payment.setInvoice(invoice);
        return payment;
    }


    // Getters y setters...


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Integer invoiceId) {
        this.invoiceId = invoiceId;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public PaymentMethodValues getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethodValues paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public StatusValues getStatus() {
        return status;
    }

    public void setStatus(StatusValues status) {
        this.status = status;
    }
}
