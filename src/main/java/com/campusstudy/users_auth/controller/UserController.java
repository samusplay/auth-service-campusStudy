package com.campusstudy.users_auth.controller;

import com.campusstudy.users_auth.config.JwtAuthenticationFilter;
import com.campusstudy.users_auth.dto.UserResponse;
import com.campusstudy.users_auth.service.AccesoDenegadoException;
import com.campusstudy.users_auth.service.TokenInvalidoException;
import com.campusstudy.users_auth.service.UserNotFoundException;
import com.campusstudy.users_auth.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/usuarios")
public class UserController {

    static final String ROL_ADMINISTRADOR = "administrador";

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> listar(HttpServletRequest request) {
        if (!ROL_ADMINISTRADOR.equalsIgnoreCase(rol(request))) {
            throw new AccesoDenegadoException();
        }
        List<UserResponse> usuarios = userRepository.findAll().stream()
                .map(UserResponse::from)
                .toList();
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> me(HttpServletRequest request) {
        Long usuarioId = usuarioId(request);
        return userRepository.findById(usuarioId)
                .map(user -> ResponseEntity.ok(UserResponse.from(user)))
                .orElseThrow(UserNotFoundException::new);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable("id") Long usuarioId) {
        return userRepository.findById(usuarioId)
                .map(user -> ResponseEntity.ok(UserResponse.from(user)))
                .orElseThrow(UserNotFoundException::new);
    }

    private Long usuarioId(HttpServletRequest request) {
        Object value = request.getAttribute(JwtAuthenticationFilter.USUARIO_ID);
        if (value instanceof Long id) {
            return id;
        }
        throw new TokenInvalidoException();
    }

    private String rol(HttpServletRequest request) {
        Object value = request.getAttribute(JwtAuthenticationFilter.ROL);
        return value instanceof String texto ? texto : null;
    }
}
