package com.fiap.foodlink_api.application.usecase.user;

import com.fiap.foodlink_api.domain.exception.UserNotFoundException;
import com.fiap.foodlink_api.domain.exception.DomainException;
import com.fiap.foodlink_api.domain.gateway.RestaurantGateway;
import com.fiap.foodlink_api.domain.gateway.UserGateway;

import java.util.UUID;

public class DeleteUserUseCase {

	private final UserGateway userGateway;
	private final RestaurantGateway restaurantGateway;

	public DeleteUserUseCase(UserGateway userGateway, RestaurantGateway restaurantGateway) {
		this.userGateway = userGateway;
		this.restaurantGateway = restaurantGateway;
	}

	public void execute(UUID id) {
		if (!userGateway.existsById(id)) {
			throw new UserNotFoundException(id);
		}

		if (restaurantGateway.existsByOwnerId(id)) {
			throw new DomainException("Usuario nao pode ser removido porque e dono de restaurante.");
		}

		userGateway.deleteById(id);
	}
}
