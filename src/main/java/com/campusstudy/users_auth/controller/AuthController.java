package com.campusstudy.users_auth.controller;

import com.campusstudy.users_auth.dto.AuthResponse;
import com.campusstudy.users_auth.dto.LoginRequest;
import com.campusstudy.users_auth.dto.RegisterRequest;
import com.campusstudy.users_auth.dto.UserResponse;
import com.campusstudy.users_auth.dto.ValidateTokenRequest;
import com.campusstudy.users_auth.dto.ValidateTokenResponse;
import org.springframework.http.ResponseEntity;

public interface AuthController {

    ResponseEntity<UserResponse> registrar(RegisterRequest request);

    ResponseEntity<AuthResponse> login(LoginRequest request);

    ResponseEntity<ValidateTokenResponse> validar(ValidateTokenRequest request);
}
