package com.fiap.foodlink_api.application.usecase.restaurants;

import com.fiap.foodlink_api.domain.entity.Restaurant;
import com.fiap.foodlink_api.domain.entity.User;
import com.fiap.foodlink_api.domain.entity.UserType;
import com.fiap.foodlink_api.domain.exception.RestaurantAlreadyExistsException;
import com.fiap.foodlink_api.domain.exception.UserNotFoundException;
import com.fiap.foodlink_api.domain.exception.UserTypeNotFoundException;
import com.fiap.foodlink_api.domain.gateway.RestaurantGateway;
import com.fiap.foodlink_api.domain.gateway.UserGateway;
import com.fiap.foodlink_api.domain.gateway.UserTypeGateway;

import java.util.UUID;

public class CreateRestaurantUseCase {

    private final RestaurantGateway restaurantGateway;
    private final UserGateway userGateway;
    private final UserTypeGateway userTypeGateway;

    public CreateRestaurantUseCase(
            RestaurantGateway restaurantGateway,
            UserGateway userGateway,
            UserTypeGateway userTypeGateway
    ) {
        this.restaurantGateway = restaurantGateway;
        this.userGateway = userGateway;
        this.userTypeGateway = userTypeGateway;
    }

    public Restaurant execute(String name, String cnpj, String type, UUID ownerId, UUID addressId) {
        requireOwnerIsRestaurantOwner(ownerId);

        if (restaurantGateway.existsByCnpj(cnpj)) {
            throw new RestaurantAlreadyExistsException("cnpj", cnpj);
        }

        return restaurantGateway.save(Restaurant.create(name, cnpj, type, ownerId, addressId));
    }

    private void requireOwnerIsRestaurantOwner(UUID ownerId) {
        User owner = userGateway.findById(ownerId)
                .orElseThrow(() -> new UserNotFoundException(ownerId));

        UserType userType = userTypeGateway.findById(owner.getUserTypeId())
                .orElseThrow(() -> new UserTypeNotFoundException(owner.getUserTypeId()));

        userType.requireDono();
    }
}
