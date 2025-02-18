package com.group1.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.group1.dto.response.DepartmentResponse;
import com.group1.entities.Department;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {

    @Mapping(source = "parentDepartment.departmentId", target = "parentId")
    DepartmentResponse toResponse(Department department);

    List<DepartmentResponse> toResponseList(List<Department> departments);
}

