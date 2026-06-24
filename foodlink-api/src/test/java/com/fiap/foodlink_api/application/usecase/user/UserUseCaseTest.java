package com.fiap.foodlink_api.application.usecase.user;

import com.fiap.foodlink_api.domain.entity.User;
import com.fiap.foodlink_api.domain.exception.AddressNotFoundException;
import com.fiap.foodlink_api.domain.exception.UserAlreadyExistsException;
import com.fiap.foodlink_api.domain.exception.UserNotFoundException;
import com.fiap.foodlink_api.domain.exception.UserTypeNotFoundException;
import com.fiap.foodlink_api.domain.gateway.AddressGateway;
import com.fiap.foodlink_api.domain.gateway.UserGateway;
import com.fiap.foodlink_api.domain.gateway.UserTypeGateway;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

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

@DisplayName("Casos de uso de usuario")
class UserUseCaseTest {

	@Test
	@DisplayName("Deve criar usuario")
	void deveCriarUsuario() {
		UserGateway userGateway = mock(UserGateway.class);
		UserTypeGateway userTypeGateway = mock(UserTypeGateway.class);
		AddressGateway addressGateway = mock(AddressGateway.class);
		UUID id = UUID.randomUUID();
		UUID userTypeId = UUID.randomUUID();
		UUID addressId = UUID.randomUUID();
		when(userGateway.existsByEmail("joao@email.com")).thenReturn(false);
		when(userGateway.existsByLogin("joao")).thenReturn(false);
		when(userTypeGateway.existsById(userTypeId)).thenReturn(true);
		when(addressGateway.existsById(addressId)).thenReturn(true);
		when(userGateway.save(any(User.class))).thenReturn(criarUsuario(id, userTypeId, addressId));
		CreateUserUseCase useCase = new CreateUserUseCase(userGateway, userTypeGateway, addressGateway);

		User user = useCase.execute("Joao Silva", "JOAO@EMAIL.COM", "joao", "senha123", userTypeId, addressId);

		assertEquals(id, user.getId());
		assertEquals("joao@email.com", user.getEmail());
		verify(userGateway).existsByEmail("joao@email.com");
		verify(userGateway).existsByLogin("joao");
		verify(userTypeGateway).existsById(userTypeId);
		verify(addressGateway).existsById(addressId);
		verify(userGateway).save(any(User.class));
	}

	@Test
	@DisplayName("Deve lancar excecao ao criar usuario com email ja cadastrado")
	void deveLancarExcecaoAoCriarUsuarioComEmailJaCadastrado() {
		UserGateway userGateway = mock(UserGateway.class);
		UserTypeGateway userTypeGateway = mock(UserTypeGateway.class);
		AddressGateway addressGateway = mock(AddressGateway.class);
		UUID userTypeId = UUID.randomUUID();
		UUID addressId = UUID.randomUUID();
		when(userGateway.existsByEmail("joao@email.com")).thenReturn(true);
		CreateUserUseCase useCase = new CreateUserUseCase(userGateway, userTypeGateway, addressGateway);

		UserAlreadyExistsException exception = assertThrows(
				UserAlreadyExistsException.class,
				() -> useCase.execute("Joao Silva", "JOAO@EMAIL.COM", "joao", "senha123", userTypeId, addressId)
		);

		assertEquals("Usuario ja cadastrado com email: joao@email.com", exception.getMessage());
		verify(userGateway, never()).save(any(User.class));
	}

	@Test
	@DisplayName("Deve lancar excecao ao criar usuario com login ja cadastrado")
	void deveLancarExcecaoAoCriarUsuarioComLoginJaCadastrado() {
		UserGateway userGateway = mock(UserGateway.class);
		UserTypeGateway userTypeGateway = mock(UserTypeGateway.class);
		AddressGateway addressGateway = mock(AddressGateway.class);
		UUID userTypeId = UUID.randomUUID();
		UUID addressId = UUID.randomUUID();
		when(userGateway.existsByEmail("joao@email.com")).thenReturn(false);
		when(userGateway.existsByLogin("joao")).thenReturn(true);
		CreateUserUseCase useCase = new CreateUserUseCase(userGateway, userTypeGateway, addressGateway);

		UserAlreadyExistsException exception = assertThrows(
				UserAlreadyExistsException.class,
				() -> useCase.execute("Joao Silva", "joao@email.com", "joao", "senha123", userTypeId, addressId)
		);

		assertEquals("Usuario ja cadastrado com login: joao", exception.getMessage());
		verify(userGateway, never()).save(any(User.class));
	}

