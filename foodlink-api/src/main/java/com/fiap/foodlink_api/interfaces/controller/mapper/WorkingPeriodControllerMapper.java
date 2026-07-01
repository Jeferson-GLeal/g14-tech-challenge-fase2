package com.fiap.foodlink_api.interfaces.controller.mapper;

import com.fiap.foodlink_api.domain.entity.Restaurant;
import com.fiap.foodlink_api.domain.entity.User;
import com.fiap.foodlink_api.domain.entity.WorkingPeriod;
import com.fiap.foodlink_api.interfaces.controller.dto.*;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class WorkingPeriodControllerMapper {

    public WorkingPeriodControllerMapper() {
    }

    public static WorkingPeriodResponse toWorkingPeriodResponse(WorkingPeriod workingPeriod) {
        return new WorkingPeriodResponse(
                workingPeriod.getDay(),
                workingPeriod.getOpenTime(),
                workingPeriod.getCloseTime()
        );
    }

    public static List<WorkingPeriod> toWorkingPeriod(WorkingPeriodRequest workingPeriodRequest) {
        List<WorkingPeriod> workingPeriodList = new ArrayList<>();
        workingPeriodRequest.day().forEach(
                day -> workingPeriodList.add(
                        new WorkingPeriod(day, workingPeriodRequest.openTime(), workingPeriodRequest.closeTime())));
        return workingPeriodList;
    }
}
