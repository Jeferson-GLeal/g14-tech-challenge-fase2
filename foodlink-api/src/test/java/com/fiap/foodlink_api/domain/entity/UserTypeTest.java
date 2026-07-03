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
		UserType userType = UserType.create("Cliente", UserTypeCode.CLIENTE);

		assertNull(userType.getId());
		assertEquals("Cliente", userType.getName());
		assertEquals(UserTypeCode.CLIENTE, userType.getCode());
	}

	@Test
	@DisplayName("Deve criar tipo de usuario com identificador existente")
	void deveCriarTipoUsuarioComIdentificadorExistente() {
		UUID id = UUID.randomUUID();

		UserType userType = new UserType(id, "Dono de Restaurante", UserTypeCode.DONO_RESTAURANTE);

		assertEquals(id, userType.getId());
		assertEquals("Dono de Restaurante", userType.getName());
		assertEquals(UserTypeCode.DONO_RESTAURANTE, userType.getCode());
	}

	@Test
	@DisplayName("Deve remover espacos extras do nome ao criar tipo de usuario")
	void deveRemoverEspacosExtrasDoNomeAoCriarTipoUsuario() {
		UserType userType = UserType.create("  Cliente  ", UserTypeCode.CLIENTE);

		assertEquals("Cliente", userType.getName());
	}

	@Test
	@DisplayName("Deve renomear tipo de usuario")
	void deveRenomearTipoUsuario() {
		UserType userType = UserType.create("Cliente", UserTypeCode.CLIENTE);

		userType.rename("Dono de Restaurante");

		assertEquals("Dono de Restaurante", userType.getName());
	}

	@Test
	@DisplayName("Deve lancar excecao quando nome for nulo")
	void deveLancarExcecaoQuandoNomeForNulo() {
		DomainException exception = assertThrows(
				DomainException.class,
				() -> UserType.create(null, UserTypeCode.CLIENTE)
		);

		assertEquals("Nome do tipo de usuario e obrigatorio.", exception.getMessage());
	}

	@Test
	@DisplayName("Deve lancar excecao quando nome estiver em branco")
	void deveLancarExcecaoQuandoNomeEstiverEmBranco() {
		DomainException exception = assertThrows(
				DomainException.class,
				() -> UserType.create("   ", UserTypeCode.CLIENTE)
		);

		assertEquals("Nome do tipo de usuario e obrigatorio.", exception.getMessage());
	}

	@Test
	@DisplayName("Deve lancar excecao quando nome exceder tamanho maximo")
	void deveLancarExcecaoQuandoNomeExcederTamanhoMaximo() {
		String name = "a".repeat(256);

		DomainException exception = assertThrows(
				DomainException.class,
				() -> UserType.create(name, UserTypeCode.CLIENTE)
		);

		assertEquals("Nome do tipo de usuario deve ter no maximo 255 caracteres.", exception.getMessage());
	}

	@Test
	@DisplayName("Deve lancar excecao quando codigo for nulo")
	void deveLancarExcecaoQuandoCodigoForNulo() {
		assertThrows(
				NullPointerException.class,
				() -> UserType.create("Cliente", null)
		);
	}

	@Test
	@DisplayName("Deve identificar tipo de usuario dono de restaurante")
	void deveIdentificarTipoUsuarioDonoDeRestaurante() {
		UserType dono = UserType.create("Dono de Restaurante", UserTypeCode.DONO_RESTAURANTE);
		UserType cliente = UserType.create("Cliente", UserTypeCode.CLIENTE);

		assertEquals(true, dono.isDono());
		assertEquals(false, cliente.isDono());
	}

	@Test
	@DisplayName("Deve lancar excecao ao exigir dono quando tipo nao for dono")
	void deveLancarExcecaoAoExigirDonoQuandoTipoNaoForDono() {
		UserType cliente = UserType.create("Cliente", UserTypeCode.CLIENTE);

		DomainException exception = assertThrows(
				DomainException.class,
				cliente::requireDono
		);

		assertEquals("Usuario nao e dono do restaurante.", exception.getMessage());
	}
}
