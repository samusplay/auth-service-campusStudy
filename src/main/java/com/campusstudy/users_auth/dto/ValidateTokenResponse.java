package com.campusstudy.users_auth.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

// Si el token no es válido, "usuario" se omite del JSON (no sale "usuario": null)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ValidateTokenResponse(boolean valido, UserResponse usuario) {

    public static ValidateTokenResponse invalido() {
        return new ValidateTokenResponse(false, null);
    }

    public static ValidateTokenResponse valido(UserResponse usuario) {
        return new ValidateTokenResponse(true, usuario);
    }
}