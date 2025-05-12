package com.denniseckerskorn.entities.finance;

import com.denniseckerskorn.entities.user_managment.users.User;
import com.denniseckerskorn.enums.StatusValues;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "INVOICES")
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "fk_user", nullable = false)
    private User user;

    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    @Column(name = "total", nullable = false)
    private BigDecimal total;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StatusValues status;

    @OneToOne(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = false)
    private Payment payment;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<InvoiceLine> invoiceLines = new HashSet<>();

    public Invoice() {
    }

    public Integer getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public StatusValues getStatus() {
        return status;
    }

    public Payment getPayment() {
        return payment;
    }

    public Set<InvoiceLine> getInvoiceLines() {
        return invoiceLines;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public void setStatus(StatusValues status) {
        this.status = status;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public void setInvoiceLines(Set<InvoiceLine> invoiceLines) {
        this.invoiceLines = invoiceLines;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Invoice invoice = (Invoice) o;
        return Objects.equals(id, invoice.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Invoice{" +
                "id=" + id +
                ", userId=" + (user != null ? user.getId() : "null") +
                ", date=" + date +
                ", total=" + total +
                ", status='" + status + '\'' +
                ", paymentId=" + (payment != null ? payment.getId() : "null") +
                ", invoiceLines= [size hidden for lazy loading safety]" +
                '}';
    }

}