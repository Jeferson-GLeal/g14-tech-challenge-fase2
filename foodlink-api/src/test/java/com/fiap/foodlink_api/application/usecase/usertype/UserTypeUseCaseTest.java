package com.fiap.foodlink_api.application.usecase.usertype;

import com.fiap.foodlink_api.domain.entity.UserType;
import com.fiap.foodlink_api.domain.entity.UserTypeCode;
import com.fiap.foodlink_api.domain.exception.DomainException;
import com.fiap.foodlink_api.domain.exception.UserTypeAlreadyExistsException;
import com.fiap.foodlink_api.domain.exception.UserTypeNotFoundException;
import com.fiap.foodlink_api.domain.gateway.UserGateway;
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
		when(gateway.save(any(UserType.class))).thenReturn(new UserType(id, "Cliente", UserTypeCode.CLIENTE));
		CreateUserTypeUseCase useCase = new CreateUserTypeUseCase(gateway);

		UserType userType = useCase.execute("Cliente", UserTypeCode.CLIENTE);

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

		UserType userType = useCase.execute("  Cliente  ", UserTypeCode.CLIENTE);

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
				() -> useCase.execute("Cliente", UserTypeCode.CLIENTE)
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
		UserType savedUserType = new UserType(id, "Cliente", UserTypeCode.CLIENTE);
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
				new UserType(UUID.randomUUID(), "Cliente", UserTypeCode.CLIENTE),
				new UserType(UUID.randomUUID(), "Dono de Restaurante", UserTypeCode.DONO_RESTAURANTE)
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
		UserGateway userGateway = mock(UserGateway.class);
		UUID id = UUID.randomUUID();
		UserType savedUserType = new UserType(id, "Cliente", UserTypeCode.CLIENTE);
		when(gateway.findById(id)).thenReturn(Optional.of(savedUserType));
		when(gateway.findByName("Dono de Restaurante")).thenReturn(Optional.empty());
		when(gateway.save(any(UserType.class))).thenAnswer(invocation -> invocation.getArgument(0));
		UpdateUserTypeUseCase useCase = new UpdateUserTypeUseCase(gateway, userGateway);

		UserType userType = useCase.execute(id, "Dono de Restaurante", UserTypeCode.DONO_RESTAURANTE);

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
		UserGateway userGateway = mock(UserGateway.class);
		UUID id = UUID.randomUUID();
		UserType savedUserType = new UserType(id, "Cliente", UserTypeCode.CLIENTE);
		when(gateway.findById(id)).thenReturn(Optional.of(savedUserType));
		when(gateway.findByName("Dono de Restaurante")).thenReturn(Optional.empty());
		when(gateway.save(any(UserType.class))).thenAnswer(invocation -> invocation.getArgument(0));
		UpdateUserTypeUseCase useCase = new UpdateUserTypeUseCase(gateway, userGateway);
		ArgumentCaptor<UserType> captor = ArgumentCaptor.forClass(UserType.class);

		useCase.execute(id, "Dono de Restaurante", UserTypeCode.DONO_RESTAURANTE);

		verify(gateway).save(captor.capture());
		assertEquals(id, captor.getValue().getId());
		assertEquals("Dono de Restaurante", captor.getValue().getName());
	}

	@Test
	@DisplayName("Deve lancar excecao ao atualizar tipo de usuario inexistente")
	void deveLancarExcecaoAoAtualizarTipoUsuarioInexistente() {
		UserTypeGateway gateway = mock(UserTypeGateway.class);
		UserGateway userGateway = mock(UserGateway.class);
		UUID id = UUID.randomUUID();
		when(gateway.findById(id)).thenReturn(Optional.empty());
		UpdateUserTypeUseCase useCase = new UpdateUserTypeUseCase(gateway, userGateway);

		UserTypeNotFoundException exception = assertThrows(
				UserTypeNotFoundException.class,
				() -> useCase.execute(id, "Cliente", UserTypeCode.CLIENTE)
		);

		assertEquals("Tipo de usuario nao encontrado: " + id, exception.getMessage());
		verify(gateway).findById(id);
		verify(gateway, never()).save(any(UserType.class));
	}

	@Test
	@DisplayName("Deve lancar excecao ao atualizar para nome ja cadastrado em outro tipo")
	void deveLancarExcecaoAoAtualizarParaNomeJaCadastradoEmOutroTipo() {
		UserTypeGateway gateway = mock(UserTypeGateway.class);
		UserGateway userGateway = mock(UserGateway.class);
		UUID clienteId = UUID.randomUUID();
		UUID donoId = UUID.randomUUID();
		UserType cliente = new UserType(clienteId, "Cliente", UserTypeCode.CLIENTE);
		UserType dono = new UserType(donoId, "Dono de Restaurante", UserTypeCode.DONO_RESTAURANTE);
		when(gateway.findById(clienteId)).thenReturn(Optional.of(cliente));
		when(gateway.findByName("Dono de Restaurante")).thenReturn(Optional.of(dono));
		UpdateUserTypeUseCase useCase = new UpdateUserTypeUseCase(gateway, userGateway);

		UserTypeAlreadyExistsException exception = assertThrows(
				UserTypeAlreadyExistsException.class,
				() -> useCase.execute(clienteId, "Dono de Restaurante", UserTypeCode.DONO_RESTAURANTE)
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
		UserGateway userGateway = mock(UserGateway.class);
		UUID id = UUID.randomUUID();
		when(gateway.findById(id)).thenReturn(Optional.of(new UserType(id, "Cliente", UserTypeCode.CLIENTE)));
		DeleteUserTypeUseCase useCase = new DeleteUserTypeUseCase(gateway, userGateway);

		useCase.execute(id);

		verify(gateway).findById(id);
		verify(gateway).deleteById(id);
	}

	@Test
	@DisplayName("Deve lancar excecao ao remover tipo de usuario inexistente")
	void deveLancarExcecaoAoRemoverTipoUsuarioInexistente() {
		UserTypeGateway gateway = mock(UserTypeGateway.class);
		UserGateway userGateway = mock(UserGateway.class);
		UUID id = UUID.randomUUID();
		when(gateway.findById(id)).thenReturn(Optional.empty());
		DeleteUserTypeUseCase useCase = new DeleteUserTypeUseCase(gateway, userGateway);

		UserTypeNotFoundException exception = assertThrows(
				UserTypeNotFoundException.class,
				() -> useCase.execute(id)
		);

		assertEquals("Tipo de usuario nao encontrado: " + id, exception.getMessage());
		verify(gateway).findById(id);
		verify(gateway, never()).deleteById(id);
	}

	@Test
	@DisplayName("Deve alterar DONO_RESTAURANTE para outro codigo sem usuario vinculado")
	void deveAlterarDonoParaOutroCodigoSemUsuarioVinculado() {
		UserTypeGateway gateway = mock(UserTypeGateway.class);
		UserGateway userGateway = mock(UserGateway.class);
		UUID userTypeId = UUID.randomUUID();
		UserType dono = new UserType(userTypeId, "Dono de Restaurante", UserTypeCode.DONO_RESTAURANTE);
		when(gateway.findById(userTypeId)).thenReturn(Optional.of(dono));
		when(gateway.findByName("Cliente")).thenReturn(Optional.empty());
		when(userGateway.existsByUserTypeId(userTypeId)).thenReturn(false);
		when(gateway.save(any(UserType.class))).thenAnswer(invocation -> invocation.getArgument(0));
		UpdateUserTypeUseCase useCase = new UpdateUserTypeUseCase(gateway, userGateway);

		UserType userType = useCase.execute(userTypeId, "Cliente", UserTypeCode.CLIENTE);

		assertEquals(UserTypeCode.CLIENTE, userType.getCode());
		assertEquals("Cliente", userType.getName());
		verify(userGateway).existsByUserTypeId(userTypeId);
		verify(gateway).save(any(UserType.class));
	}

	@Test
	@DisplayName("Deve bloquear alteracao de DONO_RESTAURANTE com usuario vinculado")
	void deveBloquearAlteracaoDeDonoComUsuarioVinculado() {
		UserTypeGateway gateway = mock(UserTypeGateway.class);
		UserGateway userGateway = mock(UserGateway.class);
		UUID userTypeId = UUID.randomUUID();
		UserType dono = new UserType(userTypeId, "Dono de Restaurante", UserTypeCode.DONO_RESTAURANTE);
		when(gateway.findById(userTypeId)).thenReturn(Optional.of(dono));
		when(userGateway.existsByUserTypeId(userTypeId)).thenReturn(true);
		UpdateUserTypeUseCase useCase = new UpdateUserTypeUseCase(gateway, userGateway);

		DomainException exception = assertThrows(
				DomainException.class,
				() -> useCase.execute(userTypeId, "Cliente", UserTypeCode.CLIENTE)
		);

		assertEquals("Tipo de usuário DONO_RESTAURANTE nao pode ser alterado porque existem usuários vinculados a esse tipo.", exception.getMessage());
		verify(userGateway).existsByUserTypeId(userTypeId);
		verify(gateway, never()).save(any(UserType.class));
	}

	@Test
	@DisplayName("Deve remover DONO_RESTAURANTE sem usuario vinculado")
	void deveRemoverDonoSemUsuarioVinculado() {
		UserTypeGateway gateway = mock(UserTypeGateway.class);
		UserGateway userGateway = mock(UserGateway.class);
		UUID userTypeId = UUID.randomUUID();
		UserType dono = new UserType(userTypeId, "Dono de Restaurante", UserTypeCode.DONO_RESTAURANTE);
		when(gateway.findById(userTypeId)).thenReturn(Optional.of(dono));
		when(userGateway.existsByUserTypeId(userTypeId)).thenReturn(false);
		DeleteUserTypeUseCase useCase = new DeleteUserTypeUseCase(gateway, userGateway);

		useCase.execute(userTypeId);

		verify(userGateway).existsByUserTypeId(userTypeId);
		verify(gateway).deleteById(userTypeId);
	}

	@Test
	@DisplayName("Deve bloquear remocao de DONO_RESTAURANTE com usuario vinculado")
	void deveBloquearRemocaoDeDonoComUsuarioVinculado() {
		UserTypeGateway gateway = mock(UserTypeGateway.class);
		UserGateway userGateway = mock(UserGateway.class);
		UUID userTypeId = UUID.randomUUID();
		UserType dono = new UserType(userTypeId, "Dono de Restaurante", UserTypeCode.DONO_RESTAURANTE);
		when(gateway.findById(userTypeId)).thenReturn(Optional.of(dono));
		when(userGateway.existsByUserTypeId(userTypeId)).thenReturn(true);
		DeleteUserTypeUseCase useCase = new DeleteUserTypeUseCase(gateway, userGateway);

		DomainException exception = assertThrows(
				DomainException.class,
				() -> useCase.execute(userTypeId)
		);

		assertEquals("Tipo de usuário DONO_RESTAURANTE nao pode ser removido porque existem usuários vinculados a esse tipo.", exception.getMessage());
		verify(gateway, never()).deleteById(userTypeId);
	}
}
