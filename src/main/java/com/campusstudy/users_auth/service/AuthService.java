package com.campusstudy.users_auth.service;

import com.campusstudy.users_auth.dto.RegistroRequest;
import com.campusstudy.users_auth.dto.UserResponse;

public interface AuthService {
    //Crear un Usuario
    UserResponse registrar(RegistroRequest request);
}