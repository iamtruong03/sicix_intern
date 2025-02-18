package com.group1.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentResponse {
    private Long departmentId;
    private String nameDepartment;
    private Long parentId;

    public DepartmentResponse(Long departmentId, String nameDepartment) {
        this.departmentId = departmentId;
        this.nameDepartment = nameDepartment;
    }
}
