package com.fiap.foodlink_api.application.usecase.restaurants;

import com.fiap.foodlink_api.domain.entity.Restaurant;
import com.fiap.foodlink_api.domain.gateway.RestaurantGateway;

import java.util.List;

public class ListRestaurantsUseCase {

    private final RestaurantGateway restaurantGateway;

    public ListRestaurantsUseCase(RestaurantGateway restaurantGateway) {
        this.restaurantGateway = restaurantGateway;
    }

    public List<Restaurant> execute() {
        return restaurantGateway.findAll();
    }
}
