package com.campusstudy.users_auth.service.impl;

import com.campusstudy.users_auth.dto.*;
import com.campusstudy.users_auth.exception.EmailAlreadyExistsException;
import com.campusstudy.users_auth.exception.InvalidCredentialsException;
import com.campusstudy.users_auth.mapper.UserMapper;
import com.campusstudy.users_auth.model.User;
import com.campusstudy.users_auth.repository.UserRepository;
import com.campusstudy.users_auth.security.JwtService;
import com.campusstudy.users_auth.service.AuthService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

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

    @Override
    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        // 1. Mismo formato que en el registro
        String email = request.getEmail().trim().toLowerCase();

        // 2. Buscar al usuario; si no existe, error genérico
        User user = userRepository.findByEmail(email)
                .orElseThrow(InvalidCredentialsException::new);

        // 3. Comparar la contraseña escrita con el hash guardado
        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new InvalidCredentialsException();
        }

        // 4. Generar el token y armar la respuesta
        return toAuthResponse(user, jwtService.generarToken(user));
    }

    @Override
    @Transactional(readOnly = true)
    public ValidateTokenResponse validarToken(String token) {
        if (token == null || token.isBlank()) {
            return ValidateTokenResponse.invalido();
        }

        try {
            // 1. Firma y expiración
            Claims claims = jwtService.validar(token.trim());
            Long usuarioId = jwtService.extraerUsuarioId(claims);

            // 2. El usuario debe existir y seguir activo (lo que la firma sola no sabe)
            return userRepository.findById(usuarioId)
                    .filter(user -> "activo".equalsIgnoreCase(user.getEstado()))
                    .map(user -> ValidateTokenResponse.valido(UserMapper.toResponse(user)))
                    .orElseGet(ValidateTokenResponse::invalido);

        } catch (JwtException | IllegalArgumentException ex) {
            // Vencido, firma falsa, mal formado o sub no numérico → "no es válido", no un error
            return ValidateTokenResponse.invalido();
        }
    }

    //metodo Privado
    private AuthResponse toAuthResponse(User user, String token) {
        return AuthResponse.builder()
                .usuarioId(user.getUsuarioId())
                .nombre(user.getNombre())
                .email(user.getEmail())
                .rol(user.getRol())
                .token(token)
                .tipoToken("Bearer")
                .expiraEn(jwtService.getExpiracionSegundos())
                .build();
    }
}