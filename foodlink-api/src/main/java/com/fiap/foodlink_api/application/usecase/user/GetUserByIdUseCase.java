package com.fiap.foodlink_api.application.usecase.user;

import com.fiap.foodlink_api.domain.entity.User;
import com.fiap.foodlink_api.domain.exception.UserNotFoundException;
import com.fiap.foodlink_api.domain.gateway.UserGateway;

import java.util.UUID;

public class GetUserByIdUseCase {

	private final UserGateway userGateway;

	public GetUserByIdUseCase(UserGateway userGateway) {
		this.userGateway = userGateway;
	}

	public User execute(UUID id) {
		return userGateway.findById(id)
				.orElseThrow(() -> new UserNotFoundException(id));
	}
}
