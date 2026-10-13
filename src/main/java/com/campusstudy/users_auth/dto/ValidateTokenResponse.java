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
public class ValidateTokenResponse {

    private Boolean valido;

    @JsonProperty("usuario_id")
    private Long usuarioId;

    private String email;

    private String rol;
}
