package com.fiap.foodlink_api.application.usecase.menuitem;

import com.fiap.foodlink_api.domain.entity.MenuItem;
import com.fiap.foodlink_api.domain.exception.MenuItemNotFoundException;
import com.fiap.foodlink_api.domain.exception.RestaurantNotFoundException;
import com.fiap.foodlink_api.domain.gateway.MenuItemGateway;
import com.fiap.foodlink_api.domain.gateway.RestaurantGateway;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Casos de uso de item do cardapio")
class MenuItemUseCaseTest {

	@Test
	@DisplayName("Deve criar item do cardapio")
	void deveCriarItemDoCardapio() {
		MenuItemGateway menuItemGateway = mock(MenuItemGateway.class);
		RestaurantGateway restaurantGateway = mock(RestaurantGateway.class);
		UUID id = UUID.randomUUID();
		UUID restaurantId = UUID.randomUUID();
		when(restaurantGateway.existsById(restaurantId)).thenReturn(true);
		when(menuItemGateway.save(any(MenuItem.class))).thenReturn(criarMenuItem(id, restaurantId));
		CreateMenuItemUseCase useCase = new CreateMenuItemUseCase(menuItemGateway, restaurantGateway);

		MenuItem menuItem = useCase.execute(
				"Pizza Margherita",
				"Pizza com molho de tomate, mussarela e manjericao",
				new BigDecimal("49.90"),
				restaurantId,
				"fotos/pizza-margherita.png",
				false
		);

		assertEquals(id, menuItem.getId());
		assertEquals("Pizza Margherita", menuItem.getName());
		verify(restaurantGateway).existsById(restaurantId);
		verify(menuItemGateway).save(any(MenuItem.class));
	}

	@Test
	@DisplayName("Deve enviar item normalizado para persistencia ao criar")
	void deveEnviarItemNormalizadoParaPersistenciaAoCriar() {
		MenuItemGateway menuItemGateway = mock(MenuItemGateway.class);
		RestaurantGateway restaurantGateway = mock(RestaurantGateway.class);
		UUID restaurantId = UUID.randomUUID();
		when(restaurantGateway.existsById(restaurantId)).thenReturn(true);
		when(menuItemGateway.save(any(MenuItem.class))).thenAnswer(invocation -> invocation.getArgument(0));
		CreateMenuItemUseCase useCase = new CreateMenuItemUseCase(menuItemGateway, restaurantGateway);
		ArgumentCaptor<MenuItem> captor = ArgumentCaptor.forClass(MenuItem.class);

		useCase.execute(
				"  Pizza Margherita  ",
				"  Pizza com molho de tomate  ",
				new BigDecimal("49.90"),
				restaurantId,
				"  fotos/pizza-margherita.png  ",
				false
		);

		verify(menuItemGateway).save(captor.capture());
		assertEquals("Pizza Margherita", captor.getValue().getName());
		assertEquals("Pizza com molho de tomate", captor.getValue().getDescription());
		assertEquals("fotos/pizza-margherita.png", captor.getValue().getPhotoPath());
	}

	@Test
	@DisplayName("Deve lancar excecao ao criar item para restaurante inexistente")
	void deveLancarExcecaoAoCriarItemParaRestauranteInexistente() {
		MenuItemGateway menuItemGateway = mock(MenuItemGateway.class);
		RestaurantGateway restaurantGateway = mock(RestaurantGateway.class);
		UUID restaurantId = UUID.randomUUID();
		when(restaurantGateway.existsById(restaurantId)).thenReturn(false);
		CreateMenuItemUseCase useCase = new CreateMenuItemUseCase(menuItemGateway, restaurantGateway);

		RestaurantNotFoundException exception = assertThrows(
				RestaurantNotFoundException.class,
				() -> useCase.execute(
						"Pizza Margherita",
						"Pizza com molho de tomate",
						new BigDecimal("49.90"),
						restaurantId,
						"fotos/pizza.png",
						false
				)
		);

		assertEquals("Restaurante nao encontrado: " + restaurantId, exception.getMessage());
		verify(menuItemGateway, never()).save(any(MenuItem.class));
	}

