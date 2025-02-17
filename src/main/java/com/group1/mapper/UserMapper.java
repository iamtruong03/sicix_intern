package com.group1.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.group1.entities.User;
import com.group1.model.request.*;
import com.group1.model.response.UserResponse;

@Mapper(componentModel = "spring")  
public interface UserMapper {
    User toUser(UserRequest request);

    UserResponse toUserResponse(User user);

    User updateUser(@MappingTarget User user, UserUpdateRequest request);
}
