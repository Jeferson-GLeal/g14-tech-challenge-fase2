package com.fiap.foodlink_api.infrastructure.config;

import com.fiap.foodlink_api.application.usecase.restaurants.CreateRestaurantUseCase;
import com.fiap.foodlink_api.application.usecase.restaurants.ListRestaurantsUseCase;
import com.fiap.foodlink_api.application.usecase.workingperiod.GetWorkingPeriodByIdUseCase;
import com.fiap.foodlink_api.domain.gateway.RestaurantGateway;
import com.fiap.foodlink_api.domain.gateway.WorkingPeriodGateway;
import org.springframework.context.annotation.Bean;

public class WorkingPeriodUseCaseConfig {

    @Bean
    public GetWorkingPeriodByIdUseCase getWorkingPeriodByIdUseCase(WorkingPeriodGateway workingPeriodGateway) {
        return new GetWorkingPeriodByIdUseCase(workingPeriodGateway);
    }
}
