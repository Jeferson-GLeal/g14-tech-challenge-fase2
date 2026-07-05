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
@DisplayName("Integracao CRUD de tipos de usuario")
class UserTypeCrudIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	@DisplayName("Deve executar salvar tipo de usuario pela API")
	void deveExecutarSalvarTipoUsuarioPelaApi() throws Exception {
		String suffix = randomSuffix();

		MvcResult createResult = mockMvc.perform(post("/api/user-types")
						.contentType(MediaType.APPLICATION_JSON)
						.content(createPayload(suffix)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").exists())
				.andExpect(jsonPath("$.name").value("Tipo Integracao " + suffix))
				.andExpect(jsonPath("$.code").value("CLIENTE"))
				.andReturn();

		String userTypeId = JsonPath.read(createResult.getResponse().getContentAsString(), "$.id");
		mockMvc.perform(get("/api/user-types/{id}", UUID.fromString(userTypeId)))
				.andExpect(status().isOk());
	}

	@Test
	@DisplayName("Deve executar buscar tipo de usuario por id pela API")
	void deveExecutarBuscarTipoUsuarioPorIdPelaApi() throws Exception {
		String userTypeId = createUserType();

		mockMvc.perform(get("/api/user-types/{id}", UUID.fromString(userTypeId)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(userTypeId))
				.andExpect(jsonPath("$.code").value("CLIENTE"));
	}

	@Test
	@DisplayName("Deve executar listar tipos de usuario pela API")
	void deveExecutarListarTiposUsuarioPelaApi() throws Exception {
		String userTypeId = createUserType();

		mockMvc.perform(get("/api/user-types"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()", greaterThanOrEqualTo(1)))
				.andExpect(jsonPath("$[*].id", hasItem(userTypeId)));
	}

	@Test
	@DisplayName("Deve executar atualizar tipo de usuario pela API")
	void deveExecutarAtualizarTipoUsuarioPelaApi() throws Exception {
		String userTypeId = createUserType();
		String suffix = randomSuffix();

		mockMvc.perform(put("/api/user-types/{id}", UUID.fromString(userTypeId))
						.contentType(MediaType.APPLICATION_JSON)
						.content(updatePayload(suffix)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(userTypeId))
				.andExpect(jsonPath("$.name").value("Tipo Integracao Atualizado " + suffix))
				.andExpect(jsonPath("$.code").value("CLIENTE"));
	}

	@Test
	@DisplayName("Deve executar deletar tipo de usuario pela API")
	void deveExecutarDeletarTipoUsuarioPelaApi() throws Exception {
		String userTypeId = createUserType();

		mockMvc.perform(delete("/api/user-types/{id}", UUID.fromString(userTypeId)))
				.andExpect(status().isNoContent());

		mockMvc.perform(get("/api/user-types/{id}", UUID.fromString(userTypeId)))
				.andExpect(status().isNotFound());
	}

	private String createUserType() throws Exception {
		MvcResult createResult = mockMvc.perform(post("/api/user-types")
						.contentType(MediaType.APPLICATION_JSON)
						.content(createPayload(randomSuffix())))
				.andExpect(status().isCreated())
				.andReturn();

		return JsonPath.read(createResult.getResponse().getContentAsString(), "$.id");
	}

	private String createPayload(String suffix) {
		return """
				{
				  "name": "Tipo Integracao %s",
				  "code": "CLIENTE"
				}
				""".formatted(suffix);
	}

	private String updatePayload(String suffix) {
		return """
				{
				  "name": "Tipo Integracao Atualizado %s",
				  "code": "CLIENTE"
				}
				""".formatted(suffix);
	}

	private String randomSuffix() {
		return UUID.randomUUID().toString().substring(0, 8);
	}
}
