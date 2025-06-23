package com.example.ecommerce.controller;

import com.example.ecommerce.dto.request.AuthenticationRequest;
import com.example.ecommerce.dto.request.IntrospectRequest;
import com.example.ecommerce.dto.request.LogoutRequest;
import com.example.ecommerce.dto.response.ApiResponse;
import com.example.ecommerce.dto.response.AuthenticationResponse;
import com.example.ecommerce.dto.response.IntrospectResponse;
import com.example.ecommerce.exception.AppException;
import com.example.ecommerce.exception.ErrorCode;
import com.example.ecommerce.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {

    AuthenticationService authenticationService;

    @PostMapping("/token")
    ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request){
        AuthenticationResponse result = authenticationService.authenticate(request);

        return ApiResponse.<AuthenticationResponse>builder()
                .result(result)
                .build();
    }

    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest request) throws ParseException, JOSEException {
        IntrospectResponse result = authenticationService.introspect(request);

        return ApiResponse.<IntrospectResponse>builder()
                .result(result)
                .build();
    }

    @PostMapping("/logout")
    ApiResponse<Void> logout(@RequestBody LogoutRequest request) throws ParseException, JOSEException {
        authenticationService.logout(request);

        return ApiResponse.<Void>builder()
                .message("Logout successfully")
                .build();
    }
}
