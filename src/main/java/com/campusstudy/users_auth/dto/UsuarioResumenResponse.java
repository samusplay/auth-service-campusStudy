package com.campusstudy.users_auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

// Lo mínimo para identificar a un usuario sin exponer rol, estado ni fechas
public record UsuarioResumenResponse(
        @JsonProperty("usuario_id") Long usuarioId,
        String nombre,
        String email
) {}