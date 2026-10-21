package com.campusstudy.users_auth.security;

import com.campusstudy.users_auth.model.User;
import com.campusstudy.users_auth.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * Protege /api/v1/usuarios/**: exige un JWT válido de un usuario que exista y esté activo.
 * /api/v1/auth/** (login, registro) queda público.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    // Nombres con los que el controller lee al usuario autenticado (@RequestAttribute)
    public static final String USUARIO_ID = "usuarioId";
    public static final String ROL = "rol";

    private static final String RUTA_PROTEGIDA = "/api/v1/usuarios";

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        // Solo se ejecuta en /api/v1/usuarios/**
        return !request.getServletPath().startsWith(RUTA_PROTEGIDA);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // 1. ¿Viene el header "Authorization: Bearer <token>"?
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            responderError(response, HttpStatus.UNAUTHORIZED, "No autorizado");
            return;
        }

        // 2. Firma, expiración y sub numérico
        Long usuarioId;
        try {
            Claims claims = jwtService.validar(header.substring(7).trim());
            usuarioId = jwtService.extraerUsuarioId(claims);
        } catch (JwtException | IllegalArgumentException ex) {
            // IllegalArgumentException incluye NumberFormatException (sub que no es número)
            responderError(response, HttpStatus.UNAUTHORIZED, "Token inválido o expirado");
            return;
        }

        // 3. El usuario del token debe seguir existiendo…
        User user = userRepository.findById(usuarioId).orElse(null);
        if (user == null) {
            responderError(response, HttpStatus.UNAUTHORIZED, "Token inválido o expirado");
            return;
        }

        // 4. …y estar activo (la firma no sabe si lo desactivaron después del login)
        if (!"activo".equalsIgnoreCase(user.getEstado())) {
            responderError(response, HttpStatus.FORBIDDEN, "El usuario no está activo");
            return;
        }

        // 5. Dejar al usuario disponible para el controller y seguir
        request.setAttribute(USUARIO_ID, user.getUsuarioId());
        request.setAttribute(ROL, user.getRol());
        filterChain.doFilter(request, response);
    }

    // Mismo formato que tu GlobalExceptionHandler: { "error": "..." }
    private void responderError(HttpServletResponse response, HttpStatus status, String mensaje) throws IOException {
        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        objectMapper.writeValue(response.getWriter(), Map.of("error", mensaje));
    }
}