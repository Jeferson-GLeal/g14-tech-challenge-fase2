package com.fiap.foodlink_api.application.usecase.restaurants;

import com.fiap.foodlink_api.domain.entity.Restaurant;
import com.fiap.foodlink_api.domain.entity.User;
import com.fiap.foodlink_api.domain.exception.RestaurantNotFoundException;
import com.fiap.foodlink_api.domain.exception.UserTypeNotAllowedException;
import com.fiap.foodlink_api.domain.gateway.RestaurantGateway;
import com.fiap.foodlink_api.infrastructure.persistence.entity.RestaurantJpaEntity;
import com.fiap.foodlink_api.infrastructure.persistence.mapper.RestaurantPersistenceMapper;
import com.fiap.foodlink_api.interfaces.controller.dto.RestaurantRequest;

import java.time.OffsetDateTime;
import java.util.UUID;

public class UpdateRestaurantByIdUseCase {

    private final RestaurantGateway restaurantGateway;

    public UpdateRestaurantByIdUseCase(RestaurantGateway restaurantGateway) {
        this.restaurantGateway = restaurantGateway;
    }

    public Restaurant execute(RestaurantRequest request, Restaurant restaurant, User user) {
        Restaurant savedRestaurant = restaurantGateway.findById(restaurant.getId())
                .orElseThrow(() -> new RestaurantNotFoundException(restaurant.getId()));

        savedRestaurant.setCnpj(request.cnpj());
        savedRestaurant.setName(request.name());
        savedRestaurant.setType(request.type());
        savedRestaurant.setOwnerId(user.getId());
        savedRestaurant.setLastUpdatedAt(OffsetDateTime.now());

        validateUserType(user);

        return restaurantGateway.save(savedRestaurant);
    }

    private void validateUserType(User user) {
        if(!user.getUserTypeId().equals(UUID.fromString("userTypeIdDonoRestaurante"))) {
            throw new UserTypeNotAllowedException(user.getId());
        }
    }
}
