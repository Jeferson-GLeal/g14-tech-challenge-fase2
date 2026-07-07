package com.fiap.foodlink_api.application.usecase.usertype;

import com.fiap.foodlink_api.domain.entity.UserType;
import com.fiap.foodlink_api.domain.entity.UserTypeCode;
import com.fiap.foodlink_api.domain.exception.DomainException;
import com.fiap.foodlink_api.domain.exception.UserTypeAlreadyExistsException;
import com.fiap.foodlink_api.domain.exception.UserTypeNotFoundException;
import com.fiap.foodlink_api.domain.gateway.UserGateway;
import com.fiap.foodlink_api.domain.gateway.UserTypeGateway;

import java.util.Objects;
import java.util.UUID;

public class UpdateUserTypeUseCase {

	private final UserTypeGateway userTypeGateway;
	private final UserGateway userGateway;

	public UpdateUserTypeUseCase(UserTypeGateway userTypeGateway, UserGateway userGateway) {
		this.userTypeGateway = userTypeGateway;
		this.userGateway = userGateway;
	}

	public UserType execute(UUID id, String name, UserTypeCode code) {
		UserType userType = userTypeGateway.findById(id)
				.orElseThrow(() -> new UserTypeNotFoundException(id));

		validateRestaurantOwners(userType, code);
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

	private void validateRestaurantOwners(UserType currentUserType, UserTypeCode newCode) {
		if (currentUserType.getCode() != UserTypeCode.DONO_RESTAURANTE || newCode == UserTypeCode.DONO_RESTAURANTE) {
			return;
		}

		if (userGateway.existsByUserTypeId(currentUserType.getId())) {
			throw new DomainException("Tipo de usuário DONO_RESTAURANTE nao pode ser alterado porque existem usuários vinculados a esse tipo.");
		}
	}
}
