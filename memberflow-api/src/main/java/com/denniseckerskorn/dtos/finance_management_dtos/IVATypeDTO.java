package com.denniseckerskorn.dtos.finance_management_dtos;

import com.denniseckerskorn.entities.finance.IVAType;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class IVATypeDTO {

    private Integer id;

    @NotNull
    private BigDecimal percentage;

    private String description;

    public IVATypeDTO() {}

    public IVATypeDTO(IVAType entity) {
        this.id = entity.getId();
        this.percentage = entity.getPercentage();
        this.description = entity.getDescription();
    }

    public static IVATypeDTO fromEntity(IVAType entity) {
        return new IVATypeDTO(entity);
    }

    public IVAType toEntity() {
        IVAType iva = new IVAType();
        iva.setId(this.id);
        iva.setPercentage(this.percentage);
        iva.setDescription(this.description);
        return iva;
    }

    // Getters y setters...


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getPercentage() {
        return percentage;
    }

    public void setPercentage(BigDecimal percentage) {
        this.percentage = percentage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
