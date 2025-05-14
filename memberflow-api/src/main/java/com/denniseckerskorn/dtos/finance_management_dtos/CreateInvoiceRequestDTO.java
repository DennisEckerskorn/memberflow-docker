package com.denniseckerskorn.dtos.finance_management_dtos;

import com.denniseckerskorn.entities.finance.Invoice;
import com.denniseckerskorn.entities.user_managment.users.User;
import com.denniseckerskorn.enums.StatusValues;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

public class CreateInvoiceRequestDTO {

    private Integer userId;
    private LocalDateTime date;
    private StatusValues status;
    private BigDecimal total;
    private Set<CreateInvoiceLineDTO> lines;

    public CreateInvoiceRequestDTO() {
    }

    // Getters y setters

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public StatusValues getStatus() {
        return status;
    }

    public void setStatus(StatusValues status) {
        this.status = status;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Set<CreateInvoiceLineDTO> getLines() {
        return lines;
    }

    public void setLines(Set<CreateInvoiceLineDTO> lines) {
        this.lines = lines;
    }

    // Convierte el DTO a entidad base (sin líneas, requiere setUser)
    public Invoice toEntity(User user) {
        Invoice invoice = new Invoice();
        invoice.setUser(user);
        invoice.setDate(this.date);
        invoice.setStatus(this.status);
        invoice.setTotal(this.total);
        return invoice;
    }

    // Crea el DTO desde una entidad (opcional)
    public static CreateInvoiceRequestDTO fromEntity(Invoice invoice) {
        CreateInvoiceRequestDTO dto = new CreateInvoiceRequestDTO();
        dto.setUserId(invoice.getUser() != null ? invoice.getUser().getId().intValue() : null);
        dto.setDate(invoice.getDate());
        dto.setStatus(invoice.getStatus());
        dto.setTotal(invoice.getTotal());
        dto.setLines(invoice.getInvoiceLines() != null ?
                invoice.getInvoiceLines().stream()
                        .map(CreateInvoiceLineDTO::fromEntity)
                        .collect(Collectors.toSet()) :
                null);
        return dto;
    }
}
