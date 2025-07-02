package com.example.ecommerce.mapper;

import com.example.ecommerce.dto.request.user.UserCreationRequest;
import com.example.ecommerce.dto.request.user.UserUpdateRequest;
import com.example.ecommerce.dto.response.user.UserResponse;
import com.example.ecommerce.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;


@Mapper
public interface UserMapper{

    @Mapping(target = "roles", ignore = true)
    User toUser(UserCreationRequest request);

    UserResponse toUserResponse(User user);

    @Mapping(target = "roles", ignore = true)
    void updateUser(@MappingTarget User user, UserUpdateRequest request);
}
