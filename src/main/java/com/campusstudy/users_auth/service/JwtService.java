package com.campusstudy.users_auth.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

    private final SecretKey key;
    private final long expirationMs;

    public JwtService(
            @Value("${app.jwt.secret}") String secret,
            @Value("${app.jwt.expiration}") long expirationMs) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationMs = expirationMs;
    }

    public String generarToken(Long usuarioId, String email, String rol) {
        Date ahora = new Date();
        return Jwts.builder()
                .subject(email)
                .claim("usuario_id", usuarioId)
                .claim("email", email)
                .claim("rol", rol)
                .issuedAt(ahora)
                .expiration(new Date(ahora.getTime() + expirationMs))
                .signWith(key)
                .compact();
    }

    public Long extraerUsuarioId(Claims claims) {
        Long desdeClaim = aLong(claims.get("usuario_id"));
        if (desdeClaim != null) {
            return desdeClaim;
        }
        return aLong(claims.getSubject());
    }

    public String extraerEmail(Claims claims) {
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

    public Claims validarToken(String token) {
        if (token == null || token.isBlank()) {
            throw new TokenInvalidoException();
        }
        try {
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (JwtException | IllegalArgumentException ex) {
            throw new TokenInvalidoException();
        }
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
