package com.campusstudy.users_auth.controller;

import com.campusstudy.users_auth.dto.UserResponse;
import com.campusstudy.users_auth.exception.UserNotFoundException;
import com.campusstudy.users_auth.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/usuarios")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable("id") Long usuarioId) {
        return userRepository.findById(usuarioId)
                .map(user -> ResponseEntity.ok(UserResponse.builder()
                        .usuarioId(user.getUsuarioId())
                        .nombre(user.getNombre())
                        .email(user.getEmail())
                        .rol(user.getRol())
                        .estado(user.getEstado())
                        .fechaRegistro(user.getCreatedAt() == null ? null : user.getCreatedAt().toLocalDate())
                        .build()))
                .orElseThrow(() -> new UserNotFoundException());
    }
}