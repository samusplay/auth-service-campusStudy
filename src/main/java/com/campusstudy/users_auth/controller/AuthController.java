package com.campusstudy.users_auth.controller;

import com.campusstudy.users_auth.dto.ValidateTokenRequest;
import com.campusstudy.users_auth.dto.ValidateTokenResponse;
import org.springframework.http.ResponseEntity;

public interface AuthController {

    ResponseEntity<ValidateTokenResponse> validar(ValidateTokenRequest request);
}
