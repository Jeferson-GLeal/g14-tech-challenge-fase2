package com.fiap.foodlink_api.application.usecase.user;

import com.fiap.foodlink_api.domain.entity.User;
import com.fiap.foodlink_api.domain.exception.UserNotFoundException;
import com.fiap.foodlink_api.domain.gateway.UserGateway;

import java.util.UUID;

public class ChangeUserPasswordUseCase {

	private final UserGateway userGateway;

	public ChangeUserPasswordUseCase(UserGateway userGateway) {
		this.userGateway = userGateway;
	}

	public User execute(UUID id, String password) {
		User user = userGateway.findById(id)
				.orElseThrow(() -> new UserNotFoundException(id));

		user.changePassword(password);
		return userGateway.save(user);
	}
}
