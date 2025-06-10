package com.example.ecommerce.controller;

import com.example.ecommerce.dto.request.UserCreationRequest;
import com.example.ecommerce.entity.User;
import com.example.ecommerce.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {

    UserService userService;

    @PostMapping
    public User createUser(@RequestBody UserCreationRequest request){
        return userService.createUser(request);
    }
}
