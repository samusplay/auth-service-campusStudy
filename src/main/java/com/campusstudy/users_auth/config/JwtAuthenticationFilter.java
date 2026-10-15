package com.campusstudy.users_auth.config;

import com.campusstudy.users_auth.dto.ErrorResponse;
import com.campusstudy.users_auth.service.TokenInvalidoException;
import com.campusstudy.users_auth.model.User;
import com.campusstudy.users_auth.repository.UserRepository;
import com.campusstudy.users_auth.service.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    public static final String USUARIO_ID = "usuarioId";
    public static final String ROL = "rol";

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    public JwtAuthenticationFilter(JwtService jwtService, UserRepository userRepository, ObjectMapper objectMapper) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !ruta(request).startsWith("/api/v1/usuarios");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            escribir(response, HttpStatus.UNAUTHORIZED, "Token inválido o expirado");
            return;
        }
        try {
            Claims claims = jwtService.validarToken(header.substring(7).trim());
            Long usuarioId = jwtService.extraerUsuarioId(claims);
            if (usuarioId == null) {
                escribir(response, HttpStatus.UNAUTHORIZED, "Token inválido o expirado");
                return;
            }
            User user = userRepository.findById(usuarioId).orElse(null);
            if (user == null) {
                escribir(response, HttpStatus.UNAUTHORIZED, "Token inválido o expirado");
                return;
            }
            if (user.getEstado() == null || !user.getEstado().equalsIgnoreCase("activo")) {
                escribir(response, HttpStatus.FORBIDDEN, "El usuario no está activo");
                return;
            }
            request.setAttribute(USUARIO_ID, user.getUsuarioId());
            request.setAttribute(ROL, user.getRol());
            filterChain.doFilter(request, response);
        } catch (TokenInvalidoException ex) {
            escribir(response, HttpStatus.UNAUTHORIZED, ex.getMessage());
        }
    }

    private void escribir(HttpServletResponse response, HttpStatus status, String mensaje) throws IOException {
        response.setStatus(status.value());
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        objectMapper.writeValue(response.getWriter(), ErrorResponse.builder()
                .error(mensaje)
                .errors(List.of())
                .build());
    }

    private String ruta(HttpServletRequest request) {
        String path = request.getRequestURI();
        String context = request.getContextPath();
        if (context != null && !context.isEmpty() && path.startsWith(context)) {
            return path.substring(context.length());
        }
        return path;
    }
}
