package com.campusstudy.users_auth.service.impl;

import com.campusstudy.users_auth.dto.AuthResponse;
import com.campusstudy.users_auth.dto.LoginRequest;
import com.campusstudy.users_auth.dto.RegisterRequest;
import com.campusstudy.users_auth.dto.UserResponse;
import com.campusstudy.users_auth.dto.ValidateTokenResponse;
import com.campusstudy.users_auth.service.CredencialesInvalidasException;
import com.campusstudy.users_auth.service.EmailYaRegistradoException;
import com.campusstudy.users_auth.service.UserInactivoException;
import com.campusstudy.users_auth.model.User;
import com.campusstudy.users_auth.repository.UserRepository;
import com.campusstudy.users_auth.service.AuthService;
import com.campusstudy.users_auth.service.JwtService;
import io.jsonwebtoken.Claims;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(JwtService jwtService, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserResponse registrar(RegisterRequest request) {
        String email = normalizarEmail(request.getEmail());
        if (userRepository.existsByEmail(email)) {
            throw new EmailYaRegistradoException();
        }

        User user = new User();
        user.setNombre(request.getNombre().trim());
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRol(request.getRol().trim().toLowerCase());
        user.setEstado("activo");
        return UserResponse.from(userRepository.save(user));
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        String email = normalizarEmail(request.getEmail());
        User user = userRepository.findByEmail(email)
                .orElseThrow(CredencialesInvalidasException::new);
        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new CredencialesInvalidasException();
        }
        if (user.getEstado() == null || !user.getEstado().equalsIgnoreCase("activo")) {
            throw new UserInactivoException();
        }
        String token = jwtService.generarToken(user.getUsuarioId(), user.getEmail(), user.getRol());
        return new AuthResponse(token, user.getUsuarioId(), user.getEmail(), user.getRol());
    }

    @Override
    public ValidateTokenResponse validarToken(String token) {
        Claims claims = jwtService.validarToken(token);
        return ValidateTokenResponse.builder()
                .valido(true)
                .usuarioId(jwtService.extraerUsuarioId(claims))
                .email(jwtService.extraerEmail(claims))
                .rol(claims.get("rol", String.class))
                .build();
    }

    private String normalizarEmail(String email) {
        return email == null ? null : email.trim().toLowerCase();
    }
}
