package com.campusstudy.users_auth.service.impl;

import com.campusstudy.users_auth.dto.RegistroRequest;
import com.campusstudy.users_auth.dto.UserResponse;
import com.campusstudy.users_auth.exception.EmailAlreadyExistsException;
import com.campusstudy.users_auth.mapper.UserMapper;
import com.campusstudy.users_auth.model.User;
import com.campusstudy.users_auth.repository.UserRepository;
import com.campusstudy.users_auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserResponse registrar(RegistroRequest request) {
        // 1. Normalizar el email: "Ana@Mail.com" y "ana@mail.com" son la misma cuenta
        String email = request.getEmail().trim().toLowerCase();

        // 2. No permitir correos repetidos
        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException(email);
        }

        // 3. Construir el usuario con la contraseña encriptada
        User user = User.builder()
                .nombre(request.getNombre().trim())
                .email(email)
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .build();

        // 4. Guardar y devolver sin la contraseña
        return UserMapper.toResponse(userRepository.save(user));
    }
}