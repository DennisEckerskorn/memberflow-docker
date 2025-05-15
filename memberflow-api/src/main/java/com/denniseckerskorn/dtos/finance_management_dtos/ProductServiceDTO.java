package com.denniseckerskorn.dtos.finance_management_dtos;

import com.denniseckerskorn.entities.finance.IVAType;
import com.denniseckerskorn.entities.finance.ProductService;
import com.denniseckerskorn.enums.StatusValues;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class ProductServiceDTO {

    private Integer id;

    @NotNull
    private Integer ivaTypeId;

    private IVATypeDTO ivaType;

    @NotNull
    private String name;

    private String description;

    @NotNull
    private BigDecimal price;

    @NotNull
    private String type;

    @NotNull
    private StatusValues status;

    public ProductServiceDTO() {
    }

    public ProductServiceDTO(ProductService entity) {
        this.id = entity.getId();
        this.ivaTypeId = entity.getIvaType() != null ? entity.getIvaType().getId() : null;
        this.ivaType = entity.getIvaType() != null ? new IVATypeDTO(entity.getIvaType()) : null;
        this.name = entity.getName();
        this.description = entity.getDescription();
        this.price = entity.getPrice();
        this.type = entity.getType();
        this.status = entity.getStatus();
    }

    public static ProductServiceDTO fromEntity(ProductService entity) {
        return new ProductServiceDTO(entity);
    }

    public ProductService toEntity() {
        ProductService ps = new ProductService();
        ps.setId(this.id);
        ps.setName(this.name);
        ps.setDescription(this.description);
        ps.setPrice(this.price);
        ps.setType(this.type);
        ps.setStatus(this.status);
        return ps;
    }

    public ProductService toEntityWithIVA(IVAType ivaType) {
        ProductService entity = new ProductService();
        entity.setId(this.id);
        entity.setName(this.name);
        entity.setPrice(this.price);
        entity.setType(this.type);
        entity.setStatus(this.status);
        entity.setIvaType(ivaType);
        return entity;
    }

    // Getters y setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIvaTypeId() {
        return ivaTypeId;
    }

    public void setIvaTypeId(Integer ivaTypeId) {
        this.ivaTypeId = ivaTypeId;
    }

    public IVATypeDTO getIvaType() {
        return ivaType;
    }

    public void setIvaType(IVATypeDTO ivaType) {
        this.ivaType = ivaType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public StatusValues getStatus() {
        return status;
    }

    public void setStatus(StatusValues status) {
        this.status = status;
    }
}
