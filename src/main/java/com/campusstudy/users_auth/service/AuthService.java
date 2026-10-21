package com.campusstudy.users_auth.service;

import com.campusstudy.users_auth.dto.*;

public interface AuthService {
    //Crear un Usuario
    UserResponse registrar(RegistroRequest request);

    //firma del servicio de Loggin
    AuthResponse login(LoginRequest request);

    // Responde si un token es válido y de quién es (nunca lanza error por token malo)
    ValidateTokenResponse validarToken(String token);
}