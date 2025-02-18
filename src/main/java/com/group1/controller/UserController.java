package com.group1.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.group1.dto.request.UserRequest;
import com.group1.dto.request.UserUpdateRequest;
import com.group1.dto.response.UserResponse;
import com.group1.entities.Department;
import com.group1.entities.User;
import com.group1.service.PermissionService;
import com.group1.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserService userService;
	@Autowired
    private PermissionService permissionService;

	// Tạo người dùng
	@PostMapping
	public UserResponse createUser(@RequestBody UserRequest request) {
		return userService.createUser(request);
	}

	// Lấy tất cả người dùng
	@GetMapping
	public List<User> getUsers() {
		return userService.getAllUsers();
	}

	// Lấy người dùng theo ID
	@GetMapping("/{id}")
	public User getUser(@PathVariable("id") Long id) {
		return userService.getUserById(id);
	}

	// Cập nhật người dùng
	@PutMapping("/{id}")
	public UserResponse updateUser(@PathVariable("id") Long id, @RequestBody UserUpdateRequest request) {
		return userService.updateUser(id, request);
	}

	// Xóa người dùng
	@DeleteMapping("/{id}")
	public String deleteUser(@PathVariable Long id) {
		userService.delete(id);
		return "User has been deleted";
	}
	
	// 
	 @GetMapping("/{userId}/accessible-departments")
	    public List<Department> getUserAccessibleDepartments(@PathVariable Long userId) {
	        return permissionService.getAccessibleDepartments(userId);
	    }
}