package com.campusstudy.users_auth.config;

import com.campusstudy.users_auth.model.User;
import com.campusstudy.users_auth.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(UserRepository repository, PasswordEncoder passwordEncoder) {
        return args -> {
            crearSiNoExiste(repository, passwordEncoder, "estudiante@uniminuto.edu",
                    "Estudiante Prueba", "Estudiante123", "estudiante");
            crearSiNoExiste(repository, passwordEncoder, "docente@uniminuto.edu",
                    "Docente Prueba", "Docente12345", "docente");
            crearSiNoExiste(repository, passwordEncoder, "admin@uniminuto.edu",
                    "Administrador", "Admin12345", "administrador");
        };
    }

    private void crearSiNoExiste(
            UserRepository repository,
            PasswordEncoder passwordEncoder,
            String email,
            String nombre,
            String password,
            String rol) {
        if (repository.existsByEmail(email)) {
            return;
        }
        User user = new User();
        user.setNombre(nombre);
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(password));
        user.setRol(rol);
        user.setEstado("activo");
        repository.save(user);
    }
}
