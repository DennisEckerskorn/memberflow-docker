package com.denniseckerskorn.dtos.class_managment_dtos;

import com.denniseckerskorn.entities.class_managment.Membership;
import com.denniseckerskorn.enums.MembershipTypeValues;
import com.denniseckerskorn.enums.StatusValues;

import java.time.LocalDate;


public class MembershipDTO {

    private Integer id;
    private LocalDate startDate;
    private LocalDate endDate;
    private MembershipTypeValues type;
    private StatusValues status;

    public MembershipDTO() {
    }

    public MembershipDTO(Integer id, LocalDate startDate, LocalDate endDate, MembershipTypeValues type, StatusValues status) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.type = type;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public MembershipTypeValues getType() {
        return type;
    }

    public void setType(MembershipTypeValues type) {
        this.type = type;
    }

    public StatusValues getStatus() {
        return status;
    }

    public void setStatus(StatusValues status) {
        this.status = status;
    }

    public static MembershipDTO fromEntity(Membership membership) {
        MembershipDTO dto = new MembershipDTO();
        dto.setId(membership.getId());
        dto.setStartDate(membership.getStartDate());
        dto.setEndDate(membership.getEndDate());
        dto.setType(membership.getType());
        dto.setStatus(membership.getStatus());
        return dto;
    }

    public Membership toEntity() {
        Membership membership = new Membership();
        membership.setId(this.id);
        membership.setStartDate(this.startDate);
        membership.setEndDate(this.endDate);
        membership.setType(this.type);
        membership.setStatus(this.status);
        return membership;
    }
}
