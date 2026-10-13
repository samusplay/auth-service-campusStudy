package com.campusstudy.users_auth.service.impl;

import com.campusstudy.users_auth.dto.ValidateTokenResponse;
import com.campusstudy.users_auth.service.AuthService;
import com.campusstudy.users_auth.service.JwtService;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final JwtService jwtService;

    public AuthServiceImpl(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public ValidateTokenResponse validarToken(String token) {
        Claims claims = jwtService.validarToken(token);
        return ValidateTokenResponse.builder()
                .valido(true)
                .usuarioId(extraerUsuarioId(claims))
                .email(extraerEmail(claims))
                .rol(claims.get("rol", String.class))
                .build();
    }

    private Long extraerUsuarioId(Claims claims) {
        Long desdeClaim = aLong(claims.get("usuario_id"));
        if (desdeClaim != null) {
            return desdeClaim;
        }
        return aLong(claims.getSubject());
    }

    private String extraerEmail(Claims claims) {
        String email = claims.get("email", String.class);
        if (email != null && !email.isBlank()) {
            return email;
        }
        String subject = claims.getSubject();
        if (subject != null && subject.contains("@")) {
            return subject;
        }
        return null;
    }

    private Long aLong(Object value) {
        if (value instanceof Number number) {
            return number.longValue();
        }
        if (value instanceof String texto && !texto.isBlank()) {
            try {
                return Long.parseLong(texto);
            } catch (NumberFormatException ex) {
                return null;
            }
        }
        return null;
    }
}
