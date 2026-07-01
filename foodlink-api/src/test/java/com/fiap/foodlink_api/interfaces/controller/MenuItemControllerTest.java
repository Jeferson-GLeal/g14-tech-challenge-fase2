package com.fiap.foodlink_api.interfaces.controller;

import com.fiap.foodlink_api.application.usecase.menuitem.CreateMenuItemUseCase;
import com.fiap.foodlink_api.application.usecase.menuitem.DeleteMenuItemUseCase;
import com.fiap.foodlink_api.application.usecase.menuitem.GetMenuItemByIdUseCase;
import com.fiap.foodlink_api.application.usecase.menuitem.ListMenuItemsByRestaurantUseCase;
import com.fiap.foodlink_api.application.usecase.menuitem.UpdateMenuItemUseCase;
import com.fiap.foodlink_api.domain.entity.MenuItem;
import com.fiap.foodlink_api.interfaces.controller.dto.MenuItemRequest;
import com.fiap.foodlink_api.interfaces.controller.dto.MenuItemResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Interface de item do cardapio")
class MenuItemControllerTest {

	@Test
	@DisplayName("Deve criar item do cardapio para restaurante")
	void deveCriarItemDoCardapioParaRestaurante() {
		CreateMenuItemUseCase createMenuItemUseCase = mock(CreateMenuItemUseCase.class);
		MenuItemController controller = criarController(createMenuItemUseCase);
		UUID id = UUID.randomUUID();
		UUID restaurantId = UUID.randomUUID();
		MenuItemRequest request = criarRequest();
		when(createMenuItemUseCase.execute(
				"Pizza Margherita",
				"Pizza com molho de tomate",
				new BigDecimal("49.90"),
				restaurantId,
				"fotos/pizza.png",
				false
		)).thenReturn(criarMenuItem(id, restaurantId));

		ResponseEntity<MenuItemResponse> response = controller.create(restaurantId, request);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(id, response.getBody().id());
		assertEquals(restaurantId, response.getBody().restaurantId());
		verify(createMenuItemUseCase).execute(
				"Pizza Margherita",
				"Pizza com molho de tomate",
				new BigDecimal("49.90"),
				restaurantId,
				"fotos/pizza.png",
				false
		);
	}

	@Test
	@DisplayName("Deve buscar item do cardapio por restaurante e identificador")
	void deveBuscarItemDoCardapioPorRestauranteEIdentificador() {
		GetMenuItemByIdUseCase getMenuItemByIdUseCase = mock(GetMenuItemByIdUseCase.class);
		MenuItemController controller = criarController(getMenuItemByIdUseCase);
		UUID id = UUID.randomUUID();
		UUID restaurantId = UUID.randomUUID();
		when(getMenuItemByIdUseCase.execute(restaurantId, id)).thenReturn(criarMenuItem(id, restaurantId));

		ResponseEntity<MenuItemResponse> response = controller.findById(restaurantId, id);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(id, response.getBody().id());
		assertEquals(restaurantId, response.getBody().restaurantId());
		verify(getMenuItemByIdUseCase).execute(restaurantId, id);
	}

	@Test
	@DisplayName("Deve listar itens do cardapio por restaurante")
	void deveListarItensDoCardapioPorRestaurante() {
		ListMenuItemsByRestaurantUseCase listMenuItemsByRestaurantUseCase = mock(ListMenuItemsByRestaurantUseCase.class);
		MenuItemController controller = criarController(listMenuItemsByRestaurantUseCase);
		UUID restaurantId = UUID.randomUUID();
		when(listMenuItemsByRestaurantUseCase.execute(restaurantId)).thenReturn(List.of(
				criarMenuItem(UUID.randomUUID(), restaurantId),
				criarMenuItem(UUID.randomUUID(), restaurantId)
		));

		ResponseEntity<List<MenuItemResponse>> response = controller.findByRestaurant(restaurantId);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(2, response.getBody().size());
		assertEquals(restaurantId, response.getBody().get(0).restaurantId());
		assertEquals(restaurantId, response.getBody().get(1).restaurantId());
		verify(listMenuItemsByRestaurantUseCase).execute(restaurantId);
	}

