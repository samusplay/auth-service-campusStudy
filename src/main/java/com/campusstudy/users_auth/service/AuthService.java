package com.campusstudy.users_auth.service;

import com.campusstudy.users_auth.dto.ValidateTokenResponse;

public interface AuthService {

    ValidateTokenResponse validarToken(String token);
}
