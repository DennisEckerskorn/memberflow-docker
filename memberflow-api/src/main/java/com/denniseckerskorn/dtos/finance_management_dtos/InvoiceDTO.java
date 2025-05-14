package com.denniseckerskorn.dtos.finance_management_dtos;

import com.denniseckerskorn.dtos.user_managment_dtos.UserDTO;
import com.denniseckerskorn.entities.finance.Invoice;
import com.denniseckerskorn.enums.StatusValues;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

public class InvoiceDTO {

    private Integer id;

    @NotNull
    private Integer userId;

    private UserDTO user;


    @NotNull
    private LocalDateTime date;

    @NotNull
    private BigDecimal total;

    @NotNull
    private StatusValues status;

    private Integer paymentId;

    private Set<Integer> invoiceLineIds;

    public InvoiceDTO() {
    }

    public InvoiceDTO(Invoice invoice) {
        this.id = invoice.getId();
        this.userId = invoice.getUser() != null ? invoice.getUser().getId() : null;
        this.user = invoice.getUser() != null ? new UserDTO(invoice.getUser()) : null;
        this.date = invoice.getDate();
        this.total = invoice.getTotal();
        this.status = invoice.getStatus();
        this.paymentId = invoice.getPayment() != null ? invoice.getPayment().getId() : null;
        this.invoiceLineIds = invoice.getInvoiceLines()
                .stream()
                .map(line -> line.getId())
                .collect(Collectors.toSet());
    }

    public static InvoiceDTO fromEntity(Invoice invoice) {
        return new InvoiceDTO(invoice);
    }

    public Invoice toEntity() {
        Invoice invoice = new Invoice();
        invoice.setId(this.id);
        invoice.setDate(this.date);
        invoice.setTotal(this.total);
        invoice.setStatus(this.status);
        return invoice;
    }

    // Getters y setters (puedes generarlos automáticamente)


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public StatusValues getStatus() {
        return status;
    }

    public void setStatus(StatusValues status) {
        this.status = status;
    }

    public Integer getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Integer paymentId) {
        this.paymentId = paymentId;
    }

    public Set<Integer> getInvoiceLineIds() {
        return invoiceLineIds;
    }

    public void setInvoiceLineIds(Set<Integer> invoiceLineIds) {
        this.invoiceLineIds = invoiceLineIds;
    }
}
