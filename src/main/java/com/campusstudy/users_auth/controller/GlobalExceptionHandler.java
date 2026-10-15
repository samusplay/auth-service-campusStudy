package com.campusstudy.users_auth.controller;

import com.campusstudy.users_auth.dto.ErrorDetail;
import com.campusstudy.users_auth.dto.ErrorResponse;
import com.campusstudy.users_auth.service.AccesoDenegadoException;
import com.campusstudy.users_auth.service.CredencialesInvalidasException;
import com.campusstudy.users_auth.service.EmailYaRegistradoException;
import com.campusstudy.users_auth.service.TokenInvalidoException;
import com.campusstudy.users_auth.service.UserNotFoundException;
import com.campusstudy.users_auth.service.UserInactivoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.builder()
                        .error(ex.getMessage())
                        .errors(List.of(ErrorDetail.builder()
                                .campo("usuario_id")
                                .mensaje(ex.getMessage())
                                .build()))
                        .build());
    }

    @ExceptionHandler(TokenInvalidoException.class)
    public ResponseEntity<ErrorResponse> handleTokenInvalido(TokenInvalidoException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(error(ex.getMessage()));
    }

    @ExceptionHandler(CredencialesInvalidasException.class)
    public ResponseEntity<ErrorResponse> handleCredenciales(CredencialesInvalidasException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(error(ex.getMessage()));
    }

    @ExceptionHandler(EmailYaRegistradoException.class)
    public ResponseEntity<ErrorResponse> handleEmailDuplicado(EmailYaRegistradoException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ErrorResponse.builder()
                        .error(ex.getMessage())
                        .errors(List.of(ErrorDetail.builder()
                                .campo("email")
                                .mensaje(ex.getMessage())
                                .build()))
                        .build());
    }

    @ExceptionHandler(UserInactivoException.class)
    public ResponseEntity<ErrorResponse> handleInactivo(UserInactivoException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(error(ex.getMessage()));
    }

    @ExceptionHandler(AccesoDenegadoException.class)
    public ResponseEntity<ErrorResponse> handleAccesoDenegado(AccesoDenegadoException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(error(ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidacion(MethodArgumentNotValidException ex) {
        List<ErrorDetail> details = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> ErrorDetail.builder()
                        .campo(err.getField())
                        .mensaje(err.getDefaultMessage())
                        .build())
                .toList();
        return ResponseEntity.badRequest()
                .body(ErrorResponse.builder()
                        .error("Datos inválidos")
                        .errors(details)
                        .build());
    }

    private ErrorResponse error(String mensaje) {
        return ErrorResponse.builder()
                .error(mensaje)
                .errors(List.of())
                .build();
    }
}
