package com.campusstudy.users_auth.controller.impl;

import com.campusstudy.users_auth.controller.AuthController;
import com.campusstudy.users_auth.dto.AuthResponse;
import com.campusstudy.users_auth.dto.LoginRequest;
import com.campusstudy.users_auth.dto.RegisterRequest;
import com.campusstudy.users_auth.dto.UserResponse;
import com.campusstudy.users_auth.dto.ValidateTokenRequest;
import com.campusstudy.users_auth.dto.ValidateTokenResponse;
import com.campusstudy.users_auth.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthControllerImpl implements AuthController {

    private final AuthService authService;

    public AuthControllerImpl(AuthService authService) {
        this.authService = authService;
    }

    @Override
    @PostMapping("/registro")
    public ResponseEntity<UserResponse> registrar(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.registrar(request));
    }

    @Override
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @Override
    @PostMapping("/validate")
    public ResponseEntity<ValidateTokenResponse> validar(@RequestBody ValidateTokenRequest request) {
        String token = request == null ? null : request.getToken();
        return ResponseEntity.ok(authService.validarToken(token));
    }
}
