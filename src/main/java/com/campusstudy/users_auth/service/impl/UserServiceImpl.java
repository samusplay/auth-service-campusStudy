package com.campusstudy.users_auth.service.impl;

import com.campusstudy.users_auth.dto.UserResponse;
import com.campusstudy.users_auth.repository.UserRepository;
import com.campusstudy.users_auth.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    //Inyectamos el Repository
    //Usarlo para llamar a los metodos
    private final UserRepository userRepository;

    @Override
    public UserResponse getUserBYId(Long usuarioId) {
        //Poner la Logica de Negocio aqui
        return null;
    }
}
