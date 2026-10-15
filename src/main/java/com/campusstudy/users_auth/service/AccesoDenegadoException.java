package com.campusstudy.users_auth.service;

public class AccesoDenegadoException extends RuntimeException {

    public AccesoDenegadoException() {
        super("No tienes permiso para esta acción");
    }
}
