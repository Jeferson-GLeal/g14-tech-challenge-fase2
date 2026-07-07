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

@DisplayName("Endereco")
class AddressTest {

	@Test
	@DisplayName("Deve criar endereco com dados validos")
	void deveCriarEnderecoComDadosValidos() {
		Address address = Address.create(
				"Rua das Flores",
				"123",
				"Apto 45",
				"Centro",
				"Sao Paulo",
				"sp",
				"01001000"
		);

		assertNull(address.getId());
		assertEquals("Rua das Flores", address.getStreet());
		assertEquals("123", address.getNumber());
		assertEquals("Apto 45", address.getComplement());
		assertEquals("Centro", address.getDistrict());
		assertEquals("Sao Paulo", address.getCity());
		assertEquals("SP", address.getState());
		assertEquals("01001000", address.getZipCode());
		assertNotNull(address.getLastUpdatedAt());
	}

	@Test
	@DisplayName("Deve criar endereco com identificador existente")
	void deveCriarEnderecoComIdentificadorExistente() {
		UUID id = UUID.randomUUID();
		OffsetDateTime lastUpdatedAt = OffsetDateTime.parse("2020-01-01T10:00:00-03:00");

		Address address = new Address(
				id,
				"Rua das Flores",
				"123",
				null,
				"Centro",
				"Sao Paulo",
				"SP",
				"01001000",
				lastUpdatedAt
		);

		assertEquals(id, address.getId());
		assertEquals(lastUpdatedAt, address.getLastUpdatedAt());
	}

	@Test
	@DisplayName("Deve normalizar campos de texto do endereco")
	void deveNormalizarCamposTextoDoEndereco() {
		Address address = Address.create(
				"  Rua das Flores  ",
				"  123  ",
				"  Apto 45  ",
				"  Centro  ",
				"  Sao Paulo  ",
				"  sp  ",
				"  01001000  "
		);

		assertEquals("Rua das Flores", address.getStreet());
		assertEquals("123", address.getNumber());
		assertEquals("Apto 45", address.getComplement());
		assertEquals("Centro", address.getDistrict());
		assertEquals("Sao Paulo", address.getCity());
		assertEquals("SP", address.getState());
		assertEquals("01001000", address.getZipCode());
	}

	@Test
	@DisplayName("Deve manter complemento nulo quando estiver em branco")
	void deveManterComplementoNuloQuandoEstiverEmBranco() {
		Address address = Address.create(
				"Rua das Flores",
				"123",
				"   ",
				"Centro",
				"Sao Paulo",
				"SP",
				"01001000"
		);

		assertNull(address.getComplement());
	}

	@Test
	@DisplayName("Deve atualizar endereco")
	void deveAtualizarEndereco() {
		OffsetDateTime lastUpdatedAt = OffsetDateTime.parse("2020-01-01T10:00:00-03:00");
		Address address = new Address(
				UUID.randomUUID(),
				"Rua das Flores",
				"123",
				null,
				"Centro",
				"Sao Paulo",
				"SP",
				"01001000",
				lastUpdatedAt
		);

		address.update("Avenida Brasil", "500", "Sala 10", "Jardins", "Rio de Janeiro", "rj", "20000000");

		assertEquals("Avenida Brasil", address.getStreet());
		assertEquals("500", address.getNumber());
		assertEquals("Sala 10", address.getComplement());
		assertEquals("Jardins", address.getDistrict());
		assertEquals("Rio de Janeiro", address.getCity());
		assertEquals("RJ", address.getState());
		assertEquals("20000000", address.getZipCode());
		assertTrue(address.getLastUpdatedAt().isAfter(lastUpdatedAt));
	}

	@Test
	@DisplayName("Deve lancar excecao quando logradouro estiver em branco")
	void deveLancarExcecaoQuandoLogradouroEstiverEmBranco() {
		DomainException exception = assertThrows(
				DomainException.class,
				() -> Address.create(" ", "123", null, "Centro", "Sao Paulo", "SP", "01001000")
		);

		assertEquals("Logradouro do endereco e obrigatório.", exception.getMessage());
	}

	@Test
	@DisplayName("Deve lancar excecao quando numero estiver em branco")
	void deveLancarExcecaoQuandoNumeroEstiverEmBranco() {
		DomainException exception = assertThrows(
				DomainException.class,
				() -> Address.create("Rua das Flores", " ", null, "Centro", "Sao Paulo", "SP", "01001000")
		);

		assertEquals("Numero do endereco e obrigatório.", exception.getMessage());
	}

	@Test
	@DisplayName("Deve lancar excecao quando bairro estiver em branco")
	void deveLancarExcecaoQuandoBairroEstiverEmBranco() {
		DomainException exception = assertThrows(
				DomainException.class,
				() -> Address.create("Rua das Flores", "123", null, " ", "Sao Paulo", "SP", "01001000")
		);

		assertEquals("Bairro do endereco e obrigatório.", exception.getMessage());
	}

	@Test
	@DisplayName("Deve lancar excecao quando cidade estiver em branco")
	void deveLancarExcecaoQuandoCidadeEstiverEmBranco() {
		DomainException exception = assertThrows(
				DomainException.class,
				() -> Address.create("Rua das Flores", "123", null, "Centro", " ", "SP", "01001000")
		);

		assertEquals("Cidade do endereco e obrigatória.", exception.getMessage());
	}

	@Test
	@DisplayName("Deve lancar excecao quando uf estiver em branco")
	void deveLancarExcecaoQuandoUfEstiverEmBranco() {
		DomainException exception = assertThrows(
				DomainException.class,
				() -> Address.create("Rua das Flores", "123", null, "Centro", "Sao Paulo", " ", "01001000")
		);

		assertEquals("UF do endereco e obrigatoria.", exception.getMessage());
	}

	@Test
	@DisplayName("Deve lancar excecao quando cep estiver em branco")
	void deveLancarExcecaoQuandoCepEstiverEmBranco() {
		DomainException exception = assertThrows(
				DomainException.class,
				() -> Address.create("Rua das Flores", "123", null, "Centro", "Sao Paulo", "SP", " ")
		);

		assertEquals("CEP do endereco e obrigatório.", exception.getMessage());
	}

	@Test
	@DisplayName("Deve lancar excecao quando campo exceder tamanho maximo")
	void deveLancarExcecaoQuandoCampoExcederTamanhoMaximo() {
		String street = "a".repeat(256);

		DomainException exception = assertThrows(
				DomainException.class,
				() -> Address.create(street, "123", null, "Centro", "Sao Paulo", "SP", "01001000")
		);

		assertEquals("Campo de endereco deve ter no máximo 255 caracteres.", exception.getMessage());
	}
}
