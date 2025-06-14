package com.example.ecommerce.service;

import com.example.ecommerce.dto.request.UserCreationRequest;
import com.example.ecommerce.dto.request.UserUpdateRequest;
import com.example.ecommerce.dto.response.UserResponse;
import com.example.ecommerce.entity.User;
import com.example.ecommerce.exception.AppException;
import com.example.ecommerce.exception.ErrorCode;
import com.example.ecommerce.mapper.UserMapper;
import com.example.ecommerce.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;

    public UserResponse createUser(UserCreationRequest request) {

        if(userRepository.existsByUsername(request.getUsername()))
           throw new AppException(ErrorCode.USER_EXISTED);

        return userMapper.toUserResponse(userRepository.save(userMapper.toUser(request)));
    }

    public UserResponse updateUser(String userId, UserUpdateRequest request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        userMapper.updateUser(user, request);

        return userMapper.toUserResponse(userRepository.save(user));
    }

    public UserResponse getUser(String userId){
        return userRepository.findById(userId).map(userMapper::toUserResponse)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
    }

    public List<UserResponse> getUsers(){
        return userRepository.findAll().stream().map(userMapper::toUserResponse).toList();
    }

    public void deleteUser(String userId){

        userRepository.deleteById(userId);
    }
}
