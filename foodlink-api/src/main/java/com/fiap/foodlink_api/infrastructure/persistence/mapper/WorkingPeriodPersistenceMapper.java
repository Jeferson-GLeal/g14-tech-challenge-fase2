package com.fiap.foodlink_api.infrastructure.persistence.mapper;

import com.fiap.foodlink_api.domain.entity.WorkingPeriod;
import com.fiap.foodlink_api.infrastructure.persistence.entity.WorkingPeriodJpaEntity;
import com.fiap.foodlink_api.infrastructure.persistence.enums.DaysOfWeekEnum;
import com.fiap.foodlink_api.interfaces.controller.dto.WorkingPeriodRequest;

import java.time.LocalTime;
import java.util.UUID;

public class WorkingPeriodPersistenceMapper {

    public WorkingPeriodPersistenceMapper() {
    }

    public static WorkingPeriod toDomain(WorkingPeriodJpaEntity entity) {
        return new WorkingPeriod(
                entity.getDay(),
                entity.getOpenTime(),
                entity.getCloseTime()
        );
    }

    public static WorkingPeriodJpaEntity toJpaEntity(UUID restaurantId, DaysOfWeekEnum day, LocalTime openTime, LocalTime closeTime) {
        return new WorkingPeriodJpaEntity(
                restaurantId,
                day,
                openTime,
                closeTime
        );
    }
}
