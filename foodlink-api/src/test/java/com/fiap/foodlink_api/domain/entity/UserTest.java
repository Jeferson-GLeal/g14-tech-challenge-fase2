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

@DisplayName("Usuario")
class UserTest {

	@Test
	@DisplayName("Deve criar usuario com dados validos")
	void deveCriarUsuarioComDadosValidos() {
		UUID userTypeId = UUID.randomUUID();
		UUID addressId = UUID.randomUUID();

		User user = User.create(
				"Joao Silva",
				"JOAO@EMAIL.COM",
				"joao",
				"senha123",
				userTypeId,
				addressId
		);

		assertNull(user.getId());
		assertEquals("Joao Silva", user.getName());
		assertEquals("joao@email.com", user.getEmail());
		assertEquals("joao", user.getLogin());
		assertEquals("senha123", user.getPassword());
		assertEquals(userTypeId, user.getUserTypeId());
		assertEquals(addressId, user.getAddressId());
		assertNotNull(user.getLastUpdatedAt());
	}

	@Test
	@DisplayName("Deve criar usuario com identificador existente")
	void deveCriarUsuarioComIdentificadorExistente() {
		UUID id = UUID.randomUUID();
		UUID userTypeId = UUID.randomUUID();
		UUID addressId = UUID.randomUUID();
		OffsetDateTime lastUpdatedAt = OffsetDateTime.parse("2020-01-01T10:00:00-03:00");

		User user = new User(
				id,
				"Joao Silva",
				"joao@email.com",
				"joao",
				"senha123",
				userTypeId,
				addressId,
				lastUpdatedAt
		);

		assertEquals(id, user.getId());
		assertEquals(lastUpdatedAt, user.getLastUpdatedAt());
	}

	@Test
	@DisplayName("Deve normalizar campos de texto do usuario")
	void deveNormalizarCamposTextoDoUsuario() {
		User user = User.create(
				"  Joao Silva  ",
				"  JOAO@EMAIL.COM  ",
				"  joao  ",
				"  senha123  ",
				UUID.randomUUID(),
				UUID.randomUUID()
		);

		assertEquals("Joao Silva", user.getName());
		assertEquals("joao@email.com", user.getEmail());
		assertEquals("joao", user.getLogin());
		assertEquals("senha123", user.getPassword());
	}

	@Test
	@DisplayName("Deve atualizar perfil do usuario")
	void deveAtualizarPerfilDoUsuario() {
		OffsetDateTime lastUpdatedAt = OffsetDateTime.parse("2020-01-01T10:00:00-03:00");
		UUID oldUserTypeId = UUID.randomUUID();
		UUID oldAddressId = UUID.randomUUID();
		UUID newUserTypeId = UUID.randomUUID();
		UUID newAddressId = UUID.randomUUID();
		User user = new User(
				UUID.randomUUID(),
				"Joao Silva",
				"joao@email.com",
				"joao",
				"senha123",
				oldUserTypeId,
				oldAddressId,
				lastUpdatedAt
		);

		user.updateProfile("Maria Silva", "MARIA@EMAIL.COM", "maria", newUserTypeId, newAddressId);

		assertEquals("Maria Silva", user.getName());
		assertEquals("maria@email.com", user.getEmail());
		assertEquals("maria", user.getLogin());
		assertEquals(newUserTypeId, user.getUserTypeId());
		assertEquals(newAddressId, user.getAddressId());
		assertTrue(user.getLastUpdatedAt().isAfter(lastUpdatedAt));
	}

	@Test
	@DisplayName("Deve alterar senha do usuario")
	void deveAlterarSenhaDoUsuario() {
		OffsetDateTime lastUpdatedAt = OffsetDateTime.parse("2020-01-01T10:00:00-03:00");
		User user = new User(
				UUID.randomUUID(),
				"Joao Silva",
				"joao@email.com",
				"joao",
				"senha123",
				UUID.randomUUID(),
				UUID.randomUUID(),
				lastUpdatedAt
		);

		user.changePassword("novaSenha123");

		assertEquals("novaSenha123", user.getPassword());
		assertTrue(user.getLastUpdatedAt().isAfter(lastUpdatedAt));
	}

