package com.campusstudy.users_auth.service;

public class UserInactivoException extends RuntimeException {

    public UserInactivoException() {
        super("El usuario no está activo");
    }
}
