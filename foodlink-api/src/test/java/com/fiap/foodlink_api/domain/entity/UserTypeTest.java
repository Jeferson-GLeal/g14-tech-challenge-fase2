package com.fiap.foodlink_api.domain.entity;

import com.fiap.foodlink_api.domain.exception.DomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Tipo de usuario")
class UserTypeTest {

	@Test
	@DisplayName("Deve criar tipo de usuario com nome valido")
	void deveCriarTipoUsuarioComNomeValido() {
		UserType userType = UserType.create("Cliente");

		assertNull(userType.getId());
		assertEquals("Cliente", userType.getName());
	}

	@Test
	@DisplayName("Deve criar tipo de usuario com identificador existente")
	void deveCriarTipoUsuarioComIdentificadorExistente() {
		UUID id = UUID.randomUUID();

		UserType userType = new UserType(id, "Dono de Restaurante");

		assertEquals(id, userType.getId());
		assertEquals("Dono de Restaurante", userType.getName());
	}

	@Test
	@DisplayName("Deve remover espacos extras do nome ao criar tipo de usuario")
	void deveRemoverEspacosExtrasDoNomeAoCriarTipoUsuario() {
		UserType userType = UserType.create("  Cliente  ");

		assertEquals("Cliente", userType.getName());
	}

	@Test
	@DisplayName("Deve renomear tipo de usuario")
	void deveRenomearTipoUsuario() {
		UserType userType = UserType.create("Cliente");

		userType.rename("Dono de Restaurante");

		assertEquals("Dono de Restaurante", userType.getName());
	}

	@Test
	@DisplayName("Deve lancar excecao quando nome for nulo")
	void deveLancarExcecaoQuandoNomeForNulo() {
		DomainException exception = assertThrows(
				DomainException.class,
				() -> UserType.create(null)
		);

		assertEquals("Nome do tipo de usuario e obrigatorio.", exception.getMessage());
	}

	@Test
	@DisplayName("Deve lancar excecao quando nome estiver em branco")
	void deveLancarExcecaoQuandoNomeEstiverEmBranco() {
		DomainException exception = assertThrows(
				DomainException.class,
				() -> UserType.create("   ")
		);

		assertEquals("Nome do tipo de usuario e obrigatorio.", exception.getMessage());
	}

	@Test
	@DisplayName("Deve lancar excecao quando nome exceder tamanho maximo")
	void deveLancarExcecaoQuandoNomeExcederTamanhoMaximo() {
		String name = "a".repeat(256);

		DomainException exception = assertThrows(
				DomainException.class,
				() -> UserType.create(name)
		);

		assertEquals("Nome do tipo de usuario deve ter no maximo 255 caracteres.", exception.getMessage());
	}
}
