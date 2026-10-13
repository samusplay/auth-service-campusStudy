package com.campusstudy.users_auth.exception;

import com.campusstudy.users_auth.dto.ErrorDetail;
import com.campusstudy.users_auth.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
                .body(ErrorResponse.builder()
                        .error(ex.getMessage())
                        .errors(List.of())
                        .build());
    }
}
