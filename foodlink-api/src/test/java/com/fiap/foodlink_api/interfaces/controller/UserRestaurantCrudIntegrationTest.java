package com.fiap.foodlink_api.interfaces.controller;

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
@DisplayName("Integracao CRUD de usuarios e restaurantes")
class UserRestaurantCrudIntegrationTest {

	private static final String CLIENT_USER_TYPE_ID = "22222222-2222-2222-2222-222222222222";
	private static final String RESTAURANT_OWNER_ID = "66666666-6666-6666-6666-666666666666";

	@Autowired
	private MockMvc mockMvc;

	@Test
	@DisplayName("Deve executar CRUD de usuario pela API")
	void deveExecutarCrudDeUsuarioPelaApi() throws Exception {
		String createPayload = """
				{
				  "name": "Usuario Integracao",
				  "email": "usuario.integracao@foodlink.com",
				  "login": "usuario.integracao",
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
				""".formatted(CLIENT_USER_TYPE_ID);

		MvcResult createResult = mockMvc.perform(post("/api/users")
						.contentType(MediaType.APPLICATION_JSON)
						.content(createPayload))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").exists())
				.andExpect(jsonPath("$.name").value("Usuario Integracao"))
				.andExpect(jsonPath("$.email").value("usuario.integracao@foodlink.com"))
				.andExpect(jsonPath("$.login").value("usuario.integracao"))
				.andExpect(jsonPath("$.userTypeId").value(CLIENT_USER_TYPE_ID))
				.andExpect(jsonPath("$.address.street").value("Rua dos Testes"))
				.andReturn();

		String userId = JsonPath.read(createResult.getResponse().getContentAsString(), "$.id");

		mockMvc.perform(get("/api/users/{id}", UUID.fromString(userId)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(userId))
				.andExpect(jsonPath("$.address.city").value("Sao Paulo"));

		mockMvc.perform(get("/api/users"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()", greaterThanOrEqualTo(1)))
				.andExpect(jsonPath("$[*].id", hasItem(userId)));

		String updatePayload = """
				{
				  "name": "Usuario Integracao Atualizado",
				  "email": "usuario.integracao.atualizado@foodlink.com",
				  "login": "usuario.integracao.atualizado",
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
				""".formatted(CLIENT_USER_TYPE_ID);

		mockMvc.perform(put("/api/users/{id}", UUID.fromString(userId))
						.contentType(MediaType.APPLICATION_JSON)
						.content(updatePayload))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(userId))
				.andExpect(jsonPath("$.name").value("Usuario Integracao Atualizado"))
				.andExpect(jsonPath("$.email").value("usuario.integracao.atualizado@foodlink.com"))
				.andExpect(jsonPath("$.address.street").value("Rua dos Testes Atualizada"))
				.andExpect(jsonPath("$.address.city").value("Campinas"));

		mockMvc.perform(delete("/api/users/{id}", UUID.fromString(userId)))
				.andExpect(status().isNoContent());

		mockMvc.perform(get("/api/users/{id}", UUID.fromString(userId)))
				.andExpect(status().isNotFound());
	}

	@Test
	@DisplayName("Deve executar CRUD de restaurante pela API")
	void deveExecutarCrudDeRestaurantePelaApi() throws Exception {
		String createPayload = """
				{
				  "cnpj": "98.765.432/0001-01",
				  "name": "Restaurante Integracao",
				  "type": "Italiana",
				  "ownerId": "%s",
				  "address": {
				    "street": "Avenida dos Testes",
				    "number": "500",
				    "complement": "Loja 2",
				    "district": "Centro",
				    "city": "Sao Paulo",
				    "state": "SP",
				    "zipCode": "01002-000"
				  },
				  "period": {
				    "day": ["SEGUNDA", "TERCA"],
				    "openTime": "18:00:00",
				    "closeTime": "23:00:00"
				  }
				}
				""".formatted(RESTAURANT_OWNER_ID);

		MvcResult createResult = mockMvc.perform(post("/restaurants")
						.contentType(MediaType.APPLICATION_JSON)
						.content(createPayload))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").exists())
				.andExpect(jsonPath("$.name").value("Restaurante Integracao"))
				.andExpect(jsonPath("$.cnpj").value("98.765.432/0001-01"))
				.andExpect(jsonPath("$.ownerId").value(RESTAURANT_OWNER_ID))
				.andExpect(jsonPath("$.ownerName").value("Dono Foodlink"))
				.andExpect(jsonPath("$.address.street").value("Avenida dos Testes"))
				.andExpect(jsonPath("$.period.length()").value(2))
				.andReturn();

		String restaurantId = JsonPath.read(createResult.getResponse().getContentAsString(), "$.id");

		mockMvc.perform(get("/restaurants/{id}", UUID.fromString(restaurantId)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(restaurantId))
				.andExpect(jsonPath("$.address.city").value("Sao Paulo"))
				.andExpect(jsonPath("$.period[*].day", hasItem("SEGUNDA")));

		mockMvc.perform(get("/restaurants"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()", greaterThanOrEqualTo(1)))
				.andExpect(jsonPath("$[*].id", hasItem(restaurantId)));

		String updatePayload = """
				{
				  "cnpj": "98.765.432/0001-02",
				  "name": "Restaurante Integracao Atualizado",
				  "type": "Brasileira",
				  "ownerId": "%s",
				  "address": {
				    "street": "Avenida dos Testes",
				    "number": "500",
				    "complement": "Loja 2",
				    "district": "Centro",
				    "city": "Sao Paulo",
				    "state": "SP",
				    "zipCode": "01002-000"
				  },
				  "period": {
				    "day": ["QUARTA"],
				    "openTime": "11:00:00",
				    "closeTime": "15:00:00"
				  }
				}
				""".formatted(RESTAURANT_OWNER_ID);

		mockMvc.perform(put("/restaurants/{id}", UUID.fromString(restaurantId))
						.contentType(MediaType.APPLICATION_JSON)
						.content(updatePayload))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(restaurantId))
				.andExpect(jsonPath("$.name").value("Restaurante Integracao Atualizado"))
				.andExpect(jsonPath("$.cnpj").value("98.765.432/0001-02"))
				.andExpect(jsonPath("$.type").value("Brasileira"))
				.andExpect(jsonPath("$.period.length()").value(1))
				.andExpect(jsonPath("$.period[0].day").value("QUARTA"));

		mockMvc.perform(delete("/restaurants/{id}", UUID.fromString(restaurantId)))
				.andExpect(status().isNoContent());

		mockMvc.perform(get("/restaurants/{id}", UUID.fromString(restaurantId)))
				.andExpect(status().isNotFound());
	}
}
