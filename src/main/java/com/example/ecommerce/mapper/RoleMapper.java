package com.example.ecommerce.mapper;

import com.example.ecommerce.dto.request.UserCreationRequest;
import com.example.ecommerce.dto.request.UserUpdateRequest;
import com.example.ecommerce.dto.response.UserResponse;
import com.example.ecommerce.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;


@Mapper
public interface UserMapper{

    User toUser(UserCreationRequest request);

    UserResponse toUserResponse(User user);

    void updateUser(@MappingTarget User user, UserUpdateRequest request);
}
