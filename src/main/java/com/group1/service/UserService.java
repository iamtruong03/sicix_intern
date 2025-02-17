package com.group1.service;

import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.group1.exception.*;
import com.group1.Repository.UserRepository;
import com.group1.entities.User;
import com.group1.mapper.UserMapper;
import com.group1.model.request.*;
import com.group1.model.response.*;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private PasswordEncoder passwordEncoder; // Inject PasswordEncoder

	public void create(User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		if (userRepository.existsByNameUser(user.getNameUser())) {
			throw new AppException(ErrorCode.USER_EXISTED);
		}
		userRepository.save(user);
	}

	public Boolean login(String nameUser, String password) {
		if( nameUser == null) {
			throw new AppException(ErrorCode.INVALID_USER);
		}
		if (password == null || password.isEmpty()) {
			throw new AppException(ErrorCode.INVALID_PASSWORD);
		}
		
		User user = userRepository.findByNameUser(nameUser).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

		if (!passwordEncoder.matches(password, user.getPassword())) {
			throw new AppException(ErrorCode.WRONG_PASSWORD);
		}
		return true;
	}

	public Boolean changePassword(Long id, String newPassword) {
		
		if (newPassword == null || newPassword.isEmpty()) {
			throw new AppException(ErrorCode.INVALID_PASSWORD);
		}
		
		User user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

		String encodedPassword = passwordEncoder.encode(newPassword);
		user.setPassword(encodedPassword);

		userRepository.save(user);
		return true;

	}

	public Boolean update(User user) {
		if (userRepository.existsById(user.getId())) {
			return false;
		}
		userRepository.save(user);
		return true;
	}

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

	// Xóa người dùng
	public void deleteUser(Long id) {
		userRepository.deleteById(id);
	}

//    // Tạo người dùng
//    public UserResponse createUser(UserRequest request) {
//        // Ánh xạ từ UserRequest sang User entity
//        User user = userMapper.toUser(request);
//
//        // Mã hóa mật khẩu
//        user.setPassword(passwordEncoder.encode(request.getPassword()));
//
//        // Lưu người dùng vào cơ sở dữ liệu
//        return userMapper.toUserResponse(userRepository.save(user));
//    }
//
//    // Cập nhật thông tin người dùng
//    public UserResponse updateUser(Long id, UserUpdateRequest request) {
//        // Tìm người dùng theo ID
//        User user = userRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        // Cập nhật thông tin người dùng từ request
//        user = userMapper.updateUser(user, request);
//
//        // Mã hóa lại mật khẩu chỉ khi có thay đổi
//        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
//            user.setPassword(passwordEncoder.encode(request.getPassword()));
//        }
//
//        // Lưu và trả về thông tin người dùng đã cập nhật
//        return userMapper.toUserResponse(userRepository.save(user));
//    }

}
