package com.fiap.foodlink_api.application.usecase.workingperiod;

import com.fiap.foodlink_api.domain.entity.WorkingPeriod;
import com.fiap.foodlink_api.domain.gateway.WorkingPeriodGateway;
import com.fiap.foodlink_api.interfaces.controller.dto.WorkingPeriodRequest;

import java.util.List;
import java.util.UUID;

public class CreateWorkingPeriodUseCase {

    private final WorkingPeriodGateway workingPeriodGateway;

    public CreateWorkingPeriodUseCase(WorkingPeriodGateway workingPeriodGateway) {
        this.workingPeriodGateway = workingPeriodGateway;
    }

    public List<WorkingPeriod> execute(WorkingPeriodRequest request, UUID restaurantId) {
        return workingPeriodGateway.createWorkingPeriod(request, restaurantId);
    }
}
