package com.fiap.foodlink_api.application.usecase.user;

import com.fiap.foodlink_api.domain.entity.User;
import com.fiap.foodlink_api.domain.entity.UserType;
import com.fiap.foodlink_api.domain.entity.UserTypeCode;
import com.fiap.foodlink_api.domain.exception.AddressNotFoundException;
import com.fiap.foodlink_api.domain.exception.DomainException;
import com.fiap.foodlink_api.domain.exception.UserAlreadyExistsException;
import com.fiap.foodlink_api.domain.exception.UserNotFoundException;
import com.fiap.foodlink_api.domain.exception.UserTypeNotFoundException;
import com.fiap.foodlink_api.domain.gateway.AddressGateway;
import com.fiap.foodlink_api.domain.gateway.RestaurantGateway;
import com.fiap.foodlink_api.domain.gateway.UserGateway;
import com.fiap.foodlink_api.domain.gateway.UserTypeGateway;

import java.util.Objects;
import java.util.UUID;

public class UpdateUserUseCase {

	private final UserGateway userGateway;
	private final UserTypeGateway userTypeGateway;
	private final AddressGateway addressGateway;
	private final RestaurantGateway restaurantGateway;

	public UpdateUserUseCase(
			UserGateway userGateway,
			UserTypeGateway userTypeGateway,
			AddressGateway addressGateway,
			RestaurantGateway restaurantGateway
	) {
		this.userGateway = userGateway;
		this.userTypeGateway = userTypeGateway;
		this.addressGateway = addressGateway;
		this.restaurantGateway = restaurantGateway;
	}

	public User execute(UUID id, String name, String email, String login, UUID userTypeId, UUID addressId) {
		User user = userGateway.findById(id)
				.orElseThrow(() -> new UserNotFoundException(id));

		validateRestaurantOwnerTypeChange(user, userTypeId);
		user.updateProfile(name, email, login, userTypeId, addressId);
		validateUniqueEmail(user);
		validateUniqueLogin(user);
		validateRelationships(user.getUserTypeId(), user.getAddressId());

		return userGateway.save(user);
	}

	private void validateUniqueEmail(User user) {
		userGateway.findByEmail(user.getEmail())
				.filter(existingUser -> !Objects.equals(existingUser.getId(), user.getId()))
				.ifPresent(existingUser -> {
					throw new UserAlreadyExistsException("email", user.getEmail());
				});
	}

	private void validateUniqueLogin(User user) {
		userGateway.findByLogin(user.getLogin())
				.filter(existingUser -> !Objects.equals(existingUser.getId(), user.getId()))
				.ifPresent(existingUser -> {
					throw new UserAlreadyExistsException("login", user.getLogin());
				});
	}

	private void validateRelationships(UUID userTypeId, UUID addressId) {
		if (!userTypeGateway.existsById(userTypeId)) {
			throw new UserTypeNotFoundException(userTypeId);
		}

		if (!addressGateway.existsById(addressId)) {
			throw new AddressNotFoundException(addressId);
		}
	}

	private void validateRestaurantOwnerTypeChange(User user, UUID newUserTypeId) {
		if (Objects.equals(user.getUserTypeId(), newUserTypeId)) {
			return;
		}

		if (!restaurantGateway.existsByOwnerId(user.getId())) {
			return;
		}

		UserType newUserType = userTypeGateway.findById(newUserTypeId)
				.orElseThrow(() -> new UserTypeNotFoundException(newUserTypeId));

		if (newUserType.getCode() != UserTypeCode.DONO_RESTAURANTE) {
			throw new DomainException("Tipo de usuario nao pode ser alterado porque o usuario e dono de restaurante.");
		}
	}
}
