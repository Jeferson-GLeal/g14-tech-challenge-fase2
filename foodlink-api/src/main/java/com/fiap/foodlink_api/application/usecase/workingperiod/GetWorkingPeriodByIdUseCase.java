package com.fiap.foodlink_api.application.usecase.workingperiod;

import com.fiap.foodlink_api.domain.entity.WorkingPeriod;
import com.fiap.foodlink_api.domain.exception.WorkingPeriodNotFoundException;
import com.fiap.foodlink_api.domain.gateway.WorkingPeriodGateway;

import java.util.List;
import java.util.UUID;

public class GetWorkingPeriodByIdUseCase {

    private final WorkingPeriodGateway workingPeriodGateway;

    public GetWorkingPeriodByIdUseCase(WorkingPeriodGateway workingPeriodGateway) {
        this.workingPeriodGateway = workingPeriodGateway;
    }

    public List<WorkingPeriod> execute(UUID restaurantId){
        List<WorkingPeriod> workingPeriods = workingPeriodGateway.findWorkingPeriodByRestaurantId(restaurantId);
        if (workingPeriods.isEmpty()) {
            throw new WorkingPeriodNotFoundException(restaurantId);
        }
        return workingPeriods;
    }
}
