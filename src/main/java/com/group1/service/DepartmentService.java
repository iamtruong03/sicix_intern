package com.group1.service;

import com.group1.entities.Department;
import com.group1.entities.User;
import com.group1.exception.AppException;
import com.group1.exception.ErrorCode;
import com.group1.Repository.UserRepository;
import com.group1.Repository.DepartmentRepository;
import com.group1.dto.request.DepartmentRequest;
import com.group1.dto.response.DepartmentResponse;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DepartmentService {
    @Autowired
    private DepartmentRepository departmentRepository;
    
    @Autowired
    private UserRepository userRepository;

//    public void create(Department department) {
//        // Kiểm tra tên phòng ban đã tồn tại chưa
//        if (departmentRepository.existsByNameDepartment(department.getNameDepartment())) {
//            throw new AppException(ErrorCode.DEPARTMENT_EXISTED);
//        }
//
//        // Xử lý phòng ban cha 
//        Department parent = null;
//        if (department.getParentId() != null) {
//            parent = departmentRepository.findById(department.getParentId())
//                .orElseThrow(() -> new RuntimeException("Phòng ban cha không tồn tại!"));
//        }
//
//        // Gán phòng ban cha và lưu vào DB
//        department.setParentDepartment(parent);
//        departmentRepository.save(department);
//    }
    
 // Lấy danh sách tất cả phòng ban con (bao gồm chính nó)
    private void getAllSubDepartments(Department department, List<Department> subDepartments) {
        subDepartments.add(department);
        List<Department> children = departmentRepository.findByParentDepartment(department);
        for (Department child : children) {
            getAllSubDepartments(child, subDepartments);
        }
    }

    // Lấy danh sách User trong tất cả phòng ban con
    public List<User> getUsersInSubDepartments(Long departmentId) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phòng ban: " + departmentId));

        List<Department> subDepartments = new ArrayList<>();
        getAllSubDepartments(department, subDepartments);

        List<User> users = new ArrayList<>();
        for (Department dep : subDepartments) {
            users.addAll(userRepository.findByDepartment(dep));
        }
        return users;
    }
    
    public DepartmentResponse create(DepartmentRequest request) {
        // Tạo entity từ request
        Department department = new Department();
        department.setNameDepartment(request.getNameDepartment());

        // Xử lý department cha (nếu có)
        if (request.getParentId() != null) {
            Department parent = departmentRepository.findById(request.getParentId())
                    .orElseThrow(() -> new RuntimeException("Phòng ban cha không tồn tại!"));
            department.setParentDepartment(parent);
        }

        // Lưu vào database
        Department savedDepartment = departmentRepository.save(department);

        // Trả về response
        return new DepartmentResponse(savedDepartment.getDepartmentId(), savedDepartment.getNameDepartment());
    }


    public List<Department> getAllDepartment(){
    	return departmentRepository.findAll();
    }
}

