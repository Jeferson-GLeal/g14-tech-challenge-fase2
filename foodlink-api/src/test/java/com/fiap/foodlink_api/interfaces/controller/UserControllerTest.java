package com.fiap.foodlink_api.interfaces.controller;

import com.fiap.foodlink_api.application.usecase.address.CreateAddressUseCase;
import com.fiap.foodlink_api.application.usecase.address.GetAddressByIdUseCase;
import com.fiap.foodlink_api.application.usecase.address.UpdateAddressUseCase;
import com.fiap.foodlink_api.application.usecase.user.ChangeUserPasswordUseCase;
import com.fiap.foodlink_api.application.usecase.user.CreateUserUseCase;
import com.fiap.foodlink_api.application.usecase.user.DeleteUserUseCase;
import com.fiap.foodlink_api.application.usecase.user.GetUserByIdUseCase;
import com.fiap.foodlink_api.application.usecase.user.ListUsersUseCase;
import com.fiap.foodlink_api.application.usecase.user.UpdateUserUseCase;
import com.fiap.foodlink_api.domain.entity.Address;
import com.fiap.foodlink_api.domain.entity.User;
import com.fiap.foodlink_api.domain.exception.DomainException;
import com.fiap.foodlink_api.interfaces.controller.dto.AddressRequest;
import com.fiap.foodlink_api.interfaces.controller.dto.ChangeUserPasswordRequest;
import com.fiap.foodlink_api.interfaces.controller.dto.UserRequest;
import com.fiap.foodlink_api.interfaces.controller.dto.UserResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Interface de usuario")
class UserControllerTest {

	@Test
	@DisplayName("Deve criar usuario com endereco")
	void deveCriarUsuarioComEndereco() {
		CreateUserUseCase createUserUseCase = mock(CreateUserUseCase.class);
		CreateAddressUseCase createAddressUseCase = mock(CreateAddressUseCase.class);
		UserController controller = criarController(createUserUseCase, createAddressUseCase);
		UUID userId = UUID.randomUUID();
		UUID userTypeId = UUID.randomUUID();
		UUID addressId = UUID.randomUUID();
		Address address = criarEndereco(addressId);
		User user = criarUsuario(userId, userTypeId, addressId);
		UserRequest request = criarUserRequest(userTypeId);
		when(createAddressUseCase.execute("Rua das Flores", "123", "Apto 10", "Centro", "Sao Paulo", "SP", "01001000"))
				.thenReturn(address);
		when(createUserUseCase.execute("Joao Silva", "joao@email.com", "joao", "senha123", userTypeId, addressId))
				.thenReturn(user);

		ResponseEntity<UserResponse> response = controller.create(request);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(userId, response.getBody().id());
		assertEquals("Joao Silva", response.getBody().name());
		assertEquals(addressId, response.getBody().address().id());
		verify(createAddressUseCase).execute("Rua das Flores", "123", "Apto 10", "Centro", "Sao Paulo", "SP", "01001000");
		verify(createUserUseCase).execute("Joao Silva", "joao@email.com", "joao", "senha123", userTypeId, addressId);
	}

	@Test
	@DisplayName("Deve buscar usuario por identificador com endereco")
	void deveBuscarUsuarioPorIdentificadorComEndereco() {
		GetUserByIdUseCase getUserByIdUseCase = mock(GetUserByIdUseCase.class);
		GetAddressByIdUseCase getAddressByIdUseCase = mock(GetAddressByIdUseCase.class);
		UserController controller = criarController(getUserByIdUseCase, getAddressByIdUseCase);
		UUID userId = UUID.randomUUID();
		UUID addressId = UUID.randomUUID();
		User user = criarUsuario(userId, UUID.randomUUID(), addressId);
		Address address = criarEndereco(addressId);
		when(getUserByIdUseCase.execute(userId)).thenReturn(user);
		when(getAddressByIdUseCase.execute(addressId)).thenReturn(address);

		ResponseEntity<UserResponse> response = controller.findById(userId);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(userId, response.getBody().id());
		assertEquals(addressId, response.getBody().address().id());
		verify(getUserByIdUseCase).execute(userId);
		verify(getAddressByIdUseCase).execute(addressId);
	}

