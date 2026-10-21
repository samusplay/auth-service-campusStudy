package com.campusstudy.users_auth.controller.impl;

import com.campusstudy.users_auth.controller.AuthController;
import com.campusstudy.users_auth.dto.*;
import com.campusstudy.users_auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthControllerImpl implements AuthController {

    private final AuthService authService;

    @Override
    public ResponseEntity<UserResponse> registrar(RegistroRequest request) {
        UserResponse usuarioCreado = authService.registrar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioCreado);
    }

    @Override
    public ResponseEntity<AuthResponse> login(LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @Override
    public ResponseEntity<ValidateTokenResponse> validar(ValidateTokenRequest request) {
        return ResponseEntity.ok(authService.validarToken(request.token()));
    }
}