	@Test
	@DisplayName("Deve atualizar item do cardapio")
	void deveAtualizarItemDoCardapio() {
		UpdateMenuItemUseCase updateMenuItemUseCase = mock(UpdateMenuItemUseCase.class);
		MenuItemController controller = criarController(updateMenuItemUseCase);
		UUID id = UUID.randomUUID();
		UUID restaurantId = UUID.randomUUID();
		MenuItemRequest request = criarRequest();
		when(updateMenuItemUseCase.execute(
				restaurantId,
				id,
				"Pizza Margherita",
				"Pizza com molho de tomate",
				new BigDecimal("49.90"),
				"fotos/pizza.png",
				false
		)).thenReturn(criarMenuItem(id, restaurantId));

		ResponseEntity<MenuItemResponse> response = controller.update(restaurantId, id, request);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(id, response.getBody().id());
		assertEquals(restaurantId, response.getBody().restaurantId());
		verify(updateMenuItemUseCase).execute(
				restaurantId,
				id,
				"Pizza Margherita",
				"Pizza com molho de tomate",
				new BigDecimal("49.90"),
				"fotos/pizza.png",
				false
		);
	}

	@Test
	@DisplayName("Deve remover item do cardapio")
	void deveRemoverItemDoCardapio() {
		DeleteMenuItemUseCase deleteMenuItemUseCase = mock(DeleteMenuItemUseCase.class);
		MenuItemController controller = criarController(deleteMenuItemUseCase);
		UUID id = UUID.randomUUID();
		UUID restaurantId = UUID.randomUUID();

		ResponseEntity<Void> response = controller.delete(restaurantId, id);

		assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
		assertNull(response.getBody());
		verify(deleteMenuItemUseCase).execute(restaurantId, id);
	}

	private MenuItemController criarController(CreateMenuItemUseCase createMenuItemUseCase) {
		return new MenuItemController(
				createMenuItemUseCase,
				mock(GetMenuItemByIdUseCase.class),
				mock(ListMenuItemsByRestaurantUseCase.class),
				mock(UpdateMenuItemUseCase.class),
				mock(DeleteMenuItemUseCase.class)
		);
	}

	private MenuItemController criarController(GetMenuItemByIdUseCase getMenuItemByIdUseCase) {
		return new MenuItemController(
				mock(CreateMenuItemUseCase.class),
				getMenuItemByIdUseCase,
				mock(ListMenuItemsByRestaurantUseCase.class),
				mock(UpdateMenuItemUseCase.class),
				mock(DeleteMenuItemUseCase.class)
		);
	}

	private MenuItemController criarController(ListMenuItemsByRestaurantUseCase listMenuItemsByRestaurantUseCase) {
		return new MenuItemController(
				mock(CreateMenuItemUseCase.class),
				mock(GetMenuItemByIdUseCase.class),
				listMenuItemsByRestaurantUseCase,
				mock(UpdateMenuItemUseCase.class),
				mock(DeleteMenuItemUseCase.class)
		);
	}

	private MenuItemController criarController(UpdateMenuItemUseCase updateMenuItemUseCase) {
		return new MenuItemController(
				mock(CreateMenuItemUseCase.class),
				mock(GetMenuItemByIdUseCase.class),
				mock(ListMenuItemsByRestaurantUseCase.class),
				updateMenuItemUseCase,
				mock(DeleteMenuItemUseCase.class)
		);
	}

	private MenuItemController criarController(DeleteMenuItemUseCase deleteMenuItemUseCase) {
		return new MenuItemController(
				mock(CreateMenuItemUseCase.class),
				mock(GetMenuItemByIdUseCase.class),
				mock(ListMenuItemsByRestaurantUseCase.class),
				mock(UpdateMenuItemUseCase.class),
				deleteMenuItemUseCase
		);
	}

	private MenuItemRequest criarRequest() {
		return new MenuItemRequest(
				"Pizza Margherita",
				"Pizza com molho de tomate",
				new BigDecimal("49.90"),
				"fotos/pizza.png",
				false
		);
	}

	private MenuItem criarMenuItem(UUID id, UUID restaurantId) {
		return new MenuItem(
				id,
				"Pizza Margherita",
				"Pizza com molho de tomate",
				new BigDecimal("49.90"),
				restaurantId,
				"fotos/pizza.png",
				false,
				OffsetDateTime.parse("2026-06-24T01:00:00-03:00")
		);
	}
}
