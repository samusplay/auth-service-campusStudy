package com.campusstudy.users_auth.controller;

import com.campusstudy.users_auth.dto.UserResponse;
import com.campusstudy.users_auth.dto.UsuarioResumenResponse;
import com.campusstudy.users_auth.security.JwtAuthFilter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/usuarios")
public interface UserController {
    //Poner los demas endpoints

    @GetMapping("/{id}")
    ResponseEntity<UserResponse> getUserById(@PathVariable("id") Long usuarioId);

    // Buscar un usuario por correo (para invitarlo a un grupo)
    @GetMapping("/buscar")
    ResponseEntity<UsuarioResumenResponse> buscarPorEmail(@RequestParam("email") String email);

    //Mis datos
    @GetMapping("/me")
    ResponseEntity<UserResponse> getMe(@RequestAttribute(JwtAuthFilter.USUARIO_ID) Long usuarioId);
}