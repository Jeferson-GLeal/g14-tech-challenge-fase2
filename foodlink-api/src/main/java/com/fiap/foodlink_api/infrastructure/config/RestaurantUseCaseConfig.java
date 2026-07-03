package com.fiap.foodlink_api.infrastructure.config;

import com.fiap.foodlink_api.application.usecase.restaurants.*;
import com.fiap.foodlink_api.domain.gateway.RestaurantGateway;
import com.fiap.foodlink_api.domain.gateway.UserGateway;
import com.fiap.foodlink_api.domain.gateway.UserTypeGateway;
import com.fiap.foodlink_api.domain.gateway.WorkingPeriodGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RestaurantUseCaseConfig {

    @Bean
    public ListRestaurantsUseCase listRestaurantsUseCase(RestaurantGateway restaurantGateway) {
        return new ListRestaurantsUseCase(restaurantGateway);
    }

    @Bean
    public CreateRestaurantUseCase createRestaurantUseCase(
            RestaurantGateway restaurantGateway,
            UserGateway userGateway,
            UserTypeGateway userTypeGateway
    ) {
        return new CreateRestaurantUseCase(restaurantGateway, userGateway, userTypeGateway);
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
    public UpdateRestaurantByIdUseCase updateRestaurantByIdUseCase(
            RestaurantGateway restaurantGateway,
            WorkingPeriodGateway workingPeriodGateway,
            UserGateway userGateway,
            UserTypeGateway userTypeGateway
    ) {
        return new UpdateRestaurantByIdUseCase(restaurantGateway, workingPeriodGateway, userGateway, userTypeGateway);
    }
}
