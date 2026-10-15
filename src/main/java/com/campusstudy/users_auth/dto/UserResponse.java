package com.campusstudy.users_auth.dto;

import com.campusstudy.users_auth.model.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    @JsonProperty("usuario_id")
    private Long usuarioId;

    private String nombre;

    private String email;

    private String rol;

    private String estado;

    @JsonProperty("fecha_registro")
    private LocalDate fechaRegistro;

    public static UserResponse from(User user) {
        return UserResponse.builder()
                .usuarioId(user.getUsuarioId())
                .nombre(user.getNombre())
                .email(user.getEmail())
                .rol(user.getRol())
                .estado(user.getEstado())
                .fechaRegistro(user.getCreatedAt() == null ? null : user.getCreatedAt().toLocalDate())
                .build();
    }
}
