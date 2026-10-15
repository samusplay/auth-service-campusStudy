package com.campusstudy.users_auth.service;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException() {
        super("El usuario_id solicitado no existe");
    }
}
