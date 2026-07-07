package com.fiap.foodlink_api.integration.interfaces.controller;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("Integracao CRUD de usuarios")
class UserCrudIntegrationTest {

	private static final String CLIENT_USER_TYPE_ID = "22222222-2222-2222-2222-222222222222";

	@Autowired
	private MockMvc mockMvc;

	@Test
	@DisplayName("Deve executar salvar do usuario pela API")
	void deveExecutarSalvarDoUsuarioPelaApi() throws Exception {
		String suffix = randomSuffix();

		MvcResult createResult = mockMvc.perform(post("/api/users")
						.contentType(MediaType.APPLICATION_JSON)
						.content(createPayload(suffix)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").exists())
				.andExpect(jsonPath("$.name").value("Usuario Integracao " + suffix))
				.andExpect(jsonPath("$.email").value("usuario.integracao.%s@foodlink.com".formatted(suffix)))
				.andExpect(jsonPath("$.login").value("usuario.integracao.%s".formatted(suffix)))
				.andExpect(jsonPath("$.userTypeId").value(CLIENT_USER_TYPE_ID))
				.andExpect(jsonPath("$.address.street").value("Rua dos Testes"))
				.andReturn();

		String userId = JsonPath.read(createResult.getResponse().getContentAsString(), "$.id");
		mockMvc.perform(get("/api/users/{id}", UUID.fromString(userId)))
				.andExpect(status().isOk());
	}

	@Test
	@DisplayName("Deve executar buscar usuario por id pela API")
	void deveExecutarBuscarUsuarioPorIdPelaApi() throws Exception {
		String userId = createUser();

		mockMvc.perform(get("/api/users/{id}", UUID.fromString(userId)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(userId))
				.andExpect(jsonPath("$.address.city").value("Sao Paulo"));
	}

	@Test
	@DisplayName("Deve executar listar usuarios pela API")
	void deveExecutarListarUsuariosPelaApi() throws Exception {
		String userId = createUser();

		mockMvc.perform(get("/api/users"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()", greaterThanOrEqualTo(1)))
				.andExpect(jsonPath("$[*].id", hasItem(userId)));
	}

	@Test
	@DisplayName("Deve executar atualizar usuario pela API")
	void deveExecutarAtualizarUsuarioPelaApi() throws Exception {
		String userId = createUser();
		String suffix = randomSuffix();

		mockMvc.perform(put("/api/users/{id}", UUID.fromString(userId))
						.contentType(MediaType.APPLICATION_JSON)
						.content(updatePayload(suffix)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(userId))
				.andExpect(jsonPath("$.name").value("Usuario Integracao Atualizado " + suffix))
				.andExpect(jsonPath("$.email").value("usuario.integracao.atualizado.%s@foodlink.com".formatted(suffix)))
				.andExpect(jsonPath("$.address.street").value("Rua dos Testes Atualizada"))
				.andExpect(jsonPath("$.address.city").value("Campinas"));
	}

	@Test
	@DisplayName("Deve executar deletar usuario pela API")
	void deveExecutarDeletarUsuarioPelaApi() throws Exception {
		String userId = createUser();

		mockMvc.perform(delete("/api/users/{id}", UUID.fromString(userId)))
				.andExpect(status().isNoContent());

		mockMvc.perform(get("/api/users/{id}", UUID.fromString(userId)))
				.andExpect(status().isNotFound());
	}

	private String createUser() throws Exception {
		MvcResult createResult = mockMvc.perform(post("/api/users")
						.contentType(MediaType.APPLICATION_JSON)
						.content(createPayload(randomSuffix())))
				.andExpect(status().isCreated())
				.andReturn();

		return JsonPath.read(createResult.getResponse().getContentAsString(), "$.id");
	}

	private String createPayload(String suffix) {
		return """
				{
				  "name": "Usuario Integracao %s",
				  "email": "usuario.integracao.%s@foodlink.com",
				  "login": "usuario.integracao.%s",
				  "password": "senha123",
				  "userTypeId": "%s",
				  "address": {
				    "street": "Rua dos Testes",
				    "number": "100",
				    "complement": "Apto 10",
				    "district": "Centro",
				    "city": "Sao Paulo",
				    "state": "SP",
				    "zipCode": "01001-000"
				  }
				}
				""".formatted(suffix, suffix, suffix, CLIENT_USER_TYPE_ID);
	}

	private String updatePayload(String suffix) {
		return """
				{
				  "name": "Usuario Integracao Atualizado %s",
				  "email": "usuario.integracao.atualizado.%s@foodlink.com",
				  "login": "usuario.integracao.atualizado.%s",
				  "password": "senha123",
				  "userTypeId": "%s",
				  "address": {
				    "street": "Rua dos Testes Atualizada",
				    "number": "200",
				    "complement": "Casa",
				    "district": "Vila Teste",
				    "city": "Campinas",
				    "state": "SP",
				    "zipCode": "13000-000"
				  }
				}
				""".formatted(suffix, suffix, suffix, CLIENT_USER_TYPE_ID);
	}

	private String randomSuffix() {
		return UUID.randomUUID().toString().substring(0, 8);
	}
}
