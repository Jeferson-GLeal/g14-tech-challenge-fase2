package com.fiap.foodlink_api.application.usecase.workingperiod;

import com.fiap.foodlink_api.domain.entity.WorkingPeriod;
import com.fiap.foodlink_api.domain.exception.WorkingPeriorAlreadyExistsException;
import com.fiap.foodlink_api.domain.gateway.WorkingPeriodGateway;
import com.fiap.foodlink_api.infrastructure.persistence.entity.WorkingPeriodJpaEntity;
import com.fiap.foodlink_api.infrastructure.persistence.mapper.WorkingPeriodPersistenceMapper;
import com.fiap.foodlink_api.interfaces.controller.dto.WorkingPeriodRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CreateWorkingPeriodUseCase {

    private final WorkingPeriodGateway workingPeriodGateway;

    public CreateWorkingPeriodUseCase(WorkingPeriodGateway workingPeriodGateway) {
        this.workingPeriodGateway = workingPeriodGateway;
    }

    public List<WorkingPeriod> execute(WorkingPeriodRequest request, UUID restaurantId) {
        validateIfWorkingPeriodAlreadyExists(restaurantId);
        List<WorkingPeriod> workingPeriodList = new ArrayList<>();

        request.day().forEach(day -> {
            WorkingPeriodJpaEntity workingPeriodJpaEntity =
                    WorkingPeriodPersistenceMapper.toJpaEntity(restaurantId, day, request.openTime(), request.closeTime());
            workingPeriodList.add(workingPeriodGateway.save(workingPeriodJpaEntity));
        });

        return workingPeriodList;
    }

    private void validateIfWorkingPeriodAlreadyExists(UUID restaurantId) {
        boolean existedWorkingPeriod = workingPeriodGateway.existsByRestaurantId(restaurantId);
        if (existedWorkingPeriod) {
            throw new WorkingPeriorAlreadyExistsException(restaurantId);
        }
    }
}