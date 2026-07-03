package com.fiap.foodlink_api.interfaces.controller;

import com.fiap.foodlink_api.application.usecase.usertype.CreateUserTypeUseCase;
import com.fiap.foodlink_api.application.usecase.usertype.DeleteUserTypeUseCase;
import com.fiap.foodlink_api.application.usecase.usertype.GetUserTypeByIdUseCase;
import com.fiap.foodlink_api.application.usecase.usertype.ListUserTypesUseCase;
import com.fiap.foodlink_api.application.usecase.usertype.UpdateUserTypeUseCase;
import com.fiap.foodlink_api.domain.entity.UserType;
import com.fiap.foodlink_api.interfaces.controller.dto.UserTypeRequest;
import com.fiap.foodlink_api.interfaces.controller.dto.UserTypeResponse;
import com.fiap.foodlink_api.interfaces.controller.mapper.UserTypeControllerMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/user-types")
@Tag(name = "Tipos de usuario", description = "Operacoes para cadastro de tipos de usuario")
public class UserTypeController {

	private final CreateUserTypeUseCase createUserTypeUseCase;
	private final GetUserTypeByIdUseCase getUserTypeByIdUseCase;
	private final ListUserTypesUseCase listUserTypesUseCase;
	private final UpdateUserTypeUseCase updateUserTypeUseCase;
	private final DeleteUserTypeUseCase deleteUserTypeUseCase;

	public UserTypeController(
			CreateUserTypeUseCase createUserTypeUseCase,
			GetUserTypeByIdUseCase getUserTypeByIdUseCase,
			ListUserTypesUseCase listUserTypesUseCase,
			UpdateUserTypeUseCase updateUserTypeUseCase,
			DeleteUserTypeUseCase deleteUserTypeUseCase
	) {
		this.createUserTypeUseCase = createUserTypeUseCase;
		this.getUserTypeByIdUseCase = getUserTypeByIdUseCase;
		this.listUserTypesUseCase = listUserTypesUseCase;
		this.updateUserTypeUseCase = updateUserTypeUseCase;
		this.deleteUserTypeUseCase = deleteUserTypeUseCase;
	}

	@PostMapping
	@Operation(summary = "Cria um tipo de usuario")
	public ResponseEntity<UserTypeResponse> create(@RequestBody UserTypeRequest request) {
		UserType userType = createUserTypeUseCase.execute(request.name(), request.code());
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(UserTypeControllerMapper.toResponse(userType));
	}

	@GetMapping("/{id}")
	@Operation(summary = "Busca um tipo de usuario por identificador")
	public ResponseEntity<UserTypeResponse> findById(@PathVariable UUID id) {
		UserType userType = getUserTypeByIdUseCase.execute(id);
		return ResponseEntity.ok(UserTypeControllerMapper.toResponse(userType));
	}

	@GetMapping
	@Operation(summary = "Lista os tipos de usuario")
	public ResponseEntity<List<UserTypeResponse>> findAll() {
		List<UserTypeResponse> userTypes = listUserTypesUseCase.execute().stream()
				.map(UserTypeControllerMapper::toResponse)
				.toList();
		return ResponseEntity.ok(userTypes);
	}

	@PutMapping("/{id}")
	@Operation(summary = "Atualiza um tipo de usuario")
	public ResponseEntity<UserTypeResponse> update(@PathVariable UUID id, @RequestBody UserTypeRequest request) {
		UserType userType = updateUserTypeUseCase.execute(id, request.name(), request.code());
		return ResponseEntity.ok(UserTypeControllerMapper.toResponse(userType));
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Remove um tipo de usuario")
	public ResponseEntity<Void> delete(@PathVariable UUID id) {
		deleteUserTypeUseCase.execute(id);
		return ResponseEntity.noContent().build();
	}
}
