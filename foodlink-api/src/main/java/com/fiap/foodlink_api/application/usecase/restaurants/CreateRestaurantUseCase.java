package com.fiap.foodlink_api.application.usecase.restaurants;

import com.fiap.foodlink_api.domain.entity.Restaurant;
import com.fiap.foodlink_api.domain.gateway.RestaurantGateway;
import com.fiap.foodlink_api.interfaces.controller.dto.RestaurantRequest;

public class CreateRestaurantUseCase {

    private final RestaurantGateway restaurantGateway;

    public CreateRestaurantUseCase(RestaurantGateway restaurantGateway) {
        this.restaurantGateway = restaurantGateway;
    }

    public Restaurant execute(RestaurantRequest restaurant) {return restaurantGateway.create(restaurant);}

}
