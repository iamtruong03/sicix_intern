package com.group1.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.group1.Repository.DepartmentRepository;
import com.group1.Repository.UserRepository;
import com.group1.entities.Department;
import com.group1.entities.User;

@Service
public class PermissionService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    // Lấy danh sách phòng ban con
    private void getAllSubDepartments(Department department, List<Department> subDepartments) {
        subDepartments.add(department);
        List<Department> children = departmentRepository.findByParentDepartment(department);
        for (Department child : children) {
            getAllSubDepartments(child, subDepartments);
        }
    }

    // Lấy danh sách phòng ban mà User có quyền truy cập
    public List<Department> getAccessibleDepartments(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản: " + userId));

        if (user.getDepartment() == null) {
            throw new RuntimeException("Người dùng không thuộc phòng ban nào, không có quyền truy cập.");
        }
        
        List<Department> accessibleDepartments = new ArrayList<>();

        // Lấy phòng ban của User
        Department userDepartment = user.getDepartment();

        // Lấy tất cả phòng ban con mà User có quyền truy cập
        getAllSubDepartments(userDepartment, accessibleDepartments);

        return accessibleDepartments;
    }
    
   
}
