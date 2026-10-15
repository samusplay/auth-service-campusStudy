package com.campusstudy.users_auth.service;

public class EmailYaRegistradoException extends RuntimeException {

    public EmailYaRegistradoException() {
        super("El email ya está registrado");
    }
}