	@Test
	@DisplayName("Deve buscar item do cardapio por restaurante e identificador")
	void deveBuscarItemDoCardapioPorRestauranteEIdentificador() {
		MenuItemGateway menuItemGateway = mock(MenuItemGateway.class);
		RestaurantGateway restaurantGateway = mock(RestaurantGateway.class);
		UUID id = UUID.randomUUID();
		UUID restaurantId = UUID.randomUUID();
		MenuItem savedMenuItem = criarMenuItem(id, restaurantId);
		when(restaurantGateway.existsById(restaurantId)).thenReturn(true);
		when(menuItemGateway.findById(id)).thenReturn(Optional.of(savedMenuItem));
		GetMenuItemByIdUseCase useCase = new GetMenuItemByIdUseCase(menuItemGateway, restaurantGateway);

		MenuItem menuItem = useCase.execute(restaurantId, id);

		assertEquals(id, menuItem.getId());
		assertEquals("Pizza Margherita", menuItem.getName());
		verify(restaurantGateway).existsById(restaurantId);
		verify(menuItemGateway).findById(id);
	}

	@Test
	@DisplayName("Deve lancar excecao ao buscar item inexistente")
	void deveLancarExcecaoAoBuscarItemInexistente() {
		MenuItemGateway menuItemGateway = mock(MenuItemGateway.class);
		RestaurantGateway restaurantGateway = mock(RestaurantGateway.class);
		UUID restaurantId = UUID.randomUUID();
		UUID id = UUID.randomUUID();
		when(restaurantGateway.existsById(restaurantId)).thenReturn(true);
		when(menuItemGateway.findById(id)).thenReturn(Optional.empty());
		GetMenuItemByIdUseCase useCase = new GetMenuItemByIdUseCase(menuItemGateway, restaurantGateway);

		MenuItemNotFoundException exception = assertThrows(
				MenuItemNotFoundException.class,
				() -> useCase.execute(restaurantId, id)
		);

		assertEquals("Item do cardapio nao encontrado: " + id, exception.getMessage());
		verify(menuItemGateway).findById(id);
	}

	@Test
	@DisplayName("Deve lancar excecao ao buscar item de outro restaurante")
	void deveLancarExcecaoAoBuscarItemDeOutroRestaurante() {
		MenuItemGateway menuItemGateway = mock(MenuItemGateway.class);
		RestaurantGateway restaurantGateway = mock(RestaurantGateway.class);
		UUID restaurantId = UUID.randomUUID();
		UUID id = UUID.randomUUID();
		when(restaurantGateway.existsById(restaurantId)).thenReturn(true);
		when(menuItemGateway.findById(id)).thenReturn(Optional.of(criarMenuItem(id, UUID.randomUUID())));
		GetMenuItemByIdUseCase useCase = new GetMenuItemByIdUseCase(menuItemGateway, restaurantGateway);

		MenuItemNotFoundException exception = assertThrows(
				MenuItemNotFoundException.class,
				() -> useCase.execute(restaurantId, id)
		);

		assertEquals("Item do cardapio nao encontrado: " + id, exception.getMessage());
		verify(menuItemGateway).findById(id);
	}

	@Test
	@DisplayName("Deve listar itens do cardapio por restaurante")
	void deveListarItensDoCardapioPorRestaurante() {
		MenuItemGateway menuItemGateway = mock(MenuItemGateway.class);
		RestaurantGateway restaurantGateway = mock(RestaurantGateway.class);
		UUID restaurantId = UUID.randomUUID();
		when(restaurantGateway.existsById(restaurantId)).thenReturn(true);
		when(menuItemGateway.findByRestaurantId(restaurantId)).thenReturn(List.of(
				criarMenuItem(UUID.randomUUID(), restaurantId)
		));
		ListMenuItemsByRestaurantUseCase useCase = new ListMenuItemsByRestaurantUseCase(
				menuItemGateway,
				restaurantGateway
		);

		List<MenuItem> menuItems = useCase.execute(restaurantId);

		assertEquals(1, menuItems.size());
		assertEquals(restaurantId, menuItems.get(0).getRestaurantId());
		verify(restaurantGateway).existsById(restaurantId);
		verify(menuItemGateway).findByRestaurantId(restaurantId);
	}

	@Test
	@DisplayName("Deve lancar excecao ao listar itens de restaurante inexistente")
	void deveLancarExcecaoAoListarItensDeRestauranteInexistente() {
		MenuItemGateway menuItemGateway = mock(MenuItemGateway.class);
		RestaurantGateway restaurantGateway = mock(RestaurantGateway.class);
		UUID restaurantId = UUID.randomUUID();
		when(restaurantGateway.existsById(restaurantId)).thenReturn(false);
		ListMenuItemsByRestaurantUseCase useCase = new ListMenuItemsByRestaurantUseCase(
				menuItemGateway,
				restaurantGateway
		);

		RestaurantNotFoundException exception = assertThrows(
				RestaurantNotFoundException.class,
				() -> useCase.execute(restaurantId)
		);

		assertEquals("Restaurante nao encontrado: " + restaurantId, exception.getMessage());
		verify(menuItemGateway, never()).findByRestaurantId(restaurantId);
	}

