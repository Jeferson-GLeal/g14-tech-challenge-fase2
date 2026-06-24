package com.fiap.foodlink_api.application.usecase.usertype;

import com.fiap.foodlink_api.domain.entity.UserType;
import com.fiap.foodlink_api.domain.exception.UserTypeAlreadyExistsException;
import com.fiap.foodlink_api.domain.exception.UserTypeNotFoundException;
import com.fiap.foodlink_api.domain.gateway.UserTypeGateway;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Casos de uso de tipo de usuario")
class UserTypeUseCaseTest {

	@Test
	@DisplayName("Deve criar tipo de usuario quando nome nao estiver cadastrado")
	void deveCriarTipoUsuarioQuandoNomeNaoEstiverCadastrado() {
		UserTypeGateway gateway = mock(UserTypeGateway.class);
		UUID id = UUID.randomUUID();
		when(gateway.existsByName("Cliente")).thenReturn(false);
		when(gateway.save(any(UserType.class))).thenReturn(new UserType(id, "Cliente"));
		CreateUserTypeUseCase useCase = new CreateUserTypeUseCase(gateway);

		UserType userType = useCase.execute("Cliente");

		assertEquals(id, userType.getId());
		assertEquals("Cliente", userType.getName());
		verify(gateway).existsByName("Cliente");
		verify(gateway).save(any(UserType.class));
	}

	@Test
	@DisplayName("Deve normalizar nome antes de criar tipo de usuario")
	void deveNormalizarNomeAntesDeCriarTipoUsuario() {
		UserTypeGateway gateway = mock(UserTypeGateway.class);
		when(gateway.existsByName("Cliente")).thenReturn(false);
		when(gateway.save(any(UserType.class))).thenAnswer(invocation -> invocation.getArgument(0));
		CreateUserTypeUseCase useCase = new CreateUserTypeUseCase(gateway);

		UserType userType = useCase.execute("  Cliente  ");

		assertNull(userType.getId());
		assertEquals("Cliente", userType.getName());
		verify(gateway).existsByName("Cliente");
	}

	@Test
	@DisplayName("Deve lancar excecao ao criar tipo de usuario com nome ja cadastrado")
	void deveLancarExcecaoAoCriarTipoUsuarioComNomeJaCadastrado() {
		UserTypeGateway gateway = mock(UserTypeGateway.class);
		when(gateway.existsByName("Cliente")).thenReturn(true);
		CreateUserTypeUseCase useCase = new CreateUserTypeUseCase(gateway);

		UserTypeAlreadyExistsException exception = assertThrows(
				UserTypeAlreadyExistsException.class,
				() -> useCase.execute("Cliente")
		);

		assertEquals("Tipo de usuario ja cadastrado: Cliente", exception.getMessage());
		verify(gateway).existsByName("Cliente");
		verify(gateway, never()).save(any(UserType.class));
	}

	@Test
	@DisplayName("Deve buscar tipo de usuario por identificador")
	void deveBuscarTipoUsuarioPorIdentificador() {
		UserTypeGateway gateway = mock(UserTypeGateway.class);
		UUID id = UUID.randomUUID();
		UserType savedUserType = new UserType(id, "Cliente");
		when(gateway.findById(id)).thenReturn(Optional.of(savedUserType));
		GetUserTypeByIdUseCase useCase = new GetUserTypeByIdUseCase(gateway);

		UserType userType = useCase.execute(id);

		assertEquals(id, userType.getId());
		assertEquals("Cliente", userType.getName());
		verify(gateway).findById(id);
	}

	@Test
	@DisplayName("Deve lancar excecao ao buscar tipo de usuario inexistente")
	void deveLancarExcecaoAoBuscarTipoUsuarioInexistente() {
		UserTypeGateway gateway = mock(UserTypeGateway.class);
		UUID id = UUID.randomUUID();
		when(gateway.findById(id)).thenReturn(Optional.empty());
		GetUserTypeByIdUseCase useCase = new GetUserTypeByIdUseCase(gateway);

		UserTypeNotFoundException exception = assertThrows(
				UserTypeNotFoundException.class,
				() -> useCase.execute(id)
		);

		assertEquals("Tipo de usuario nao encontrado: " + id, exception.getMessage());
		verify(gateway).findById(id);
	}

	@Test
	@DisplayName("Deve listar tipos de usuario cadastrados")
	void deveListarTiposUsuarioCadastrados() {
		UserTypeGateway gateway = mock(UserTypeGateway.class);
		List<UserType> savedUserTypes = List.of(
				new UserType(UUID.randomUUID(), "Cliente"),
				new UserType(UUID.randomUUID(), "Dono de Restaurante")
		);
		when(gateway.findAll()).thenReturn(savedUserTypes);
		ListUserTypesUseCase useCase = new ListUserTypesUseCase(gateway);

		List<UserType> userTypes = useCase.execute();

		assertEquals(2, userTypes.size());
		assertEquals("Cliente", userTypes.get(0).getName());
		assertEquals("Dono de Restaurante", userTypes.get(1).getName());
		verify(gateway).findAll();
	}

