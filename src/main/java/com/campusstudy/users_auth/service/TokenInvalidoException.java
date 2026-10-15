package com.campusstudy.users_auth.service;

public class TokenInvalidoException extends RuntimeException {

    public TokenInvalidoException() {
        super("Token inválido o expirado");
    }
}
