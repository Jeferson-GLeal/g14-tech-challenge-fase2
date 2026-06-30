package com.fiap.foodlink_api.interfaces.controller.dto;

import com.fiap.foodlink_api.infrastructure.persistence.enums.DaysOfWeekEnum;

import java.time.LocalTime;
import java.util.List;

public record WorkingPeriodRequest(List<DaysOfWeekEnum> day,
                                   LocalTime openTime,
                                   LocalTime closeTime) {
}
