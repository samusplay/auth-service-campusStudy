package com.campusstudy.users_auth.controller;

import com.campusstudy.users_auth.dto.RegistroRequest;
import com.campusstudy.users_auth.dto.UserResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/v1/auth")
public interface AuthController {

    @PostMapping("/registro")
    ResponseEntity<UserResponse> registrar(@Valid @RequestBody RegistroRequest request);
}