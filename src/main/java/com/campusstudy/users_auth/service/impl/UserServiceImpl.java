package com.campusstudy.users_auth.service.impl;

import com.campusstudy.users_auth.dto.UserResponse;
import com.campusstudy.users_auth.dto.UsuarioResumenResponse;
import com.campusstudy.users_auth.exception.UserNotFoundException;
import com.campusstudy.users_auth.mapper.UserMapper;
import com.campusstudy.users_auth.repository.UserRepository;
import com.campusstudy.users_auth.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    //Inyectamos el Repository
    //Usarlo para llamar a los metodos
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserBYId(Long usuarioId) {
        // 1. Buscar; si no existe → 404
        // 2. Devolver sin la contraseña (el mapper no copia passwordHash)
        return userRepository.findById(usuarioId)
                .map(UserMapper::toResponse)
                .orElseThrow(UserNotFoundException::new);
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioResumenResponse buscarPorEmail(String email) {
        String normalizado = email.trim().toLowerCase();

        return userRepository.findByEmail(normalizado)
                .filter(user -> "activo".equalsIgnoreCase(user.getEstado()))
                .map(UserMapper::toResumen)
                .orElseThrow(() -> new UserNotFoundException("Ese correo no tiene una cuenta activa en CampusStudy"));
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getMe(Long usuarioId) {
        // El filtro ya comprobó que existe y está activo; reutilizamos la búsqueda por id
        return getUserBYId(usuarioId);
    }
}
