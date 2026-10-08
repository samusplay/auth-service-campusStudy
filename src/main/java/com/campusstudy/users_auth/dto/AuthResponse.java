package com.campusstudy.users_auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AuthResponse(
    String token,
    @JsonProperty("usuario_id") Long usuarioId,
    String email
) {}