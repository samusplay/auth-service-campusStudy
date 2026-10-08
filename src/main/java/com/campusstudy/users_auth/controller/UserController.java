package com.campusstudy.users_auth.controller;

import com.campusstudy.users_auth.dto.UserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/v1/users")
public interface UserController {
    //Poner los demas endpoints

    @GetMapping("/{id}")
    ResponseEntity<UserResponse> getUserById(@PathVariable("id") Long usuarioId);
}