package com.denniseckerskorn.entities.finance;

import com.denniseckerskorn.enums.StatusValues;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "PRODUCTS_SERVICES")
public class ProductService {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "fk_iva_type", nullable = false)
    private IVAType ivaType;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "description", length = 250)
    private String description;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "type", nullable = false, length = 45)
    private String type;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private StatusValues status;

    public ProductService() {

    }

    public Integer getId() {
        return id;
    }

    public IVAType getIvaType() {
        return ivaType;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getType() {
        return type;
    }

    public StatusValues getStatus() {
        return status;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setIvaType(IVAType ivaType) {
        this.ivaType = ivaType;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setStatus(StatusValues status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProductService that = (ProductService) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ProductService{" +
                "id=" + id +
                ", ivaTypeId=" + (ivaType != null ? ivaType.getId() : "null") +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", type='" + type + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
