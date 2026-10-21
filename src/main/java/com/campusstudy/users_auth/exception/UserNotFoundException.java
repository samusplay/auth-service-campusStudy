package com.campusstudy.users_auth.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException() {
        super("El usuario no existe");
    }
    //nuevo constrcutor
    public UserNotFoundException(String mensaje) {
        super(mensaje);
    }
}