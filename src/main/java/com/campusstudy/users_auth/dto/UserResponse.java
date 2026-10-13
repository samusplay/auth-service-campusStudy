package com.campusstudy.users_auth.dto;

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
}
