package com.fiap.foodlink_api.application.usecase.user;

import com.fiap.foodlink_api.domain.exception.UserNotFoundException;
import com.fiap.foodlink_api.domain.gateway.UserGateway;

import java.util.UUID;

public class DeleteUserUseCase {

	private final UserGateway userGateway;

	public DeleteUserUseCase(UserGateway userGateway) {
		this.userGateway = userGateway;
	}

	public void execute(UUID id) {
		if (!userGateway.existsById(id)) {
			throw new UserNotFoundException(id);
		}

		userGateway.deleteById(id);
	}
}
