package com.campusstudy.users_auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UserResponse(
    @JsonProperty("usuario_id") Long usuarioId,
    String nombre,
    String email,
    String estado
) {}