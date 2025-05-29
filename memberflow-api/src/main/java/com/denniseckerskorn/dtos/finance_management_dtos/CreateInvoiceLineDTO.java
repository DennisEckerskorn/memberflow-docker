package com.denniseckerskorn.dtos.finance_management_dtos;

import com.denniseckerskorn.entities.finance.Invoice;
import com.denniseckerskorn.entities.finance.InvoiceLine;
import com.denniseckerskorn.entities.finance.ProductService;

import java.math.BigDecimal;

public class CreateInvoiceLineDTO {

    private Integer productServiceId;
    private int quantity;
    private BigDecimal unitPrice;

    public CreateInvoiceLineDTO() {
    }

    public Integer getProductServiceId() {
        return productServiceId;
    }

    public void setProductServiceId(Integer productServiceId) {
        this.productServiceId = productServiceId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public InvoiceLine toEntity(Invoice invoice, ProductService productService) {
        InvoiceLine entity = new InvoiceLine();
        entity.setInvoice(invoice);
        entity.setProductService(productService);
        entity.setQuantity(this.quantity);
        entity.setUnitPrice(this.unitPrice);
        entity.setSubtotal(this.unitPrice.multiply(BigDecimal.valueOf(this.quantity)));
        entity.setDescription(productService.getName());
        return entity;
    }

    public static CreateInvoiceLineDTO fromEntity(InvoiceLine entity) {
        CreateInvoiceLineDTO dto = new CreateInvoiceLineDTO();
        dto.setProductServiceId(entity.getProductService() != null ? entity.getProductService().getId() : null);
        dto.setQuantity(entity.getQuantity());
        dto.setUnitPrice(entity.getUnitPrice());
        return dto;
    }
}
