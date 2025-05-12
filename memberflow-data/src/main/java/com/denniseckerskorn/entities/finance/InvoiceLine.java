package com.denniseckerskorn.entities.finance;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "INVOICE_LINES")
public class InvoiceLine {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "fk_invoice", nullable = false)
    private Invoice invoice;

    @ManyToOne
    @JoinColumn(name = "fk_product_service", nullable = false)
    private ProductService productService;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "unit_price", nullable = false)
    private BigDecimal unitPrice;

    @Column(name = "subtotal", nullable = false)
    private BigDecimal subtotal;

    public InvoiceLine() {

    }

    public Integer getId() {
        return id;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public ProductService getProductService() {
        return productService;
    }

    public String getDescription() {
        return description;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InvoiceLine that = (InvoiceLine) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "InvoiceLine{" +
                "id=" + id +
                ", invoiceId=" + (invoice != null ? invoice.getId() : "null") +
                ", productServiceId=" + (productService != null ? productService.getId() : "null") +
                ", quantity=" + quantity +
                ", unitPrice=" + unitPrice +
                ", subtotal=" + subtotal +
                ", description='" + description + '\'' +
                '}';
    }
}
