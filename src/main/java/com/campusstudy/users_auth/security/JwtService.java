package com.campusstudy.users_auth.security;

import com.campusstudy.users_auth.model.User;
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

    public JwtService(@Value("${app.jwt.secret}") String secret,
                      @Value("${app.jwt.expiration}") long expirationMs) {
        // Bytes del texto tal cual: así el gateway (PyJWT) valida con el mismo secreto
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationMs = expirationMs;
    }

    public String generarToken(User user) {
        Date ahora = new Date();

        return Jwts.builder()
                .subject(String.valueOf(user.getUsuarioId()))
                .claim("email", user.getEmail())
                .claim("rol", user.getRol())
                .issuedAt(ahora)
                .expiration(new Date(ahora.getTime() + expirationMs))
                // HS256 explícito: es el algoritmo que espera el gateway
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }

    public long getExpiracionSegundos() {
        return expirationMs / 1000;
    }
}