package com.campusstudy.users_auth.controller.impl;

import com.campusstudy.users_auth.controller.AuthController;
import com.campusstudy.users_auth.dto.ValidateTokenRequest;
import com.campusstudy.users_auth.dto.ValidateTokenResponse;
import com.campusstudy.users_auth.service.AuthService;
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
    @PostMapping("/validate")
    public ResponseEntity<ValidateTokenResponse> validar(@RequestBody ValidateTokenRequest request) {
        String token = request == null ? null : request.getToken();
        return ResponseEntity.ok(authService.validarToken(token));
    }
}
