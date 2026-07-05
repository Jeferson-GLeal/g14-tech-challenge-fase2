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
@DisplayName("Integracao CRUD de itens do cardapio")
class MenuItemCrudIntegrationTest {

	private static final String RESTAURANT_ID = "dddddddd-dddd-dddd-dddd-ddddddddddd1";

	@Autowired
	private MockMvc mockMvc;

	@Test
	@DisplayName("Deve executar salvar item do cardapio pela API")
	void deveExecutarSalvarItemCardapioPelaApi() throws Exception {
		String suffix = randomSuffix();

		MvcResult createResult = mockMvc.perform(post("/api/menu-items/{restaurantId}", UUID.fromString(RESTAURANT_ID))
						.contentType(MediaType.APPLICATION_JSON)
						.content(createPayload(suffix)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").exists())
				.andExpect(jsonPath("$.name").value("Item Integracao " + suffix))
				.andExpect(jsonPath("$.restaurantId").value(RESTAURANT_ID))
				.andExpect(jsonPath("$.price").value(29.9))
				.andReturn();

		String menuItemId = JsonPath.read(createResult.getResponse().getContentAsString(), "$.id");
		mockMvc.perform(get("/api/menu-items/{restaurantId}/{id}", UUID.fromString(RESTAURANT_ID), UUID.fromString(menuItemId)))
				.andExpect(status().isOk());
	}

	@Test
	@DisplayName("Deve executar buscar item do cardapio por id pela API")
	void deveExecutarBuscarItemCardapioPorIdPelaApi() throws Exception {
		String menuItemId = createMenuItem();

		mockMvc.perform(get("/api/menu-items/{restaurantId}/{id}", UUID.fromString(RESTAURANT_ID), UUID.fromString(menuItemId)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(menuItemId))
				.andExpect(jsonPath("$.restaurantId").value(RESTAURANT_ID));
	}

	@Test
	@DisplayName("Deve executar listar itens do cardapio pela API")
	void deveExecutarListarItensCardapioPelaApi() throws Exception {
		String menuItemId = createMenuItem();

		mockMvc.perform(get("/api/menu-items/{restaurantId}", UUID.fromString(RESTAURANT_ID)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()", greaterThanOrEqualTo(1)))
				.andExpect(jsonPath("$[*].id", hasItem(menuItemId)));
	}

	@Test
	@DisplayName("Deve executar atualizar item do cardapio pela API")
	void deveExecutarAtualizarItemCardapioPelaApi() throws Exception {
		String menuItemId = createMenuItem();
		String suffix = randomSuffix();

		mockMvc.perform(put("/api/menu-items/{restaurantId}/{id}", UUID.fromString(RESTAURANT_ID), UUID.fromString(menuItemId))
						.contentType(MediaType.APPLICATION_JSON)
						.content(updatePayload(suffix)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(menuItemId))
				.andExpect(jsonPath("$.name").value("Item Integracao Atualizado " + suffix))
				.andExpect(jsonPath("$.description").value("Item atualizado pela integracao."))
				.andExpect(jsonPath("$.price").value(39.9))
				.andExpect(jsonPath("$.availableOnlyAtRestaurant").value(true));
	}

	@Test
	@DisplayName("Deve executar deletar item do cardapio pela API")
	void deveExecutarDeletarItemCardapioPelaApi() throws Exception {
		String menuItemId = createMenuItem();

		mockMvc.perform(delete("/api/menu-items/{restaurantId}/{id}", UUID.fromString(RESTAURANT_ID), UUID.fromString(menuItemId)))
				.andExpect(status().isNoContent());

		mockMvc.perform(get("/api/menu-items/{restaurantId}/{id}", UUID.fromString(RESTAURANT_ID), UUID.fromString(menuItemId)))
				.andExpect(status().isNotFound());
	}

	private String createMenuItem() throws Exception {
		MvcResult createResult = mockMvc.perform(post("/api/menu-items/{restaurantId}", UUID.fromString(RESTAURANT_ID))
						.contentType(MediaType.APPLICATION_JSON)
						.content(createPayload(randomSuffix())))
				.andExpect(status().isCreated())
				.andReturn();

		return JsonPath.read(createResult.getResponse().getContentAsString(), "$.id");
	}

	private String createPayload(String suffix) {
		return """
				{
				  "name": "Item Integracao %s",
				  "description": "Item criado pela integracao.",
				  "price": 29.90,
				  "photoPath": "fotos/item-integracao.png",
				  "availableOnlyAtRestaurant": false
				}
				""".formatted(suffix);
	}

	private String updatePayload(String suffix) {
		return """
				{
				  "name": "Item Integracao Atualizado %s",
				  "description": "Item atualizado pela integracao.",
				  "price": 39.90,
				  "photoPath": "fotos/item-integracao-atualizado.png",
				  "availableOnlyAtRestaurant": true
				}
				""".formatted(suffix);
	}

	private String randomSuffix() {
		return UUID.randomUUID().toString().substring(0, 8);
	}
}
