package com.fiap.foodlink_api.domain.entity;

import com.fiap.foodlink_api.domain.exception.DomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Restaurante")
class RestaurantTest {

	@Test
	@DisplayName("Deve criar restaurante com dados validos")
	void deveCriarRestauranteComDadosValidos() {
		UUID ownerId = UUID.randomUUID();
		UUID addressId = UUID.randomUUID();

		Restaurant restaurant = Restaurant.create(
				"Pizzaria Italiana",
				"12.345.678/0001-99",
				"Italiana",
				ownerId,
				addressId
		);

		assertNull(restaurant.getId());
		assertEquals("Pizzaria Italiana", restaurant.getName());
		assertEquals("12.345.678/0001-99", restaurant.getCnpj());
		assertEquals("Italiana", restaurant.getType());
		assertEquals(ownerId, restaurant.getOwnerId());
		assertEquals(addressId, restaurant.getAddressId());
		assertNotNull(restaurant.getLastUpdatedAt());
	}

	@Test
	@DisplayName("Deve criar restaurante com identificador existente")
	void deveCriarRestauranteComIdentificadorExistente() {
		UUID id = UUID.randomUUID();
		UUID ownerId = UUID.randomUUID();
		UUID addressId = UUID.randomUUID();
		OffsetDateTime lastUpdatedAt = OffsetDateTime.parse("2020-01-01T10:00:00-03:00");

		Restaurant restaurant = new Restaurant(
				id,
				"Pizzaria Italiana",
				"12.345.678/0001-99",
				"Italiana",
				ownerId,
				addressId,
				lastUpdatedAt
		);

		assertEquals(id, restaurant.getId());
		assertEquals(lastUpdatedAt, restaurant.getLastUpdatedAt());
	}

	@Test
	@DisplayName("Deve normalizar campos de texto do restaurante")
	void deveNormalizarCamposTextoDoRestaurante() {
		Restaurant restaurant = Restaurant.create(
				"  Pizzaria Italiana  ",
				"  12.345.678/0001-99  ",
				"  Italiana  ",
				UUID.randomUUID(),
				UUID.randomUUID()
		);

		assertEquals("Pizzaria Italiana", restaurant.getName());
		assertEquals("12.345.678/0001-99", restaurant.getCnpj());
		assertEquals("Italiana", restaurant.getType());
	}

	@Test
	@DisplayName("Deve atualizar dados do restaurante")
	void deveAtualizarDadosDoRestaurante() {
		OffsetDateTime lastUpdatedAt = OffsetDateTime.parse("2020-01-01T10:00:00-03:00");
		UUID ownerId = UUID.randomUUID();
		UUID newOwnerId = UUID.randomUUID();
		Restaurant restaurant = new Restaurant(
				UUID.randomUUID(),
				"Pizzaria Italiana",
				"12.345.678/0001-99",
				"Italiana",
				ownerId,
				UUID.randomUUID(),
				lastUpdatedAt
		);

		restaurant.update("Churrascaria Gourmet", "98.765.432/0001-11", "Brasileira", newOwnerId);

		assertEquals("Churrascaria Gourmet", restaurant.getName());
		assertEquals("98.765.432/0001-11", restaurant.getCnpj());
		assertEquals("Brasileira", restaurant.getType());
		assertEquals(newOwnerId, restaurant.getOwnerId());
		assertTrue(restaurant.getLastUpdatedAt().isAfter(lastUpdatedAt));
	}

	@Test
	@DisplayName("Deve lancar excecao quando nome estiver em branco")
	void deveLancarExcecaoQuandoNomeEstiverEmBranco() {
		DomainException exception = assertThrows(
				DomainException.class,
				() -> Restaurant.create(" ", "12.345.678/0001-99", "Italiana", UUID.randomUUID(), UUID.randomUUID())
		);

		assertEquals("Nome do restaurante e obrigatorio.", exception.getMessage());
	}

	@Test
	@DisplayName("Deve lancar excecao quando cnpj estiver em branco")
	void deveLancarExcecaoQuandoCnpjEstiverEmBranco() {
		DomainException exception = assertThrows(
				DomainException.class,
				() -> Restaurant.create("Pizzaria Italiana", " ", "Italiana", UUID.randomUUID(), UUID.randomUUID())
		);

		assertEquals("CNPJ do restaurante e obrigatorio.", exception.getMessage());
	}

	@Test
	@DisplayName("Deve lancar excecao quando tipo estiver em branco")
	void deveLancarExcecaoQuandoTipoEstiverEmBranco() {
		DomainException exception = assertThrows(
				DomainException.class,
				() -> Restaurant.create("Pizzaria Italiana", "12.345.678/0001-99", " ", UUID.randomUUID(), UUID.randomUUID())
		);

		assertEquals("Tipo de cozinha do restaurante e obrigatorio.", exception.getMessage());
	}

	@Test
	@DisplayName("Deve lancar excecao quando dono for nulo")
	void deveLancarExcecaoQuandoDonForNulo() {
		NullPointerException exception = assertThrows(
				NullPointerException.class,
				() -> Restaurant.create("Pizzaria Italiana", "12.345.678/0001-99", "Italiana", null, UUID.randomUUID())
		);

		assertEquals("Dono do restaurante e obrigatorio.", exception.getMessage());
	}

