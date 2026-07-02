package com.fiap.foodlink_api.application.usecase.usertype;

import com.fiap.foodlink_api.domain.entity.UserType;
import com.fiap.foodlink_api.domain.entity.UserTypeCode;
import com.fiap.foodlink_api.domain.exception.UserTypeAlreadyExistsException;
import com.fiap.foodlink_api.domain.exception.UserTypeNotFoundException;
import com.fiap.foodlink_api.domain.gateway.UserTypeGateway;

import java.util.Objects;
import java.util.UUID;

public class UpdateUserTypeUseCase {

	private final UserTypeGateway userTypeGateway;

	public UpdateUserTypeUseCase(UserTypeGateway userTypeGateway) {
		this.userTypeGateway = userTypeGateway;
	}

	public UserType execute(UUID id, String name, UserTypeCode code) {
		UserType userType = userTypeGateway.findById(id)
				.orElseThrow(() -> new UserTypeNotFoundException(id));

		userType.update(name, code);
		validateUniqueName(userType);

		return userTypeGateway.save(userType);
	}

	private void validateUniqueName(UserType userType) {
		userTypeGateway.findByName(userType.getName())
				.filter(existingUserType -> !Objects.equals(existingUserType.getId(), userType.getId()))
				.ifPresent(existingUserType -> {
					throw new UserTypeAlreadyExistsException(userType.getName());
				});
	}
}
