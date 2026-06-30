package com.fiap.foodlink_api.domain.entity;

import com.fiap.foodlink_api.domain.exception.DomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Item do cardapio")
class MenuItemTest {

	@Test
	@DisplayName("Deve criar item do cardapio")
	void deveCriarItemDoCardapio() {
		UUID restaurantId = UUID.randomUUID();

		MenuItem menuItem = MenuItem.create(
				"Pizza Margherita",
				"Pizza com molho de tomate, mussarela e manjericao",
				new BigDecimal("49.90"),
				restaurantId,
				"fotos/pizza-margherita.png",
				false
		);

		assertNull(menuItem.getId());
		assertEquals("Pizza Margherita", menuItem.getName());
		assertEquals("Pizza com molho de tomate, mussarela e manjericao", menuItem.getDescription());
		assertEquals(new BigDecimal("49.90"), menuItem.getPrice());
		assertEquals(restaurantId, menuItem.getRestaurantId());
		assertEquals("fotos/pizza-margherita.png", menuItem.getPhotoPath());
		assertNotNull(menuItem.getLastUpdatedAt());
	}

	@Test
	@DisplayName("Deve criar item disponivel apenas no restaurante")
	void deveCriarItemDisponivelApenasNoRestaurante() {
		MenuItem menuItem = MenuItem.create(
				"Suco Natural",
				"Suco de laranja natural",
				new BigDecimal("12.00"),
				UUID.randomUUID(),
				"fotos/suco-natural.png",
				true
		);

		assertTrue(menuItem.isAvailableOnlyAtRestaurant());
	}

	@Test
	@DisplayName("Deve normalizar textos ao criar item do cardapio")
	void deveNormalizarTextosAoCriarItemDoCardapio() {
		MenuItem menuItem = MenuItem.create(
				"  Pizza Margherita  ",
				"  Pizza com molho de tomate  ",
				new BigDecimal("49.90"),
				UUID.randomUUID(),
				"  fotos/pizza-margherita.png  ",
				false
		);

		assertEquals("Pizza Margherita", menuItem.getName());
		assertEquals("Pizza com molho de tomate", menuItem.getDescription());
		assertEquals("fotos/pizza-margherita.png", menuItem.getPhotoPath());
	}

	@Test
	@DisplayName("Deve atualizar item do cardapio")
	void deveAtualizarItemDoCardapio() {
		MenuItem menuItem = criarMenuItem();
		UUID restaurantId = menuItem.getRestaurantId();

		menuItem.update(
				"Hamburguer Artesanal",
				"Hamburguer com queijo e molho da casa",
				new BigDecimal("39.90"),
				"fotos/hamburguer-artesanal.png",
				true
		);

		assertEquals("Hamburguer Artesanal", menuItem.getName());
		assertEquals("Hamburguer com queijo e molho da casa", menuItem.getDescription());
		assertEquals(new BigDecimal("39.90"), menuItem.getPrice());
		assertEquals(restaurantId, menuItem.getRestaurantId());
		assertEquals("fotos/hamburguer-artesanal.png", menuItem.getPhotoPath());
		assertTrue(menuItem.isAvailableOnlyAtRestaurant());
	}

	@Test
	@DisplayName("Deve manter identificador ao atualizar item do cardapio")
	void deveManterIdentificadorAoAtualizarItemDoCardapio() {
		UUID id = UUID.randomUUID();
		MenuItem menuItem = new MenuItem(
				id,
				"Pizza Margherita",
				"Pizza com molho de tomate, mussarela e manjericao",
				new BigDecimal("49.90"),
				UUID.randomUUID(),
				"fotos/pizza-margherita.png",
				false,
				OffsetDateTime.parse("2026-06-24T01:00:00-03:00")
		);

		menuItem.update(
				"Hamburguer Artesanal",
				"Hamburguer com queijo e molho da casa",
				new BigDecimal("39.90"),
				"fotos/hamburguer-artesanal.png",
				true
		);

		assertEquals(id, menuItem.getId());
	}

	@Test
	@DisplayName("Deve manter restaurante ao atualizar item do cardapio")
	void deveManterRestauranteAoAtualizarItemDoCardapio() {
		UUID restaurantId = UUID.randomUUID();
		MenuItem menuItem = new MenuItem(
				UUID.randomUUID(),
				"Pizza Margherita",
				"Pizza com molho de tomate, mussarela e manjericao",
				new BigDecimal("49.90"),
				restaurantId,
				"fotos/pizza-margherita.png",
				false,
				OffsetDateTime.parse("2026-06-24T01:00:00-03:00")
		);

		menuItem.update(
				"Hamburguer Artesanal",
				"Hamburguer com queijo e molho da casa",
				new BigDecimal("39.90"),
				"fotos/hamburguer-artesanal.png",
				true
		);

		assertEquals(restaurantId, menuItem.getRestaurantId());
	}

