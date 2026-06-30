package com.fiap.foodlink_api.application.usecase.restaurants;

import com.fiap.foodlink_api.domain.exception.RestaurantNotFoundException;
import com.fiap.foodlink_api.domain.gateway.RestaurantGateway;

import java.util.UUID;

public class DeleteRestauranteByIdUseCase {
    private final RestaurantGateway restaurantGateway;

    public DeleteRestauranteByIdUseCase(RestaurantGateway restaurantGateway) {
        this.restaurantGateway = restaurantGateway;
    }

    public void execute(UUID id) {
        if (!restaurantGateway.existsById(id)) {
            throw new RestaurantNotFoundException(id);
        }
        restaurantGateway.deleteById(id);
    }
}
