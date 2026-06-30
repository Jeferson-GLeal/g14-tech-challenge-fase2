package com.fiap.foodlink_api.domain.gateway;

import com.fiap.foodlink_api.domain.entity.WorkingPeriod;
import com.fiap.foodlink_api.infrastructure.persistence.entity.WorkingPeriodJpaEntity;

import java.util.List;
import java.util.UUID;

public interface WorkingPeriodGateway {

    boolean existsByRestaurantId(UUID restaurantId);
    List<WorkingPeriod> findWorkingPeriodByRestaurantId(UUID restaurantId);
    WorkingPeriod save(WorkingPeriodJpaEntity workingPeriod);
}
