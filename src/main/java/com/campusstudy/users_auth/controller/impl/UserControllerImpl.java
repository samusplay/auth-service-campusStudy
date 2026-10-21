package com.campusstudy.users_auth.controller.impl;

import com.campusstudy.users_auth.controller.UserController;
import com.campusstudy.users_auth.dto.UserResponse;
import com.campusstudy.users_auth.dto.UsuarioResumenResponse;
import com.campusstudy.users_auth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserControllerImpl implements UserController {
    //Inyectamos el servicio
    private final UserService userService;
    @Override
    //Devolvemos la responsbailidad al controaldor
    public ResponseEntity<UserResponse> getUserById(Long usuarioId) {
        return  ResponseEntity.ok(userService.getUserBYId(usuarioId));
    }

    @Override
    public ResponseEntity<UsuarioResumenResponse> buscarPorEmail(String email) {
        return ResponseEntity.ok(userService.buscarPorEmail(email));
    }

    @Override
    public ResponseEntity<UserResponse> getMe(Long usuarioId) {
        return  ResponseEntity.ok(userService.getMe(usuarioId));
    }

}