	@Test
	@DisplayName("Deve listar usuarios com endereco")
	void deveListarUsuariosComEndereco() {
		ListUsersUseCase listUsersUseCase = mock(ListUsersUseCase.class);
		GetAddressByIdUseCase getAddressByIdUseCase = mock(GetAddressByIdUseCase.class);
		UserController controller = criarController(listUsersUseCase, getAddressByIdUseCase);
		UUID firstAddressId = UUID.randomUUID();
		UUID secondAddressId = UUID.randomUUID();
		User firstUser = criarUsuario(UUID.randomUUID(), UUID.randomUUID(), firstAddressId);
		User secondUser = criarUsuario(UUID.randomUUID(), UUID.randomUUID(), secondAddressId);
		when(listUsersUseCase.execute()).thenReturn(List.of(firstUser, secondUser));
		when(getAddressByIdUseCase.execute(firstAddressId)).thenReturn(criarEndereco(firstAddressId));
		when(getAddressByIdUseCase.execute(secondAddressId)).thenReturn(criarEndereco(secondAddressId));

		ResponseEntity<List<UserResponse>> response = controller.findAll();

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(2, response.getBody().size());
		assertEquals(firstAddressId, response.getBody().get(0).address().id());
		assertEquals(secondAddressId, response.getBody().get(1).address().id());
		verify(listUsersUseCase).execute();
		verify(getAddressByIdUseCase).execute(firstAddressId);
		verify(getAddressByIdUseCase).execute(secondAddressId);
	}

	@Test
	@DisplayName("Deve atualizar usuario com endereco")
	void deveAtualizarUsuarioComEndereco() {
		GetUserByIdUseCase getUserByIdUseCase = mock(GetUserByIdUseCase.class);
		UpdateUserUseCase updateUserUseCase = mock(UpdateUserUseCase.class);
		UpdateAddressUseCase updateAddressUseCase = mock(UpdateAddressUseCase.class);
		UserController controller = criarController(getUserByIdUseCase, updateUserUseCase, updateAddressUseCase);
		UUID userId = UUID.randomUUID();
		UUID userTypeId = UUID.randomUUID();
		UUID addressId = UUID.randomUUID();
		User currentUser = criarUsuario(userId, UUID.randomUUID(), addressId);
		Address updatedAddress = criarEndereco(addressId);
		User updatedUser = criarUsuario(userId, userTypeId, addressId);
		UserRequest request = criarUserRequest(userTypeId);
		when(getUserByIdUseCase.execute(userId)).thenReturn(currentUser);
		when(updateAddressUseCase.execute(addressId, "Rua das Flores", "123", "Apto 10", "Centro", "Sao Paulo", "SP", "01001000"))
				.thenReturn(updatedAddress);
		when(updateUserUseCase.execute(userId, "Joao Silva", "joao@email.com", "joao", userTypeId, addressId))
				.thenReturn(updatedUser);

		ResponseEntity<UserResponse> response = controller.update(userId, request);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(userId, response.getBody().id());
		assertEquals(addressId, response.getBody().address().id());
		verify(getUserByIdUseCase).execute(userId);
		verify(updateAddressUseCase).execute(addressId, "Rua das Flores", "123", "Apto 10", "Centro", "Sao Paulo", "SP", "01001000");
		verify(updateUserUseCase).execute(userId, "Joao Silva", "joao@email.com", "joao", userTypeId, addressId);
	}

	@Test
	@DisplayName("Deve alterar senha do usuario")
	void deveAlterarSenhaDoUsuario() {
		ChangeUserPasswordUseCase changeUserPasswordUseCase = mock(ChangeUserPasswordUseCase.class);
		GetAddressByIdUseCase getAddressByIdUseCase = mock(GetAddressByIdUseCase.class);
		UserController controller = criarController(changeUserPasswordUseCase, getAddressByIdUseCase);
		UUID userId = UUID.randomUUID();
		UUID addressId = UUID.randomUUID();
		User user = criarUsuario(userId, UUID.randomUUID(), addressId);
		when(changeUserPasswordUseCase.execute(userId, "novaSenha")).thenReturn(user);
		when(getAddressByIdUseCase.execute(addressId)).thenReturn(criarEndereco(addressId));

		ResponseEntity<UserResponse> response = controller.changePassword(
				userId,
				new ChangeUserPasswordRequest("novaSenha")
		);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(userId, response.getBody().id());
		verify(changeUserPasswordUseCase).execute(userId, "novaSenha");
		verify(getAddressByIdUseCase).execute(addressId);
	}

	@Test
	@DisplayName("Deve remover usuario")
	void deveRemoverUsuario() {
		DeleteUserUseCase deleteUserUseCase = mock(DeleteUserUseCase.class);
		UserController controller = criarController(deleteUserUseCase);
		UUID userId = UUID.randomUUID();

		ResponseEntity<Void> response = controller.delete(userId);

		assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
		assertNull(response.getBody());
		verify(deleteUserUseCase).execute(userId);
	}

