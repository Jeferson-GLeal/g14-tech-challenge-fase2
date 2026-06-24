package com.fiap.foodlink_api.application.usecase.usertype;

import com.fiap.foodlink_api.domain.entity.UserType;
import com.fiap.foodlink_api.domain.exception.UserTypeNotFoundException;
import com.fiap.foodlink_api.domain.gateway.UserTypeGateway;

import java.util.UUID;

public class GetUserTypeByIdUseCase {

	private final UserTypeGateway userTypeGateway;

	public GetUserTypeByIdUseCase(UserTypeGateway userTypeGateway) {
		this.userTypeGateway = userTypeGateway;
	}

	public UserType execute(UUID id) {
		return userTypeGateway.findById(id)
				.orElseThrow(() -> new UserTypeNotFoundException(id));
	}
}
