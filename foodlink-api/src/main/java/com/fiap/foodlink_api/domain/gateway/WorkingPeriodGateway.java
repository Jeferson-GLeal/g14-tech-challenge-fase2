package com.fiap.foodlink_api.domain.gateway;

import com.fiap.foodlink_api.domain.entity.WorkingPeriod;
import com.fiap.foodlink_api.interfaces.controller.dto.WorkingPeriodRequest;

import java.util.List;
import java.util.UUID;

public interface WorkingPeriodGateway {

    List<WorkingPeriod> findWorkingPeriodByRestaurantId(UUID restaurantId);
    List<WorkingPeriod> createWorkingPeriod(WorkingPeriodRequest request, UUID restaurantId);
}
