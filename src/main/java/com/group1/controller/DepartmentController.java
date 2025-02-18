package com.group1.controller;

import com.group1.service.*;
import com.group1.dto.request.DepartmentRequest;
import com.group1.dto.response.DepartmentResponse;
import com.group1.entities.Department;
import com.group1.entities.User;
import com.group1.service.DepartmentService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/departments")
public class DepartmentController {
    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private UserService userService;

    // add-user
    @PostMapping("/{departmentId}/add-user/{userId}")
    public ResponseEntity<String> addUserToDepartment(@PathVariable Long departmentId, @PathVariable Long userId) {
        userService.addUserToDepartment(userId, departmentId);
        return ResponseEntity.ok("Thêm thành viên vào phòng ban thành công");
    }
    
    // add-department
    @PostMapping
    public DepartmentResponse create(@RequestBody DepartmentRequest request) {
    	return departmentService.create(request);
    }
    
    // read-department
    @GetMapping
    public List<Department> getDepartments() {
        return departmentService.getAllDepartment();
    }
    
    // view full department
    @GetMapping("/{departmentId}/users")
    public List<User> getUsersInSubDepartments(@PathVariable Long departmentId) {
        return departmentService.getUsersInSubDepartments(departmentId);
    }

   
}
