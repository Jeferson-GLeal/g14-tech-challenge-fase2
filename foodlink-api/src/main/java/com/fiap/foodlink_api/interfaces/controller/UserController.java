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
import com.fiap.foodlink_api.interfaces.controller.mapper.UserControllerMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Usuarios", description = "Operacoes para cadastro de usuarios")
public class UserController {

	private final CreateUserUseCase createUserUseCase;
	private final GetUserByIdUseCase getUserByIdUseCase;
	private final ListUsersUseCase listUsersUseCase;
	private final UpdateUserUseCase updateUserUseCase;
	private final ChangeUserPasswordUseCase changeUserPasswordUseCase;
	private final DeleteUserUseCase deleteUserUseCase;
	private final CreateAddressUseCase createAddressUseCase;
	private final GetAddressByIdUseCase getAddressByIdUseCase;
	private final UpdateAddressUseCase updateAddressUseCase;

	public UserController(
			CreateUserUseCase createUserUseCase,
			GetUserByIdUseCase getUserByIdUseCase,
			ListUsersUseCase listUsersUseCase,
			UpdateUserUseCase updateUserUseCase,
			ChangeUserPasswordUseCase changeUserPasswordUseCase,
			DeleteUserUseCase deleteUserUseCase,
			CreateAddressUseCase createAddressUseCase,
			GetAddressByIdUseCase getAddressByIdUseCase,
			UpdateAddressUseCase updateAddressUseCase
	) {
		this.createUserUseCase = createUserUseCase;
		this.getUserByIdUseCase = getUserByIdUseCase;
		this.listUsersUseCase = listUsersUseCase;
		this.updateUserUseCase = updateUserUseCase;
		this.changeUserPasswordUseCase = changeUserPasswordUseCase;
		this.deleteUserUseCase = deleteUserUseCase;
		this.createAddressUseCase = createAddressUseCase;
		this.getAddressByIdUseCase = getAddressByIdUseCase;
		this.updateAddressUseCase = updateAddressUseCase;
	}

	@PostMapping
	@Transactional
	@Operation(summary = "Cria um usuario com endereco")
	public ResponseEntity<UserResponse> create(@RequestBody UserRequest request) {
		Address address = createAddress(request.address());
		User user = createUserUseCase.execute(
				request.name(),
				request.email(),
				request.login(),
				request.password(),
				request.userTypeId(),
				address.getId()
		);

		return ResponseEntity.status(HttpStatus.CREATED)
				.body(UserControllerMapper.toResponse(user, address));
	}

	@GetMapping("/{id}")
	@Operation(summary = "Busca um usuario por identificador")
	public ResponseEntity<UserResponse> findById(@PathVariable UUID id) {
		User user = getUserByIdUseCase.execute(id);
		Address address = getAddressByIdUseCase.execute(user.getAddressId());
		return ResponseEntity.ok(UserControllerMapper.toResponse(user, address));
	}

	@GetMapping
	@Operation(summary = "Lista usuarios")
	public ResponseEntity<List<UserResponse>> findAll() {
		List<UserResponse> users = listUsersUseCase.execute().stream()
				.map(user -> UserControllerMapper.toResponse(
						user,
						getAddressByIdUseCase.execute(user.getAddressId())
				))
				.toList();

		return ResponseEntity.ok(users);
	}

	@PutMapping("/{id}")
	@Transactional
	@Operation(summary = "Atualiza um usuario com endereco")
	public ResponseEntity<UserResponse> update(@PathVariable UUID id, @RequestBody UserRequest request) {
		User currentUser = getUserByIdUseCase.execute(id);
		Address address = updateAddress(currentUser.getAddressId(), request.address());
		User user = updateUserUseCase.execute(
				id,
				request.name(),
				request.email(),
				request.login(),
				request.userTypeId(),
				address.getId()
		);

		return ResponseEntity.ok(UserControllerMapper.toResponse(user, address));
	}

	@PatchMapping("/{id}/password")
	@Operation(summary = "Altera a senha de um usuario")
	public ResponseEntity<UserResponse> changePassword(
			@PathVariable UUID id,
			@RequestBody ChangeUserPasswordRequest request
	) {
		User user = changeUserPasswordUseCase.execute(id, request.password());
		Address address = getAddressByIdUseCase.execute(user.getAddressId());
		return ResponseEntity.ok(UserControllerMapper.toResponse(user, address));
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Remove um usuario")
	public ResponseEntity<Void> delete(@PathVariable UUID id) {
		deleteUserUseCase.execute(id);
		return ResponseEntity.noContent().build();
	}

	private Address createAddress(AddressRequest request) {
		requireAddress(request);

		return createAddressUseCase.execute(
				request.street(),
				request.number(),
				request.complement(),
				request.district(),
				request.city(),
				request.state(),
				request.zipCode()
		);
	}

	private Address updateAddress(UUID id, AddressRequest request) {
		requireAddress(request);

		return updateAddressUseCase.execute(
				id,
				request.street(),
				request.number(),
				request.complement(),
				request.district(),
				request.city(),
				request.state(),
				request.zipCode()
		);
	}

	private void requireAddress(AddressRequest request) {
		if (request == null) {
			throw new DomainException("Endereco do usuario e obrigatorio.");
		}
	}
}
