package com.campusstudy.users_auth.service;

import com.campusstudy.users_auth.dto.UserResponse;

public interface UserService {
    //Firma del servicio
    UserResponse getUserBYId(Long usuarioId);
}
