package com.fiap.foodlink_api.interfaces.controller;

import com.fiap.foodlink_api.application.usecase.usertype.CreateUserTypeUseCase;
import com.fiap.foodlink_api.application.usecase.usertype.DeleteUserTypeUseCase;
import com.fiap.foodlink_api.application.usecase.usertype.GetUserTypeByIdUseCase;
import com.fiap.foodlink_api.application.usecase.usertype.ListUserTypesUseCase;
import com.fiap.foodlink_api.application.usecase.usertype.UpdateUserTypeUseCase;
import com.fiap.foodlink_api.domain.entity.UserType;
import com.fiap.foodlink_api.interfaces.controller.dto.UserTypeRequest;
import com.fiap.foodlink_api.interfaces.controller.dto.UserTypeResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Interface de tipo de usuario")
class UserTypeControllerTest {

	@Test
	@DisplayName("Deve criar tipo de usuario")
	void deveCriarTipoUsuario() {
		CreateUserTypeUseCase createUserTypeUseCase = mock(CreateUserTypeUseCase.class);
		UserTypeController controller = criarController(createUserTypeUseCase);
		UUID id = UUID.randomUUID();
		when(createUserTypeUseCase.execute("Cliente")).thenReturn(new UserType(id, "Cliente"));

		ResponseEntity<UserTypeResponse> response = controller.create(new UserTypeRequest("Cliente"));

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(id, response.getBody().id());
		assertEquals("Cliente", response.getBody().name());
		verify(createUserTypeUseCase).execute("Cliente");
	}

	@Test
	@DisplayName("Deve buscar tipo de usuario por identificador")
	void deveBuscarTipoUsuarioPorIdentificador() {
		GetUserTypeByIdUseCase getUserTypeByIdUseCase = mock(GetUserTypeByIdUseCase.class);
		UserTypeController controller = criarController(getUserTypeByIdUseCase);
		UUID id = UUID.randomUUID();
		when(getUserTypeByIdUseCase.execute(id)).thenReturn(new UserType(id, "Cliente"));

		ResponseEntity<UserTypeResponse> response = controller.findById(id);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(id, response.getBody().id());
		assertEquals("Cliente", response.getBody().name());
		verify(getUserTypeByIdUseCase).execute(id);
	}

	@Test
	@DisplayName("Deve listar tipos de usuario")
	void deveListarTiposUsuario() {
		ListUserTypesUseCase listUserTypesUseCase = mock(ListUserTypesUseCase.class);
		UserTypeController controller = criarController(listUserTypesUseCase);
		when(listUserTypesUseCase.execute()).thenReturn(List.of(
				new UserType(UUID.randomUUID(), "Cliente"),
				new UserType(UUID.randomUUID(), "Dono de Restaurante")
		));

		ResponseEntity<List<UserTypeResponse>> response = controller.findAll();

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(2, response.getBody().size());
		assertEquals("Cliente", response.getBody().get(0).name());
		assertEquals("Dono de Restaurante", response.getBody().get(1).name());
		verify(listUserTypesUseCase).execute();
	}

	@Test
	@DisplayName("Deve atualizar tipo de usuario")
	void deveAtualizarTipoUsuario() {
		UpdateUserTypeUseCase updateUserTypeUseCase = mock(UpdateUserTypeUseCase.class);
		UserTypeController controller = criarController(updateUserTypeUseCase);
		UUID id = UUID.randomUUID();
		when(updateUserTypeUseCase.execute(id, "Dono de Restaurante"))
				.thenReturn(new UserType(id, "Dono de Restaurante"));

		ResponseEntity<UserTypeResponse> response = controller.update(id, new UserTypeRequest("Dono de Restaurante"));

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(id, response.getBody().id());
		assertEquals("Dono de Restaurante", response.getBody().name());
		verify(updateUserTypeUseCase).execute(id, "Dono de Restaurante");
	}

	@Test
	@DisplayName("Deve remover tipo de usuario")
	void deveRemoverTipoUsuario() {
		DeleteUserTypeUseCase deleteUserTypeUseCase = mock(DeleteUserTypeUseCase.class);
		UserTypeController controller = criarController(deleteUserTypeUseCase);
		UUID id = UUID.randomUUID();

		ResponseEntity<Void> response = controller.delete(id);

		assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
		assertNull(response.getBody());
		verify(deleteUserTypeUseCase).execute(id);
	}

	private UserTypeController criarController(CreateUserTypeUseCase createUserTypeUseCase) {
		return new UserTypeController(
				createUserTypeUseCase,
				mock(GetUserTypeByIdUseCase.class),
				mock(ListUserTypesUseCase.class),
				mock(UpdateUserTypeUseCase.class),
				mock(DeleteUserTypeUseCase.class)
		);
	}

	private UserTypeController criarController(GetUserTypeByIdUseCase getUserTypeByIdUseCase) {
		return new UserTypeController(
				mock(CreateUserTypeUseCase.class),
				getUserTypeByIdUseCase,
				mock(ListUserTypesUseCase.class),
				mock(UpdateUserTypeUseCase.class),
				mock(DeleteUserTypeUseCase.class)
		);
	}

	private UserTypeController criarController(ListUserTypesUseCase listUserTypesUseCase) {
		return new UserTypeController(
				mock(CreateUserTypeUseCase.class),
				mock(GetUserTypeByIdUseCase.class),
				listUserTypesUseCase,
				mock(UpdateUserTypeUseCase.class),
				mock(DeleteUserTypeUseCase.class)
		);
	}

	private UserTypeController criarController(UpdateUserTypeUseCase updateUserTypeUseCase) {
		return new UserTypeController(
				mock(CreateUserTypeUseCase.class),
				mock(GetUserTypeByIdUseCase.class),
				mock(ListUserTypesUseCase.class),
				updateUserTypeUseCase,
				mock(DeleteUserTypeUseCase.class)
		);
	}

	private UserTypeController criarController(DeleteUserTypeUseCase deleteUserTypeUseCase) {
		return new UserTypeController(
				mock(CreateUserTypeUseCase.class),
				mock(GetUserTypeByIdUseCase.class),
				mock(ListUserTypesUseCase.class),
				mock(UpdateUserTypeUseCase.class),
				deleteUserTypeUseCase
		);
	}
}