	@Test
	@DisplayName("Deve lancar excecao ao criar usuario com tipo inexistente")
	void deveLancarExcecaoAoCriarUsuarioComTipoInexistente() {
		UserGateway userGateway = mock(UserGateway.class);
		UserTypeGateway userTypeGateway = mock(UserTypeGateway.class);
		AddressGateway addressGateway = mock(AddressGateway.class);
		UUID userTypeId = UUID.randomUUID();
		UUID addressId = UUID.randomUUID();
		when(userGateway.existsByEmail("joao@email.com")).thenReturn(false);
		when(userGateway.existsByLogin("joao")).thenReturn(false);
		when(userTypeGateway.existsById(userTypeId)).thenReturn(false);
		CreateUserUseCase useCase = new CreateUserUseCase(userGateway, userTypeGateway, addressGateway);

		UserTypeNotFoundException exception = assertThrows(
				UserTypeNotFoundException.class,
				() -> useCase.execute("Joao Silva", "joao@email.com", "joao", "senha123", userTypeId, addressId)
		);

		assertEquals("Tipo de usuario nao encontrado: " + userTypeId, exception.getMessage());
		verify(userGateway, never()).save(any(User.class));
	}

	@Test
	@DisplayName("Deve lancar excecao ao criar usuario com endereco inexistente")
	void deveLancarExcecaoAoCriarUsuarioComEnderecoInexistente() {
		UserGateway userGateway = mock(UserGateway.class);
		UserTypeGateway userTypeGateway = mock(UserTypeGateway.class);
		AddressGateway addressGateway = mock(AddressGateway.class);
		UUID userTypeId = UUID.randomUUID();
		UUID addressId = UUID.randomUUID();
		when(userGateway.existsByEmail("joao@email.com")).thenReturn(false);
		when(userGateway.existsByLogin("joao")).thenReturn(false);
		when(userTypeGateway.existsById(userTypeId)).thenReturn(true);
		when(addressGateway.existsById(addressId)).thenReturn(false);
		CreateUserUseCase useCase = new CreateUserUseCase(userGateway, userTypeGateway, addressGateway);

		AddressNotFoundException exception = assertThrows(
				AddressNotFoundException.class,
				() -> useCase.execute("Joao Silva", "joao@email.com", "joao", "senha123", userTypeId, addressId)
		);

		assertEquals("Endereco nao encontrado: " + addressId, exception.getMessage());
		verify(userGateway, never()).save(any(User.class));
	}

	@Test
	@DisplayName("Deve buscar usuario por identificador")
	void deveBuscarUsuarioPorIdentificador() {
		UserGateway userGateway = mock(UserGateway.class);
		UUID id = UUID.randomUUID();
		User savedUser = criarUsuario(id, UUID.randomUUID(), UUID.randomUUID());
		when(userGateway.findById(id)).thenReturn(Optional.of(savedUser));
		GetUserByIdUseCase useCase = new GetUserByIdUseCase(userGateway);

		User user = useCase.execute(id);

		assertEquals(id, user.getId());
		assertEquals("Joao Silva", user.getName());
		verify(userGateway).findById(id);
	}

	@Test
	@DisplayName("Deve lancar excecao ao buscar usuario inexistente")
	void deveLancarExcecaoAoBuscarUsuarioInexistente() {
		UserGateway userGateway = mock(UserGateway.class);
		UUID id = UUID.randomUUID();
		when(userGateway.findById(id)).thenReturn(Optional.empty());
		GetUserByIdUseCase useCase = new GetUserByIdUseCase(userGateway);

		UserNotFoundException exception = assertThrows(
				UserNotFoundException.class,
				() -> useCase.execute(id)
		);

		assertEquals("Usuario nao encontrado: " + id, exception.getMessage());
		verify(userGateway).findById(id);
	}

	@Test
	@DisplayName("Deve listar usuarios cadastrados")
	void deveListarUsuariosCadastrados() {
		UserGateway userGateway = mock(UserGateway.class);
		when(userGateway.findAll()).thenReturn(List.of(
				criarUsuario(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID()),
				criarUsuario(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID())
		));
		ListUsersUseCase useCase = new ListUsersUseCase(userGateway);

		List<User> users = useCase.execute();

		assertEquals(2, users.size());
		verify(userGateway).findAll();
	}

	@Test
	@DisplayName("Deve atualizar usuario")
	void deveAtualizarUsuario() {
		UserGateway userGateway = mock(UserGateway.class);
		UserTypeGateway userTypeGateway = mock(UserTypeGateway.class);
		AddressGateway addressGateway = mock(AddressGateway.class);
		UUID id = UUID.randomUUID();
		UUID userTypeId = UUID.randomUUID();
		UUID addressId = UUID.randomUUID();
		User savedUser = criarUsuario(id, UUID.randomUUID(), UUID.randomUUID());
		when(userGateway.findById(id)).thenReturn(Optional.of(savedUser));
		when(userGateway.findByEmail("maria@email.com")).thenReturn(Optional.empty());
		when(userGateway.findByLogin("maria")).thenReturn(Optional.empty());
		when(userTypeGateway.existsById(userTypeId)).thenReturn(true);
		when(addressGateway.existsById(addressId)).thenReturn(true);
		when(userGateway.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
		UpdateUserUseCase useCase = new UpdateUserUseCase(userGateway, userTypeGateway, addressGateway);
		ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);

		User user = useCase.execute(id, "Maria Silva", "MARIA@EMAIL.COM", "maria", userTypeId, addressId);

		assertEquals(id, user.getId());
		assertEquals("Maria Silva", user.getName());
		assertEquals("maria@email.com", user.getEmail());
		verify(userGateway).save(captor.capture());
		assertEquals("maria", captor.getValue().getLogin());
	}

