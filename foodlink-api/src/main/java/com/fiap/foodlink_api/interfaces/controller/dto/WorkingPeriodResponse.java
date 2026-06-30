package com.fiap.foodlink_api.interfaces.controller.dto;

import com.fiap.foodlink_api.infrastructure.persistence.enums.DaysOfWeekEnum;

import java.time.LocalTime;

public record WorkingPeriodResponse(
        DaysOfWeekEnum day,
        LocalTime openTime,
        LocalTime closeTime
) {
}
