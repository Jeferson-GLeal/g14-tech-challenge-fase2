package com.fiap.foodlink_api.infrastructure.config;

import com.fiap.foodlink_api.application.usecase.workingperiod.CreateWorkingPeriodUseCase;
import com.fiap.foodlink_api.application.usecase.workingperiod.GetWorkingPeriodByIdUseCase;
import com.fiap.foodlink_api.domain.gateway.WorkingPeriodGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WorkingPeriodUseCaseConfig {

    @Bean
    public GetWorkingPeriodByIdUseCase getWorkingPeriodByIdUseCase(WorkingPeriodGateway workingPeriodGateway) {
        return new GetWorkingPeriodByIdUseCase(workingPeriodGateway);
    }

    @Bean
    public CreateWorkingPeriodUseCase createWorkingPeriodUseCase(WorkingPeriodGateway workingPeriodGateway){
        return new CreateWorkingPeriodUseCase(workingPeriodGateway);
    }
}