	@Test
	@DisplayName("Deve lancar excecao quando nome estiver em branco")
	void deveLancarExcecaoQuandoNomeEstiverEmBranco() {
		DomainException exception = assertThrows(
				DomainException.class,
				() -> User.create(" ", "joao@email.com", "joao", "senha123", UUID.randomUUID(), UUID.randomUUID())
		);

		assertEquals("Nome do usuario e obrigatorio.", exception.getMessage());
	}

	@Test
	@DisplayName("Deve lancar excecao quando email estiver em branco")
	void deveLancarExcecaoQuandoEmailEstiverEmBranco() {
		DomainException exception = assertThrows(
				DomainException.class,
				() -> User.create("Joao Silva", " ", "joao", "senha123", UUID.randomUUID(), UUID.randomUUID())
		);

		assertEquals("Email do usuario e obrigatorio.", exception.getMessage());
	}

	@Test
	@DisplayName("Deve lancar excecao quando email for invalido")
	void deveLancarExcecaoQuandoEmailForInvalido() {
		DomainException exception = assertThrows(
				DomainException.class,
				() -> User.create("Joao Silva", "joao.email.com", "joao", "senha123", UUID.randomUUID(), UUID.randomUUID())
		);

		assertEquals("Email do usuario deve ser valido.", exception.getMessage());
	}

	@Test
	@DisplayName("Deve lancar excecao quando login estiver em branco")
	void deveLancarExcecaoQuandoLoginEstiverEmBranco() {
		DomainException exception = assertThrows(
				DomainException.class,
				() -> User.create("Joao Silva", "joao@email.com", " ", "senha123", UUID.randomUUID(), UUID.randomUUID())
		);

		assertEquals("Login do usuario e obrigatorio.", exception.getMessage());
	}

	@Test
	@DisplayName("Deve lancar excecao quando senha estiver em branco")
	void deveLancarExcecaoQuandoSenhaEstiverEmBranco() {
		DomainException exception = assertThrows(
				DomainException.class,
				() -> User.create("Joao Silva", "joao@email.com", "joao", " ", UUID.randomUUID(), UUID.randomUUID())
		);

		assertEquals("Senha do usuario e obrigatoria.", exception.getMessage());
	}

	@Test
	@DisplayName("Deve lancar excecao quando tipo de usuario for nulo")
	void deveLancarExcecaoQuandoTipoUsuarioForNulo() {
		NullPointerException exception = assertThrows(
				NullPointerException.class,
				() -> User.create("Joao Silva", "joao@email.com", "joao", "senha123", null, UUID.randomUUID())
		);

		assertEquals("Tipo de usuario e obrigatorio.", exception.getMessage());
	}

	@Test
	@DisplayName("Deve lancar excecao quando endereco for nulo")
	void deveLancarExcecaoQuandoEnderecoForNulo() {
		NullPointerException exception = assertThrows(
				NullPointerException.class,
				() -> User.create("Joao Silva", "joao@email.com", "joao", "senha123", UUID.randomUUID(), null)
		);

		assertEquals("Endereco do usuario e obrigatorio.", exception.getMessage());
	}

	@Test
	@DisplayName("Deve lancar excecao quando campo exceder tamanho maximo")
	void deveLancarExcecaoQuandoCampoExcederTamanhoMaximo() {
		String name = "a".repeat(256);

		DomainException exception = assertThrows(
				DomainException.class,
				() -> User.create(name, "joao@email.com", "joao", "senha123", UUID.randomUUID(), UUID.randomUUID())
		);

		assertEquals("Campo de usuario deve ter no maximo 255 caracteres.", exception.getMessage());
	}
}
