package com.denniseckerskorn.entities.finance;

import com.denniseckerskorn.enums.PaymentMethodValues;
import com.denniseckerskorn.enums.StatusValues;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "PAYMENTS")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "fk_invoice", nullable = false)
    private Invoice invoice;

    @Column(name = "payment_date", nullable = false)
    private LocalDateTime paymentDate;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false, length = 50)
    private PaymentMethodValues paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private StatusValues status;

    public Payment() {

    }

    public Integer getId() {
        return id;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public PaymentMethodValues getPaymentMethod() {
        return paymentMethod;
    }

    public StatusValues getStatus() {
        return status;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setPaymentMethod(PaymentMethodValues paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public void setStatus(StatusValues status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Payment payment = (Payment) o;
        return Objects.equals(id, payment.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Payment{" +
                "id=" + id +
                ", invoiceId=" + (invoice != null ? invoice.getId() : "null") +
                ", paymentDate=" + paymentDate +
                ", amount=" + amount +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
