package com.campusstudy.users_auth.service;

import com.campusstudy.users_auth.dto.UserResponse;
import com.campusstudy.users_auth.dto.UsuarioResumenResponse;

public interface UserService {

    //Firma del servicio, tarer datos del usuario
    UserResponse getUserBYId(Long usuarioId);

    //para invitar grupos
    UsuarioResumenResponse buscarPorEmail(String email);

    // GET /api/v1/usuarios/me → los datos del usuario del token
    UserResponse getMe(Long usuarioId);
}
