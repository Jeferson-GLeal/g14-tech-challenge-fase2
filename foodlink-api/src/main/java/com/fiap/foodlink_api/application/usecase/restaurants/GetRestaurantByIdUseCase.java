package com.fiap.foodlink_api.application.usecase.restaurants;

import com.fiap.foodlink_api.domain.entity.Restaurant;
import com.fiap.foodlink_api.domain.exception.RestaurantNotFoundException;
import com.fiap.foodlink_api.domain.gateway.RestaurantGateway;

import java.util.UUID;

public class GetRestaurantByIdUseCase {

    private final RestaurantGateway restaurantGateway;

    public GetRestaurantByIdUseCase(RestaurantGateway restaurantGateway) {
        this.restaurantGateway = restaurantGateway;
    }

    public Restaurant execute(UUID id) {
        return restaurantGateway.findById(id).orElseThrow(() -> new RestaurantNotFoundException(id));
    }
}