	@Test
	@DisplayName("Deve atualizar item do cardapio")
	void deveAtualizarItemDoCardapio() {
		MenuItemGateway menuItemGateway = mock(MenuItemGateway.class);
		RestaurantGateway restaurantGateway = mock(RestaurantGateway.class);
		UUID id = UUID.randomUUID();
		UUID restaurantId = UUID.randomUUID();
		MenuItem savedMenuItem = criarMenuItem(id, restaurantId);
		when(restaurantGateway.existsById(restaurantId)).thenReturn(true);
		when(menuItemGateway.findById(id)).thenReturn(Optional.of(savedMenuItem));
		when(menuItemGateway.save(any(MenuItem.class))).thenAnswer(invocation -> invocation.getArgument(0));
		UpdateMenuItemUseCase useCase = new UpdateMenuItemUseCase(menuItemGateway, restaurantGateway);
		ArgumentCaptor<MenuItem> captor = ArgumentCaptor.forClass(MenuItem.class);

		MenuItem menuItem = useCase.execute(
				restaurantId,
				id,
				"Hamburguer Artesanal",
				"Hamburguer com queijo e molho da casa",
				new BigDecimal("39.90"),
				"fotos/hamburguer-artesanal.png",
				true
		);

		assertEquals(id, menuItem.getId());
		assertEquals("Hamburguer Artesanal", menuItem.getName());
		assertEquals(restaurantId, menuItem.getRestaurantId());
		verify(restaurantGateway).existsById(restaurantId);
		verify(menuItemGateway).save(captor.capture());
		assertEquals("fotos/hamburguer-artesanal.png", captor.getValue().getPhotoPath());
	}

	@Test
	@DisplayName("Deve lancar excecao ao atualizar item inexistente")
	void deveLancarExcecaoAoAtualizarItemInexistente() {
		MenuItemGateway menuItemGateway = mock(MenuItemGateway.class);
		RestaurantGateway restaurantGateway = mock(RestaurantGateway.class);
		UUID restaurantId = UUID.randomUUID();
		UUID id = UUID.randomUUID();
		when(restaurantGateway.existsById(restaurantId)).thenReturn(true);
		when(menuItemGateway.findById(id)).thenReturn(Optional.empty());
		UpdateMenuItemUseCase useCase = new UpdateMenuItemUseCase(menuItemGateway, restaurantGateway);

		MenuItemNotFoundException exception = assertThrows(
				MenuItemNotFoundException.class,
				() -> useCase.execute(
						restaurantId,
						id,
						"Hamburguer Artesanal",
						"Hamburguer com queijo e molho da casa",
						new BigDecimal("39.90"),
						"fotos/hamburguer.png",
						true
				)
		);

		assertEquals("Item do cardapio nao encontrado: " + id, exception.getMessage());
		verify(menuItemGateway, never()).save(any(MenuItem.class));
	}

	@Test
	@DisplayName("Deve manter restaurante ao atualizar item do cardapio")
	void deveManterRestauranteAoAtualizarItemDoCardapio() {
		MenuItemGateway menuItemGateway = mock(MenuItemGateway.class);
		RestaurantGateway restaurantGateway = mock(RestaurantGateway.class);
		UUID id = UUID.randomUUID();
		UUID restaurantId = UUID.randomUUID();
		MenuItem savedMenuItem = criarMenuItem(id, restaurantId);
		when(restaurantGateway.existsById(restaurantId)).thenReturn(true);
		when(menuItemGateway.findById(id)).thenReturn(Optional.of(savedMenuItem));
		when(menuItemGateway.save(any(MenuItem.class))).thenAnswer(invocation -> invocation.getArgument(0));
		UpdateMenuItemUseCase useCase = new UpdateMenuItemUseCase(menuItemGateway, restaurantGateway);

		MenuItem menuItem = useCase.execute(
				restaurantId,
				id,
				"Hamburguer Artesanal",
				"Hamburguer com queijo e molho da casa",
				new BigDecimal("39.90"),
				"fotos/hamburguer.png",
				true
		);

		assertEquals(restaurantId, menuItem.getRestaurantId());
		verify(menuItemGateway).save(any(MenuItem.class));
	}