	@Test
	@DisplayName("Deve atualizar nome do tipo de usuario")
	void deveAtualizarNomeDoTipoUsuario() {
		UserTypeGateway gateway = mock(UserTypeGateway.class);
		UUID id = UUID.randomUUID();
		UserType savedUserType = new UserType(id, "Cliente");
		when(gateway.findById(id)).thenReturn(Optional.of(savedUserType));
		when(gateway.findByName("Dono de Restaurante")).thenReturn(Optional.empty());
		when(gateway.save(any(UserType.class))).thenAnswer(invocation -> invocation.getArgument(0));
		UpdateUserTypeUseCase useCase = new UpdateUserTypeUseCase(gateway);

		UserType userType = useCase.execute(id, "Dono de Restaurante");

		assertEquals(id, userType.getId());
		assertEquals("Dono de Restaurante", userType.getName());
		verify(gateway).findById(id);
		verify(gateway).findByName("Dono de Restaurante");
		verify(gateway).save(any(UserType.class));
	}

	@Test
	@DisplayName("Deve enviar tipo de usuario atualizado para persistencia")
	void deveEnviarTipoUsuarioAtualizadoParaPersistencia() {
		UserTypeGateway gateway = mock(UserTypeGateway.class);
		UUID id = UUID.randomUUID();
		UserType savedUserType = new UserType(id, "Cliente");
		when(gateway.findById(id)).thenReturn(Optional.of(savedUserType));
		when(gateway.findByName("Dono de Restaurante")).thenReturn(Optional.empty());
		when(gateway.save(any(UserType.class))).thenAnswer(invocation -> invocation.getArgument(0));
		UpdateUserTypeUseCase useCase = new UpdateUserTypeUseCase(gateway);
		ArgumentCaptor<UserType> captor = ArgumentCaptor.forClass(UserType.class);

		useCase.execute(id, "Dono de Restaurante");

		verify(gateway).save(captor.capture());
		assertEquals(id, captor.getValue().getId());
		assertEquals("Dono de Restaurante", captor.getValue().getName());
	}

	@Test
	@DisplayName("Deve lancar excecao ao atualizar tipo de usuario inexistente")
	void deveLancarExcecaoAoAtualizarTipoUsuarioInexistente() {
		UserTypeGateway gateway = mock(UserTypeGateway.class);
		UUID id = UUID.randomUUID();
		when(gateway.findById(id)).thenReturn(Optional.empty());
		UpdateUserTypeUseCase useCase = new UpdateUserTypeUseCase(gateway);

		UserTypeNotFoundException exception = assertThrows(
				UserTypeNotFoundException.class,
				() -> useCase.execute(id, "Cliente")
		);

		assertEquals("Tipo de usuario nao encontrado: " + id, exception.getMessage());
		verify(gateway).findById(id);
		verify(gateway, never()).save(any(UserType.class));
	}

	@Test
	@DisplayName("Deve lancar excecao ao atualizar para nome ja cadastrado em outro tipo")
	void deveLancarExcecaoAoAtualizarParaNomeJaCadastradoEmOutroTipo() {
		UserTypeGateway gateway = mock(UserTypeGateway.class);
		UUID clienteId = UUID.randomUUID();
		UUID donoId = UUID.randomUUID();
		UserType cliente = new UserType(clienteId, "Cliente");
		UserType dono = new UserType(donoId, "Dono de Restaurante");
		when(gateway.findById(clienteId)).thenReturn(Optional.of(cliente));
		when(gateway.findByName("Dono de Restaurante")).thenReturn(Optional.of(dono));
		UpdateUserTypeUseCase useCase = new UpdateUserTypeUseCase(gateway);

		UserTypeAlreadyExistsException exception = assertThrows(
				UserTypeAlreadyExistsException.class,
				() -> useCase.execute(clienteId, "Dono de Restaurante")
		);

		assertEquals("Tipo de usuario ja cadastrado: Dono de Restaurante", exception.getMessage());
		verify(gateway).findById(clienteId);
		verify(gateway).findByName("Dono de Restaurante");
		verify(gateway, never()).save(any(UserType.class));
	}

	@Test
	@DisplayName("Deve remover tipo de usuario cadastrado")
	void deveRemoverTipoUsuarioCadastrado() {
		UserTypeGateway gateway = mock(UserTypeGateway.class);
		UUID id = UUID.randomUUID();
		when(gateway.existsById(id)).thenReturn(true);
		DeleteUserTypeUseCase useCase = new DeleteUserTypeUseCase(gateway);

		useCase.execute(id);

		verify(gateway).existsById(id);
		verify(gateway).deleteById(id);
	}

	@Test
	@DisplayName("Deve lancar excecao ao remover tipo de usuario inexistente")
	void deveLancarExcecaoAoRemoverTipoUsuarioInexistente() {
		UserTypeGateway gateway = mock(UserTypeGateway.class);
		UUID id = UUID.randomUUID();
		when(gateway.existsById(id)).thenReturn(false);
		DeleteUserTypeUseCase useCase = new DeleteUserTypeUseCase(gateway);

		UserTypeNotFoundException exception = assertThrows(
				UserTypeNotFoundException.class,
				() -> useCase.execute(id)
		);

		assertEquals("Tipo de usuario nao encontrado: " + id, exception.getMessage());
		verify(gateway).existsById(id);
		verify(gateway, never()).deleteById(id);
	}
}
