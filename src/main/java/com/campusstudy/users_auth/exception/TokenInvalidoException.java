package com.campusstudy.users_auth.exception;

public class TokenInvalidoException extends RuntimeException {

    public TokenInvalidoException() {
        super("Token inválido o expirado");
    }
}