	@Test
	@DisplayName("Deve lancar excecao ao criar item sem nome")
	void deveLancarExcecaoAoCriarItemSemNome() {
		DomainException exception = assertThrows(
				DomainException.class,
				() -> MenuItem.create(
						" ",
						"Pizza com molho de tomate",
						new BigDecimal("49.90"),
						UUID.randomUUID(),
						"fotos/pizza.png",
						false
				)
		);

		assertEquals("Nome do item do cardapio e obrigatorio.", exception.getMessage());
	}

	@Test
	@DisplayName("Deve lancar excecao ao criar item sem descricao")
	void deveLancarExcecaoAoCriarItemSemDescricao() {
		DomainException exception = assertThrows(
				DomainException.class,
				() -> MenuItem.create(
						"Pizza Margherita",
						null,
						new BigDecimal("49.90"),
						UUID.randomUUID(),
						"fotos/pizza.png",
						false
				)
		);

		assertEquals("Descricao do item do cardapio e obrigatoria.", exception.getMessage());
	}

	@Test
	@DisplayName("Deve lancar excecao ao criar item com preco nulo")
	void deveLancarExcecaoAoCriarItemComPrecoNulo() {
		NullPointerException exception = assertThrows(
				NullPointerException.class,
				() -> MenuItem.create(
						"Pizza Margherita",
						"Pizza com molho de tomate",
						null,
						UUID.randomUUID(),
						"fotos/pizza.png",
						false
				)
		);

		assertEquals("Preco do item do cardapio e obrigatorio.", exception.getMessage());
	}

	@Test
	@DisplayName("Deve lancar excecao ao criar item com preco zero")
	void deveLancarExcecaoAoCriarItemComPrecoZero() {
		DomainException exception = assertThrows(
				DomainException.class,
				() -> MenuItem.create(
						"Pizza Margherita",
						"Pizza com molho de tomate",
						BigDecimal.ZERO,
						UUID.randomUUID(),
						"fotos/pizza.png",
						false
				)
		);

		assertEquals("Preco do item do cardapio deve ser maior que zero.", exception.getMessage());
	}

	@Test
	@DisplayName("Deve lancar excecao ao criar item com preco negativo")
	void deveLancarExcecaoAoCriarItemComPrecoNegativo() {
		DomainException exception = assertThrows(
				DomainException.class,
				() -> MenuItem.create(
						"Pizza Margherita",
						"Pizza com molho de tomate",
						new BigDecimal("-1.00"),
						UUID.randomUUID(),
						"fotos/pizza.png",
						false
				)
		);

		assertEquals("Preco do item do cardapio deve ser maior que zero.", exception.getMessage());
	}

	@Test
	@DisplayName("Deve lancar excecao ao criar item sem restaurante")
	void deveLancarExcecaoAoCriarItemSemRestaurante() {
		NullPointerException exception = assertThrows(
				NullPointerException.class,
				() -> MenuItem.create(
						"Pizza Margherita",
						"Pizza com molho de tomate",
						new BigDecimal("49.90"),
						null,
						"fotos/pizza.png",
						false
				)
		);

		assertEquals("Restaurante do item do cardapio e obrigatorio.", exception.getMessage());
	}

	@Test
	@DisplayName("Deve lancar excecao ao criar item sem caminho da foto")
	void deveLancarExcecaoAoCriarItemSemCaminhoDaFoto() {
		DomainException exception = assertThrows(
				DomainException.class,
				() -> MenuItem.create(
						"Pizza Margherita",
						"Pizza com molho de tomate",
						new BigDecimal("49.90"),
						UUID.randomUUID(),
						" ",
						false
				)
		);

		assertEquals("Caminho da foto do item do cardapio e obrigatorio.", exception.getMessage());
	}

	@Test
	@DisplayName("Deve lancar excecao ao criar item com descricao maior que quatrocentos caracteres")
	void deveLancarExcecaoAoCriarItemComDescricaoMaiorQueQuatrocentosCaracteres() {
		String description = "a".repeat(401);

		DomainException exception = assertThrows(
				DomainException.class,
				() -> MenuItem.create(
						"Pizza Margherita",
						description,
						new BigDecimal("49.90"),
						UUID.randomUUID(),
						"fotos/pizza.png",
						false
				)
		);

		assertEquals("Campo de item do cardapio deve ter no maximo 400 caracteres.", exception.getMessage());
	}

	private MenuItem criarMenuItem() {
		return MenuItem.create(
				"Pizza Margherita",
				"Pizza com molho de tomate, mussarela e manjericao",
				new BigDecimal("49.90"),
				UUID.randomUUID(),
				"fotos/pizza-margherita.png",
				false
		);
	}
}
