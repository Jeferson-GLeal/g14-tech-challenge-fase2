package com.fiap.foodlink_api.application.usecase.restaurants;

import com.fiap.foodlink_api.domain.entity.Restaurant;
import com.fiap.foodlink_api.domain.entity.User;
import com.fiap.foodlink_api.domain.gateway.RestaurantGateway;
import com.fiap.foodlink_api.interfaces.controller.dto.RestaurantRequest;

import java.util.UUID;

public class UpdateRestaurantByIdUseCase {

    private final RestaurantGateway restaurantGateway;

    public UpdateRestaurantByIdUseCase(RestaurantGateway restaurantGateway) {
        this.restaurantGateway = restaurantGateway;
    }

    public Restaurant execute(RestaurantRequest request, Restaurant restaurant, User user) {
        return restaurantGateway.update(request, restaurant, user);
    }
}
