package com.campusstudy.users_auth.config;

import com.campusstudy.users_auth.model.User;
import com.campusstudy.users_auth.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(UserRepository repository) {
        return args -> {
            if (!repository.existsById(105L)) {
                User testUser = new User();
                testUser.setUsuarioId(105L);
                testUser.setNombre("Estudiante Prueba");
                testUser.setEmail("estudiante@uniminuto.edu");
                testUser.setPasswordHash("$2a$10$e8w6W2E5x2z89x1.1.dummyhash");
                testUser.setEstado("activo");
                repository.save(testUser);
                System.out.println("✅ Usuario de prueba ID: 105 cargado en la base de datos.");
            }
        };
    }
}