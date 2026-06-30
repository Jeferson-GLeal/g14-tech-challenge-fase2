package com.fiap.foodlink_api.infrastructure.config;

import com.fiap.foodlink_api.application.usecase.restaurants.*;
import com.fiap.foodlink_api.domain.gateway.RestaurantGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RestaurantUseCaseConfig {

    @Bean
    public ListRestaurantsUseCase listRestaurantsUseCase(RestaurantGateway restaurantGateway) {
        return new ListRestaurantsUseCase(restaurantGateway);
    }

    @Bean
    public CreateRestaurantUseCase createRestaurantUseCase(RestaurantGateway restaurantGateway) {
        return new CreateRestaurantUseCase(restaurantGateway);
    }

    @Bean
    public DeleteRestauranteByIdUseCase deleteRestauranteByIdUseCase(RestaurantGateway restaurantGateway) {
        return new DeleteRestauranteByIdUseCase(restaurantGateway);
    }

    @Bean
    public GetRestaurantByIdUseCase getRestaurantByIdUseCase(RestaurantGateway restaurantGateway) {
        return new GetRestaurantByIdUseCase(restaurantGateway);
    }

    @Bean
    public UpdateRestaurantByIdUseCase updateRestaurantByIdUseCase(RestaurantGateway restaurantGateway) {
        return new UpdateRestaurantByIdUseCase(restaurantGateway);
    }
}