	@Test
	@DisplayName("Deve lancar excecao quando endereco nao for informado na criacao")
	void deveLancarExcecaoQuandoEnderecoNaoForInformadoNaCriacao() {
		UserController controller = criarController(mock(CreateUserUseCase.class), mock(CreateAddressUseCase.class));
		UserRequest request = new UserRequest(
				"Joao Silva",
				"joao@email.com",
				"joao",
				"senha123",
				UUID.randomUUID(),
				null
		);

		DomainException exception = assertThrows(DomainException.class, () -> controller.create(request));

		assertEquals("Endereco do usuario e obrigatorio.", exception.getMessage());
	}

	private UserController criarController(CreateUserUseCase createUserUseCase, CreateAddressUseCase createAddressUseCase) {
		return new UserController(
				createUserUseCase,
				mock(GetUserByIdUseCase.class),
				mock(ListUsersUseCase.class),
				mock(UpdateUserUseCase.class),
				mock(ChangeUserPasswordUseCase.class),
				mock(DeleteUserUseCase.class),
				createAddressUseCase,
				mock(GetAddressByIdUseCase.class),
				mock(UpdateAddressUseCase.class)
		);
	}

	private UserController criarController(GetUserByIdUseCase getUserByIdUseCase, GetAddressByIdUseCase getAddressByIdUseCase) {
		return new UserController(
				mock(CreateUserUseCase.class),
				getUserByIdUseCase,
				mock(ListUsersUseCase.class),
				mock(UpdateUserUseCase.class),
				mock(ChangeUserPasswordUseCase.class),
				mock(DeleteUserUseCase.class),
				mock(CreateAddressUseCase.class),
				getAddressByIdUseCase,
				mock(UpdateAddressUseCase.class)
		);
	}

	private UserController criarController(ListUsersUseCase listUsersUseCase, GetAddressByIdUseCase getAddressByIdUseCase) {
		return new UserController(
				mock(CreateUserUseCase.class),
				mock(GetUserByIdUseCase.class),
				listUsersUseCase,
				mock(UpdateUserUseCase.class),
				mock(ChangeUserPasswordUseCase.class),
				mock(DeleteUserUseCase.class),
				mock(CreateAddressUseCase.class),
				getAddressByIdUseCase,
				mock(UpdateAddressUseCase.class)
		);
	}

	private UserController criarController(
			GetUserByIdUseCase getUserByIdUseCase,
			UpdateUserUseCase updateUserUseCase,
			UpdateAddressUseCase updateAddressUseCase
	) {
		return new UserController(
				mock(CreateUserUseCase.class),
				getUserByIdUseCase,
				mock(ListUsersUseCase.class),
				updateUserUseCase,
				mock(ChangeUserPasswordUseCase.class),
				mock(DeleteUserUseCase.class),
				mock(CreateAddressUseCase.class),
				mock(GetAddressByIdUseCase.class),
				updateAddressUseCase
		);
	}

	private UserController criarController(
			ChangeUserPasswordUseCase changeUserPasswordUseCase,
			GetAddressByIdUseCase getAddressByIdUseCase
	) {
		return new UserController(
				mock(CreateUserUseCase.class),
				mock(GetUserByIdUseCase.class),
				mock(ListUsersUseCase.class),
				mock(UpdateUserUseCase.class),
				changeUserPasswordUseCase,
				mock(DeleteUserUseCase.class),
				mock(CreateAddressUseCase.class),
				getAddressByIdUseCase,
				mock(UpdateAddressUseCase.class)
		);
	}

	private UserController criarController(DeleteUserUseCase deleteUserUseCase) {
		return new UserController(
				mock(CreateUserUseCase.class),
				mock(GetUserByIdUseCase.class),
				mock(ListUsersUseCase.class),
				mock(UpdateUserUseCase.class),
				mock(ChangeUserPasswordUseCase.class),
				deleteUserUseCase,
				mock(CreateAddressUseCase.class),
				mock(GetAddressByIdUseCase.class),
				mock(UpdateAddressUseCase.class)
		);
	}

	private UserRequest criarUserRequest(UUID userTypeId) {
		return new UserRequest(
				"Joao Silva",
				"joao@email.com",
				"joao",
				"senha123",
				userTypeId,
				new AddressRequest("Rua das Flores", "123", "Apto 10", "Centro", "Sao Paulo", "SP", "01001000")
		);
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
				OffsetDateTime.parse("2026-06-24T01:00:00-03:00")
		);
	}

	private Address criarEndereco(UUID id) {
		return new Address(
				id,
				"Rua das Flores",
				"123",
				"Apto 10",
				"Centro",
				"Sao Paulo",
				"SP",
				"01001000",
				OffsetDateTime.parse("2026-06-24T01:00:00-03:00")
		);
	}
}
