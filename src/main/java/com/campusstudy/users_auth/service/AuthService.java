package com.campusstudy.users_auth.service;

import com.campusstudy.users_auth.dto.AuthResponse;
import com.campusstudy.users_auth.dto.LoginRequest;
import com.campusstudy.users_auth.dto.RegisterRequest;
import com.campusstudy.users_auth.dto.UserResponse;
import com.campusstudy.users_auth.dto.ValidateTokenResponse;

public interface AuthService {

    UserResponse registrar(RegisterRequest request);

    AuthResponse login(LoginRequest request);

    ValidateTokenResponse validarToken(String token);
}
