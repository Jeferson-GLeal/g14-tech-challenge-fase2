package com.fiap.foodlink_api.application.usecase.user;

import com.fiap.foodlink_api.domain.entity.User;
import com.fiap.foodlink_api.domain.exception.AddressNotFoundException;
import com.fiap.foodlink_api.domain.exception.UserAlreadyExistsException;
import com.fiap.foodlink_api.domain.exception.UserTypeNotFoundException;
import com.fiap.foodlink_api.domain.gateway.AddressGateway;
import com.fiap.foodlink_api.domain.gateway.UserGateway;
import com.fiap.foodlink_api.domain.gateway.UserTypeGateway;

import java.util.UUID;

public class CreateUserUseCase {

	private final UserGateway userGateway;
	private final UserTypeGateway userTypeGateway;
	private final AddressGateway addressGateway;

	public CreateUserUseCase(UserGateway userGateway, UserTypeGateway userTypeGateway, AddressGateway addressGateway) {
		this.userGateway = userGateway;
		this.userTypeGateway = userTypeGateway;
		this.addressGateway = addressGateway;
	}

	public User execute(String name, String email, String login, String password, UUID userTypeId, UUID addressId) {
		User user = User.create(name, email, login, password, userTypeId, addressId);

		validateUniqueEmail(user);
		validateUniqueLogin(user);
		validateRelationships(user.getUserTypeId(), user.getAddressId());

		return userGateway.save(user);
	}

	private void validateUniqueEmail(User user) {
		if (userGateway.existsByEmail(user.getEmail())) {
			throw new UserAlreadyExistsException("email", user.getEmail());
		}
	}

	private void validateUniqueLogin(User user) {
		if (userGateway.existsByLogin(user.getLogin())) {
			throw new UserAlreadyExistsException("login", user.getLogin());
		}
	}

	private void validateRelationships(UUID userTypeId, UUID addressId) {
		if (!userTypeGateway.existsById(userTypeId)) {
			throw new UserTypeNotFoundException(userTypeId);
		}

		if (!addressGateway.existsById(addressId)) {
			throw new AddressNotFoundException(addressId);
		}
	}
}
