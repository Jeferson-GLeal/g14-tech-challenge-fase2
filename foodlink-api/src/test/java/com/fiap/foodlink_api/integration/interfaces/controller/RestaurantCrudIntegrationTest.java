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
@DisplayName("Integracao CRUD de restaurantes")
class RestaurantCrudIntegrationTest {

	private static final String RESTAURANT_OWNER_ID = "66666666-6666-6666-6666-666666666666";

	@Autowired
	private MockMvc mockMvc;

	@Test
	@DisplayName("Deve executar salvar do restaurante pela API")
	void deveExecutarSalvarDoRestaurantePelaApi() throws Exception {
		String suffix = randomSuffix();
		String cnpj = cnpjFromSuffix(suffix);

		MvcResult createResult = mockMvc.perform(post("/restaurants")
						.contentType(MediaType.APPLICATION_JSON)
						.content(createPayload(suffix, cnpj)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").exists())
				.andExpect(jsonPath("$.name").value("Restaurante Integracao " + suffix))
				.andExpect(jsonPath("$.cnpj").value(cnpj))
				.andExpect(jsonPath("$.ownerId").value(RESTAURANT_OWNER_ID))
				.andExpect(jsonPath("$.ownerName").value("Dono Foodlink"))
				.andExpect(jsonPath("$.address.street").value("Avenida dos Testes"))
				.andExpect(jsonPath("$.period.length()").value(2))
				.andReturn();

		String restaurantId = JsonPath.read(createResult.getResponse().getContentAsString(), "$.id");
		mockMvc.perform(get("/restaurants/{id}", UUID.fromString(restaurantId)))
				.andExpect(status().isOk());
	}

	@Test
	@DisplayName("Deve executar buscar restaurante por id pela API")
	void deveExecutarBuscarRestaurantePorIdPelaApi() throws Exception {
		String restaurantId = createRestaurant();

		mockMvc.perform(get("/restaurants/{id}", UUID.fromString(restaurantId)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(restaurantId))
				.andExpect(jsonPath("$.address.city").value("Sao Paulo"))
				.andExpect(jsonPath("$.period[*].day", hasItem("SEGUNDA")));
	}

	@Test
	@DisplayName("Deve executar listar restaurantes pela API")
	void deveExecutarListarRestaurantesPelaApi() throws Exception {
		String restaurantId = createRestaurant();

		mockMvc.perform(get("/restaurants"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()", greaterThanOrEqualTo(1)))
				.andExpect(jsonPath("$[*].id", hasItem(restaurantId)));
	}

	@Test
	@DisplayName("Deve executar atualizar restaurante pela API")
	void deveExecutarAtualizarRestaurantePelaApi() throws Exception {
		String restaurantId = createRestaurant();
		String suffix = randomSuffix();
		String cnpj = cnpjFromSuffix(suffix);

		mockMvc.perform(put("/restaurants/{id}", UUID.fromString(restaurantId))
						.contentType(MediaType.APPLICATION_JSON)
						.content(updatePayload(suffix, cnpj)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(restaurantId))
				.andExpect(jsonPath("$.name").value("Restaurante Integracao Atualizado " + suffix))
				.andExpect(jsonPath("$.cnpj").value(cnpj))
				.andExpect(jsonPath("$.type").value("Brasileira"))
				.andExpect(jsonPath("$.period.length()").value(1))
				.andExpect(jsonPath("$.period[0].day").value("QUARTA"));
	}

	@Test
	@DisplayName("Deve executar deletar restaurante pela API")
	void deveExecutarDeletarRestaurantePelaApi() throws Exception {
		String restaurantId = createRestaurant();

		mockMvc.perform(delete("/restaurants/{id}", UUID.fromString(restaurantId)))
				.andExpect(status().isNoContent());

		mockMvc.perform(get("/restaurants/{id}", UUID.fromString(restaurantId)))
				.andExpect(status().isNotFound());
	}

	private String createRestaurant() throws Exception {
		String suffix = randomSuffix();
		MvcResult createResult = mockMvc.perform(post("/restaurants")
						.contentType(MediaType.APPLICATION_JSON)
						.content(createPayload(suffix, cnpjFromSuffix(suffix))))
				.andExpect(status().isCreated())
				.andReturn();

		return JsonPath.read(createResult.getResponse().getContentAsString(), "$.id");
	}

	private String createPayload(String suffix, String cnpj) {
		return """
				{
				  "cnpj": "%s",
				  "name": "Restaurante Integracao %s",
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
				""".formatted(cnpj, suffix, RESTAURANT_OWNER_ID);
	}

	private String updatePayload(String suffix, String cnpj) {
		return """
				{
				  "cnpj": "%s",
				  "name": "Restaurante Integracao Atualizado %s",
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
				""".formatted(cnpj, suffix, RESTAURANT_OWNER_ID);
	}

	private String cnpjFromSuffix(String suffix) {
		String digits = suffix.chars()
				.mapToObj(Integer::toHexString)
				.reduce("", String::concat)
				.replaceAll("\\D", "");

		String paddedDigits = (digits + "00000000000000").substring(0, 14);
		return "%s.%s.%s/%s-%s".formatted(
				paddedDigits.substring(0, 2),
				paddedDigits.substring(2, 5),
				paddedDigits.substring(5, 8),
				paddedDigits.substring(8, 12),
				paddedDigits.substring(12, 14)
		);
	}

	private String randomSuffix() {
		return UUID.randomUUID().toString().substring(0, 8);
	}
}