	@Test
	@DisplayName("Deve lancar excecao ao atualizar item de outro restaurante")
	void deveLancarExcecaoAoAtualizarItemDeOutroRestaurante() {
		MenuItemGateway menuItemGateway = mock(MenuItemGateway.class);
		RestaurantGateway restaurantGateway = mock(RestaurantGateway.class);
		UUID restaurantId = UUID.randomUUID();
		UUID id = UUID.randomUUID();
		when(restaurantGateway.existsById(restaurantId)).thenReturn(true);
		when(menuItemGateway.findById(id)).thenReturn(Optional.of(criarMenuItem(id, UUID.randomUUID())));
		UpdateMenuItemUseCase useCase = new UpdateMenuItemUseCase(menuItemGateway, restaurantGateway);

		MenuItemNotFoundException exception = assertThrows(
				MenuItemNotFoundException.class,
				() -> useCase.execute(
						restaurantId,
						id,
						"Hamburguer Artesanal",
						"Hamburguer com queijo e molho da casa",
						new BigDecimal("39.90"),
						"fotos/hamburguer.png",
						true
				)
		);

		assertEquals("Item do cardapio nao encontrado: " + id, exception.getMessage());
		verify(menuItemGateway, never()).save(any(MenuItem.class));
	}

	@Test
	@DisplayName("Deve remover item do cardapio cadastrado")
	void deveRemoverItemDoCardapioCadastrado() {
		MenuItemGateway menuItemGateway = mock(MenuItemGateway.class);
		RestaurantGateway restaurantGateway = mock(RestaurantGateway.class);
		UUID restaurantId = UUID.randomUUID();
		UUID id = UUID.randomUUID();
		when(restaurantGateway.existsById(restaurantId)).thenReturn(true);
		when(menuItemGateway.findById(id)).thenReturn(Optional.of(criarMenuItem(id, restaurantId)));
		DeleteMenuItemUseCase useCase = new DeleteMenuItemUseCase(menuItemGateway, restaurantGateway);

		useCase.execute(restaurantId, id);

		verify(restaurantGateway).existsById(restaurantId);
		verify(menuItemGateway).findById(id);
		verify(menuItemGateway).deleteById(id);
	}

	@Test
	@DisplayName("Deve lancar excecao ao remover item inexistente")
	void deveLancarExcecaoAoRemoverItemInexistente() {
		MenuItemGateway menuItemGateway = mock(MenuItemGateway.class);
		RestaurantGateway restaurantGateway = mock(RestaurantGateway.class);
		UUID restaurantId = UUID.randomUUID();
		UUID id = UUID.randomUUID();
		when(restaurantGateway.existsById(restaurantId)).thenReturn(true);
		when(menuItemGateway.findById(id)).thenReturn(Optional.empty());
		DeleteMenuItemUseCase useCase = new DeleteMenuItemUseCase(menuItemGateway, restaurantGateway);

		MenuItemNotFoundException exception = assertThrows(
				MenuItemNotFoundException.class,
				() -> useCase.execute(restaurantId, id)
		);

		assertEquals("Item do cardapio nao encontrado: " + id, exception.getMessage());
		verify(menuItemGateway, never()).deleteById(id);
	}

	@Test
	@DisplayName("Deve lancar excecao ao remover item de outro restaurante")
	void deveLancarExcecaoAoRemoverItemDeOutroRestaurante() {
		MenuItemGateway menuItemGateway = mock(MenuItemGateway.class);
		RestaurantGateway restaurantGateway = mock(RestaurantGateway.class);
		UUID restaurantId = UUID.randomUUID();
		UUID id = UUID.randomUUID();
		when(restaurantGateway.existsById(restaurantId)).thenReturn(true);
		when(menuItemGateway.findById(id)).thenReturn(Optional.of(criarMenuItem(id, UUID.randomUUID())));
		DeleteMenuItemUseCase useCase = new DeleteMenuItemUseCase(menuItemGateway, restaurantGateway);

		MenuItemNotFoundException exception = assertThrows(
				MenuItemNotFoundException.class,
				() -> useCase.execute(restaurantId, id)
		);

		assertEquals("Item do cardapio nao encontrado: " + id, exception.getMessage());
		verify(menuItemGateway, never()).deleteById(id);
	}

	private MenuItem criarMenuItem(UUID id, UUID restaurantId) {
		return new MenuItem(
				id,
				"Pizza Margherita",
				"Pizza com molho de tomate, mussarela e manjericao",
				new BigDecimal("49.90"),
				restaurantId,
				"fotos/pizza-margherita.png",
				false,
				OffsetDateTime.parse("2026-06-24T01:00:00-03:00")
		);
	}
}