	@Test
	@DisplayName("Deve lancar excecao quando endereco for nulo")
	void deveLancarExcecaoQuandoEnderecoForNulo() {
		NullPointerException exception = assertThrows(
				NullPointerException.class,
				() -> Restaurant.create("Pizzaria Italiana", "12.345.678/0001-99", "Italiana", UUID.randomUUID(), null)
		);

		assertEquals("Endereco do restaurante e obrigatorio.", exception.getMessage());
	}

	@Test
	@DisplayName("Deve lancar excecao quando nome exceder tamanho maximo")
	void deveLancarExcecaoQuandoNomeExcederTamanhoMaximo() {
		String name = "a".repeat(256);

		DomainException exception = assertThrows(
				DomainException.class,
				() -> Restaurant.create(name, "12.345.678/0001-99", "Italiana", UUID.randomUUID(), UUID.randomUUID())
		);

		assertEquals("Campo do restaurante deve ter no maximo 255 caracteres.", exception.getMessage());
	}

	@Test
	@DisplayName("Deve lancar excecao quando cnpj exceder tamanho maximo")
	void deveLancarExcecaoQuandoCnpjExcederTamanhoMaximo() {
		String cnpj = "a".repeat(19);

		DomainException exception = assertThrows(
				DomainException.class,
				() -> Restaurant.create("Pizzaria Italiana", cnpj, "Italiana", UUID.randomUUID(), UUID.randomUUID())
		);

		assertEquals("Campo do restaurante deve ter no maximo 18 caracteres.", exception.getMessage());
	}

	@Test
	@DisplayName("Deve lancar excecao quando tipo exceder tamanho maximo")
	void deveLancarExcecaoQuandoTipoExcederTamanhoMaximo() {
		String type = "a".repeat(256);

		DomainException exception = assertThrows(
				DomainException.class,
				() -> Restaurant.create("Pizzaria Italiana", "12.345.678/0001-99", type, UUID.randomUUID(), UUID.randomUUID())
		);

		assertEquals("Campo do restaurante deve ter no maximo 255 caracteres.", exception.getMessage());
	}

	@Test
	@DisplayName("Deve lançar exceção ao atualizar com nome em branco")
	void deveLancarExcecaoAoAtualizarComNomeEmBranco() {
		Restaurant restaurant = new Restaurant(
				UUID.randomUUID(),
				"Pizzaria Italiana",
				"12.345.678/0001-99",
				"Italiana",
				UUID.randomUUID(),
				UUID.randomUUID(),
				OffsetDateTime.now()
		);

		DomainException exception = assertThrows(
				DomainException.class,
				() -> restaurant.update(" ", "98.765.432/0001-11", "Brasileira", UUID.randomUUID())
		);

		assertEquals("Nome do restaurante e obrigatorio.", exception.getMessage());
	}

	@Test
	@DisplayName("Deve lançar exceção ao atualizar com cnpj em branco")
	void deveLancarExcecaoAoAtualizarComCnpjEmBranco() {
		Restaurant restaurant = new Restaurant(
				UUID.randomUUID(),
				"Pizzaria Italiana",
				"12.345.678/0001-99",
				"Italiana",
				UUID.randomUUID(),
				UUID.randomUUID(),
				OffsetDateTime.now()
		);

		DomainException exception = assertThrows(
				DomainException.class,
				() -> restaurant.update("Churrascaria Gourmet", " ", "Brasileira", UUID.randomUUID())
		);

		assertEquals("CNPJ do restaurante e obrigatorio.", exception.getMessage());
	}

	@Test
	@DisplayName("Deve lançar exceção ao atualizar com tipo em branco")
	void deveLancarExcecaoAoAtualizarComTipoEmBranco() {
		Restaurant restaurant = new Restaurant(
				UUID.randomUUID(),
				"Pizzaria Italiana",
				"12.345.678/0001-99",
				"Italiana",
				UUID.randomUUID(),
				UUID.randomUUID(),
				OffsetDateTime.now()
		);

		DomainException exception = assertThrows(
				DomainException.class,
				() -> restaurant.update("Churrascaria Gourmet", "98.765.432/0001-11", " ", UUID.randomUUID())
		);

		assertEquals("Tipo de cozinha do restaurante e obrigatorio.", exception.getMessage());
	}

	@Test
	@DisplayName("Deve lançar exceção ao atualizar com owner nulo")
	void deveLancarExcecaoAoAtualizarComOwnerNulo() {
		Restaurant restaurant = new Restaurant(
				UUID.randomUUID(),
				"Pizzaria Italiana",
				"12.345.678/0001-99",
				"Italiana",
				UUID.randomUUID(),
				UUID.randomUUID(),
				OffsetDateTime.now()
		);

		NullPointerException exception = assertThrows(
				NullPointerException.class,
				() -> restaurant.update("Churrascaria Gourmet", "98.765.432/0001-11", "Brasileira", null)
		);

		assertEquals("Dono do restaurante e obrigatorio.", exception.getMessage());
	}
}
