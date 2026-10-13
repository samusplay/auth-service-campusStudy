package com.campusstudy.users_auth.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException() {
        super("El usuario_id solicitado no existe");
    }
}
