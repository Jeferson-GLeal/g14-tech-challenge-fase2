package com.fiap.foodlink_api.application.usecase.restaurants;

import com.fiap.foodlink_api.domain.entity.Restaurant;
import com.fiap.foodlink_api.domain.exception.RestaurantAlreadyExistsException;
import com.fiap.foodlink_api.domain.gateway.RestaurantGateway;
import com.fiap.foodlink_api.infrastructure.persistence.mapper.RestaurantPersistenceMapper;
import com.fiap.foodlink_api.interfaces.controller.dto.RestaurantRequest;

public class CreateRestaurantUseCase {

    private final RestaurantGateway restaurantGateway;

    public CreateRestaurantUseCase(RestaurantGateway restaurantGateway) {
        this.restaurantGateway = restaurantGateway;
    }

    public Restaurant execute(RestaurantRequest restaurant) {
        boolean existsRestaurant = restaurantGateway.existsByCnpj(restaurant.cnpj());
        if (existsRestaurant) {
            throw new RestaurantAlreadyExistsException("cnpj", restaurant.cnpj());
        }
        return restaurantGateway.save(RestaurantPersistenceMapper.fromRequestToDomain(restaurant));
    }
}
