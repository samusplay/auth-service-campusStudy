package com.campusstudy.users_auth.mapper;

import com.campusstudy.users_auth.dto.UserResponse;
import com.campusstudy.users_auth.model.User;

//Mapeamos los campos en la base de datos hacia un objeto
public final class UserMapper {

    private UserMapper() {
    }

    public static UserResponse toResponse(User user) {
        return UserResponse.builder()
                .usuarioId(user.getUsuarioId())
                .nombre(user.getNombre())
                .email(user.getEmail())
                .rol(user.getRol())
                .estado(user.getEstado())
                .fechaRegistro(user.getCreatedAt())
                .build();
    }
}