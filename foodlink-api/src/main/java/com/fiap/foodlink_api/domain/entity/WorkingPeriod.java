package com.fiap.foodlink_api.domain.entity;

import com.fiap.foodlink_api.infrastructure.persistence.enums.DaysOfWeekEnum;

import java.time.LocalTime;
import java.time.OffsetDateTime;

public class WorkingPeriod {
    private DaysOfWeekEnum day;
    private LocalTime openTime;
    private LocalTime closeTime;

    public WorkingPeriod(DaysOfWeekEnum days, LocalTime openTime, LocalTime closeTime) {
        this.day = days;
        this.openTime = openTime;
        this.closeTime = closeTime;
    }

    public DaysOfWeekEnum getDay() {
        return day;
    }

    public void setDay(DaysOfWeekEnum day) {
        this.day = day;
    }

    public LocalTime getOpenTime() {
        return openTime;
    }

    public void setOpenTime(LocalTime openTime) {
        this.openTime = openTime;
    }

    public LocalTime getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(LocalTime closeTime) {
        this.closeTime = closeTime;
    }
}
