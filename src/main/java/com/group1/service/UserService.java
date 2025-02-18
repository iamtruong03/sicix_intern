package com.group1.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.group1.exception.*;
import com.group1.Repository.DepartmentRepository;
import com.group1.Repository.UserRepository;
import com.group1.dto.request.*;
import com.group1.dto.response.*;
import com.group1.entities.User;
import com.group1.mapper.UserMapper;
import com.group1.entities.Department;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private PasswordEncoder passwordEncoder; // Inject PasswordEncoder

	@Autowired
	private DepartmentService departmentService;

	@Autowired
	private DepartmentRepository departmentRepository;

	// thêm user vào phòng ban
	public void addUserToDepartment(Long userId, Long departmentId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản: " + userId));

		Department department = departmentRepository.findById(departmentId)
				.orElseThrow(() -> new RuntimeException("Không tìm thấy phòng ban: " + departmentId));

		user.setDepartment(department);
		userRepository.save(user);
	}

//	public void create(User user) {
//		user.setPassword(passwordEncoder.encode(user.getPassword()));
//		if (userRepository.existsByNameUser(user.getNameUser())) {
//			throw new AppException(ErrorCode.USER_EXISTED);
//		}
//		userRepository.save(user);
//	}
//
//	public Boolean login(String nameUser, String password) {
//		if (nameUser == null) {
//			throw new AppException(ErrorCode.INVALID_USER);
//		}
//		if (password == null || password.isEmpty()) {
//			throw new AppException(ErrorCode.INVALID_PASSWORD);
//		}
//
//		User user = userRepository.findByNameUser(nameUser)
//				.orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
//
//		if (!passwordEncoder.matches(password, user.getPassword())) {
//			throw new AppException(ErrorCode.WRONG_PASSWORD);
//		}
//		return true;
//	}
//
//	public Boolean changePassword(Long id, String newPassword) {
//
//		if (newPassword == null || newPassword.isEmpty()) {
//			throw new AppException(ErrorCode.INVALID_PASSWORD);
//		}
//
//		User user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
//
//		String encodedPassword = passwordEncoder.encode(newPassword);
//		user.setPassword(encodedPassword);
//
//		userRepository.save(user);
//		return true;
//
//	}
//
//	public Boolean update(User user) {
//		if (userRepository.existsById(user.getId())) {
//			return false;
//		}
//		userRepository.save(user);
//		return true;
//	}

	public Boolean delete(Long id) {
		if (userRepository.existsById(id)) {
			userRepository.deleteById(id);
			return true;
		}
		return false;
	}

	// Lấy tất cả người dùng
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	// Lấy người dùng theo ID
	public User getUserById(Long id) {
		return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
	}

	// Tạo người dùng
	public UserResponse createUser(UserRequest request) {
		// Ánh xạ từ UserRequest sang User entity
		User user = userMapper.toUser(request);

		// Mã hóa mật khẩu
		user.setPassword(passwordEncoder.encode(request.getPassword()));

		// Lưu người dùng vào cơ sở dữ liệu
		return userMapper.toUserResponse(userRepository.save(user));
	}

	public UserResponse updateUser(Long id, UserUpdateRequest request) {
		// Kiểm tra user có tồn tại không trước khi truy vấn
		if (!userRepository.existsById(id)) {
			throw new RuntimeException("User not found");
		}

		// Lấy user từ DB
		User user = userRepository.findById(id).orElseThrow();

		// Cập nhật thông tin người dùng từ request
		userMapper.updateUser(user, request);

		// Mã hóa lại mật khẩu nếu có thay đổi
		Optional.ofNullable(request.getPassword()).filter(password -> !password.isEmpty())
				.ifPresent(password -> user.setPassword(passwordEncoder.encode(password)));

		// Lưu và trả về thông tin người dùng đã cập nhật
		return userMapper.toUserResponse(userRepository.save(user));
	}

}
