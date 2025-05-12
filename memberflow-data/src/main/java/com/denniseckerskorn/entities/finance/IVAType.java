package com.denniseckerskorn.entities.finance;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "IVA_TYPE")
public class IVAType {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "percentage", nullable = false)
    private BigDecimal percentage;

    @Column(name = "description", length = 50)
    private String description;

    public IVAType() {

    }

    public Integer getId() {
        return id;
    }

    public BigDecimal getPercentage() {
        return percentage;
    }

    public String getDescription() {
        return description;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setPercentage(BigDecimal percentage) {
        this.percentage = percentage;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IVAType ivaType = (IVAType) o;
        return Objects.equals(id, ivaType.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "IvaType{" +
                "id=" + id +
                ", percentage=" + percentage +
                ", description='" + description + '\'' +
                '}';
    }
}
