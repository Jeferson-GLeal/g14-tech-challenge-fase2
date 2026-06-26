package com.fiap.foodlink_api.application.usecase.usertype;

import com.fiap.foodlink_api.domain.exception.UserTypeNotFoundException;
import com.fiap.foodlink_api.domain.gateway.UserTypeGateway;

import java.util.UUID;

public class DeleteUserTypeUseCase {

	private final UserTypeGateway userTypeGateway;

	public DeleteUserTypeUseCase(UserTypeGateway userTypeGateway) {
		this.userTypeGateway = userTypeGateway;
	}

	public void execute(UUID id) {
		if (!userTypeGateway.existsById(id)) {
			throw new UserTypeNotFoundException(id);
		}

		userTypeGateway.deleteById(id);
	}
}
