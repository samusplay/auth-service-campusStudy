package com.campusstudy.users_auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

    @JsonProperty("usuario_id")
    private Long usuarioId;

    private String nombre;
    private String email;
    private String rol;
    private String token;

    @JsonProperty("tipo_token")
    private String tipoToken;

    @JsonProperty("expira_en")
    private Long expiraEn;
}