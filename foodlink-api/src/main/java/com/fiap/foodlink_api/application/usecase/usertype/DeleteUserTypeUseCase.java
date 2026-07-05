package com.fiap.foodlink_api.application.usecase.usertype;

import com.fiap.foodlink_api.domain.entity.UserType;
import com.fiap.foodlink_api.domain.entity.UserTypeCode;
import com.fiap.foodlink_api.domain.exception.DomainException;
import com.fiap.foodlink_api.domain.exception.UserTypeNotFoundException;
import com.fiap.foodlink_api.domain.gateway.UserGateway;
import com.fiap.foodlink_api.domain.gateway.UserTypeGateway;

import java.util.UUID;

public class DeleteUserTypeUseCase {

	private final UserTypeGateway userTypeGateway;
	private final UserGateway userGateway;

	public DeleteUserTypeUseCase(UserTypeGateway userTypeGateway, UserGateway userGateway) {
		this.userTypeGateway = userTypeGateway;
		this.userGateway = userGateway;
	}

	public void execute(UUID id) {
		UserType userType = userTypeGateway.findById(id)
				.orElseThrow(() -> new UserTypeNotFoundException(id));

		validateRestaurantOwners(userType);
		userTypeGateway.deleteById(id);
	}

	private void validateRestaurantOwners(UserType userType) {
		if (userType.getCode() != UserTypeCode.DONO_RESTAURANTE) {
			return;
		}

		if (userGateway.existsByUserTypeId(userType.getId())) {
			throw new DomainException("Tipo de usuário DONO_RESTAURANTE nao pode ser removido porque existem usuários vinculados a esse tipo.");
		}
	}
}
