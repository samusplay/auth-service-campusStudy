package com.campusstudy.users_auth;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = "spring.datasource.url=jdbc:postgresql://localhost:5433/campusstudy_users_test")
@AutoConfigureMockMvc
class UsersAuthApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void contextLoads() {
	}

	@Test
	void registroLoginYAutorizacion() throws Exception {
		mockMvc.perform(post("/api/v1/auth/registro")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{"nombre":"Ana Perez","email":"ana@uniminuto.edu","password":"clave1234","rol":"estudiante"}
								"""))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.email").value("ana@uniminuto.edu"))
				.andExpect(jsonPath("$.rol").value("estudiante"))
				.andExpect(jsonPath("$.estado").value("activo"));

		mockMvc.perform(post("/api/v1/auth/registro")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{"nombre":"Ana Perez","email":"ana@uniminuto.edu","password":"clave1234","rol":"estudiante"}
								"""))
				.andExpect(status().isConflict());

		mockMvc.perform(post("/api/v1/auth/registro")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{"nombre":"Luis Gomez","email":"luis@uniminuto.edu","password":"clave1234","rol":"docente"}
								"""))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.rol").value("docente"));

		mockMvc.perform(post("/api/v1/auth/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{"email":"ana@uniminuto.edu","password":"incorrecta"}
								"""))
				.andExpect(status().isUnauthorized());

		MvcResult login = mockMvc.perform(post("/api/v1/auth/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{"email":"ana@uniminuto.edu","password":"clave1234"}
								"""))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.token").isNotEmpty())
				.andExpect(jsonPath("$.rol").value("estudiante"))
				.andReturn();

		String token = JsonPath.read(login.getResponse().getContentAsString(), "$.token");

		mockMvc.perform(post("/api/v1/auth/validate")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"token\":\"" + token + "\"}"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.valido").value(true))
				.andExpect(jsonPath("$.email").value("ana@uniminuto.edu"))
				.andExpect(jsonPath("$.rol").value("estudiante"));

		mockMvc.perform(get("/api/v1/usuarios/me"))
				.andExpect(status().isUnauthorized());

		mockMvc.perform(get("/api/v1/usuarios/me")
						.header("Authorization", "Bearer " + token))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.email").value("ana@uniminuto.edu"));

		mockMvc.perform(get("/api/v1/usuarios")
						.header("Authorization", "Bearer " + token))
				.andExpect(status().isForbidden());

		MvcResult adminLogin = mockMvc.perform(post("/api/v1/auth/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{"email":"admin@uniminuto.edu","password":"Admin12345"}
								"""))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.rol").value("administrador"))
				.andReturn();

		String adminToken = JsonPath.read(adminLogin.getResponse().getContentAsString(), "$.token");

		mockMvc.perform(get("/api/v1/usuarios")
						.header("Authorization", "Bearer " + adminToken))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].email").exists());
	}
}
