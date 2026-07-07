package com.fiap.foodlink_api.interfaces.controller.dto;

import com.fiap.foodlink_api.domain.entity.DaysOfWeekEnum;

import java.time.LocalTime;

public record WorkingPeriodResponse(
        DaysOfWeekEnum day,
        LocalTime openTime,
        LocalTime closeTime
) {
}
