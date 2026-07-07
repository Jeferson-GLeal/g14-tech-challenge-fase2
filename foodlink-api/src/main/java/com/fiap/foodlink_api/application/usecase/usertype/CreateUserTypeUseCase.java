package com.fiap.foodlink_api.application.usecase.usertype;

import com.fiap.foodlink_api.domain.entity.UserType;
import com.fiap.foodlink_api.domain.entity.UserTypeCode;
import com.fiap.foodlink_api.domain.exception.UserTypeAlreadyExistsException;
import com.fiap.foodlink_api.domain.gateway.UserTypeGateway;

public class CreateUserTypeUseCase {

	private final UserTypeGateway userTypeGateway;

	public CreateUserTypeUseCase(UserTypeGateway userTypeGateway) {
		this.userTypeGateway = userTypeGateway;
	}

	public UserType execute(String name, UserTypeCode code) {
		UserType userType = UserType.create(name, code);

		if (userTypeGateway.existsByName(userType.getName())) {
			throw new UserTypeAlreadyExistsException(userType.getName());
		}

		return userTypeGateway.save(userType);
	}
}
