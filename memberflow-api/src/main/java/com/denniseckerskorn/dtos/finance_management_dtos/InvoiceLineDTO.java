package com.denniseckerskorn.dtos.finance_management_dtos;

import com.denniseckerskorn.entities.finance.InvoiceLine;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class InvoiceLineDTO {

    private Integer id;

    @NotNull
    private Integer invoiceId;

    @NotNull
    private Integer productServiceId;

    private String description;

    @NotNull
    private Integer quantity;

    @NotNull
    private BigDecimal unitPrice;

    private BigDecimal subtotal;

    public InvoiceLineDTO() {
    }

    public InvoiceLineDTO(InvoiceLine entity) {
        this.id = entity.getId();
        this.invoiceId = entity.getInvoice() != null ? entity.getInvoice().getId() : null;
        this.productServiceId = entity.getProductService() != null ? entity.getProductService().getId() : null;
        this.description = entity.getDescription();
        this.quantity = entity.getQuantity();
        this.unitPrice = entity.getUnitPrice();
        this.subtotal = entity.getSubtotal();
    }

    public static InvoiceLineDTO fromEntity(InvoiceLine entity) {
        return new InvoiceLineDTO(entity);
    }

    public InvoiceLine toEntity() {
        InvoiceLine entity = new InvoiceLine();
        entity.setId(this.id);
        entity.setDescription(this.description);
        entity.setQuantity(this.quantity);
        entity.setUnitPrice(this.unitPrice);
        entity.setSubtotal(this.subtotal);
        return entity;
    }

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

    public Integer getProductServiceId() {
        return productServiceId;
    }

    public void setProductServiceId(Integer productServiceId) {
        this.productServiceId = productServiceId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }
}