	@Test
	@DisplayName("Deve lancar excecao ao atualizar usuario inexistente")
	void deveLancarExcecaoAoAtualizarUsuarioInexistente() {
		UserGateway userGateway = mock(UserGateway.class);
		UserTypeGateway userTypeGateway = mock(UserTypeGateway.class);
		AddressGateway addressGateway = mock(AddressGateway.class);
		UUID id = UUID.randomUUID();
		when(userGateway.findById(id)).thenReturn(Optional.empty());
		UpdateUserUseCase useCase = new UpdateUserUseCase(userGateway, userTypeGateway, addressGateway);

		UserNotFoundException exception = assertThrows(
				UserNotFoundException.class,
				() -> useCase.execute(id, "Maria Silva", "maria@email.com", "maria", UUID.randomUUID(), UUID.randomUUID())
		);

		assertEquals("Usuario nao encontrado: " + id, exception.getMessage());
		verify(userGateway, never()).save(any(User.class));
	}

	@Test
	@DisplayName("Deve lancar excecao ao atualizar para email de outro usuario")
	void deveLancarExcecaoAoAtualizarParaEmailDeOutroUsuario() {
		UserGateway userGateway = mock(UserGateway.class);
		UserTypeGateway userTypeGateway = mock(UserTypeGateway.class);
		AddressGateway addressGateway = mock(AddressGateway.class);
		UUID id = UUID.randomUUID();
		User savedUser = criarUsuario(id, UUID.randomUUID(), UUID.randomUUID());
		User otherUser = criarUsuario(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());
		when(userGateway.findById(id)).thenReturn(Optional.of(savedUser));
		when(userGateway.findByEmail("joao@email.com")).thenReturn(Optional.of(otherUser));
		UpdateUserUseCase useCase = new UpdateUserUseCase(userGateway, userTypeGateway, addressGateway);

		UserAlreadyExistsException exception = assertThrows(
				UserAlreadyExistsException.class,
				() -> useCase.execute(id, "Joao Silva", "joao@email.com", "joao", UUID.randomUUID(), UUID.randomUUID())
		);

		assertEquals("Usuario ja cadastrado com email: joao@email.com", exception.getMessage());
		verify(userGateway, never()).save(any(User.class));
	}

	@Test
	@DisplayName("Deve alterar senha do usuario")
	void deveAlterarSenhaDoUsuario() {
		UserGateway userGateway = mock(UserGateway.class);
		UUID id = UUID.randomUUID();
		User savedUser = criarUsuario(id, UUID.randomUUID(), UUID.randomUUID());
		when(userGateway.findById(id)).thenReturn(Optional.of(savedUser));
		when(userGateway.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
		ChangeUserPasswordUseCase useCase = new ChangeUserPasswordUseCase(userGateway);

		User user = useCase.execute(id, "novaSenha123");

		assertEquals("novaSenha123", user.getPassword());
		verify(userGateway).findById(id);
		verify(userGateway).save(any(User.class));
	}

	@Test
	@DisplayName("Deve remover usuario cadastrado")
	void deveRemoverUsuarioCadastrado() {
		UserGateway userGateway = mock(UserGateway.class);
		UUID id = UUID.randomUUID();
		when(userGateway.existsById(id)).thenReturn(true);
		DeleteUserUseCase useCase = new DeleteUserUseCase(userGateway);

		useCase.execute(id);

		verify(userGateway).existsById(id);
		verify(userGateway).deleteById(id);
	}

	@Test
	@DisplayName("Deve lancar excecao ao remover usuario inexistente")
	void deveLancarExcecaoAoRemoverUsuarioInexistente() {
		UserGateway userGateway = mock(UserGateway.class);
		UUID id = UUID.randomUUID();
		when(userGateway.existsById(id)).thenReturn(false);
		DeleteUserUseCase useCase = new DeleteUserUseCase(userGateway);

		UserNotFoundException exception = assertThrows(
				UserNotFoundException.class,
				() -> useCase.execute(id)
		);

		assertEquals("Usuario nao encontrado: " + id, exception.getMessage());
		verify(userGateway, never()).deleteById(id);
	}

	private User criarUsuario(UUID id, UUID userTypeId, UUID addressId) {
		return new User(
				id,
				"Joao Silva",
				"joao@email.com",
				"joao",
				"senha123",
				userTypeId,
				addressId,
				OffsetDateTime.now()
		);
	}
}
