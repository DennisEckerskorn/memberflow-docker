package com.denniseckerskorn.dtos.user_managment_dtos;

import java.util.Set;

public class StudentMiniDTO {
    private Integer id;
    private Set<StudentHistoryMiniDTO> histories; // 🔥 mini DTO

    public StudentMiniDTO() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Set<StudentHistoryMiniDTO> getHistories() {
        return histories;
    }

    public void setHistories(Set<StudentHistoryMiniDTO> histories) {
        this.histories = histories;
    }
}
