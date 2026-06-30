package com.fiap.foodlink_api.infrastructure.config;

import com.fiap.foodlink_api.application.usecase.restaurants.CreateRestaurantUseCase;
import com.fiap.foodlink_api.application.usecase.restaurants.ListRestaurantsUseCase;
import com.fiap.foodlink_api.domain.gateway.RestaurantGateway;
import org.springframework.context.annotation.Bean;

public class RestaurantUseCaseConfig {

    @Bean
    public ListRestaurantsUseCase listRestaurantsUseCase(RestaurantGateway restaurantGateway) {
        return new ListRestaurantsUseCase(restaurantGateway);
    }

    @Bean
    public CreateRestaurantUseCase createRestaurantUseCase(RestaurantGateway restaurantGateway) {
        return new CreateRestaurantUseCase(restaurantGateway);
    }
}
