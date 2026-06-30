package com.fiap.foodlink_api.interfaces.controller.mapper;

import com.fiap.foodlink_api.domain.entity.Restaurant;
import com.fiap.foodlink_api.domain.entity.User;
import com.fiap.foodlink_api.domain.entity.WorkingPeriod;
import com.fiap.foodlink_api.interfaces.controller.dto.RestaurantResponse;
import com.fiap.foodlink_api.interfaces.controller.dto.UserResponse;
import com.fiap.foodlink_api.interfaces.controller.dto.WorkingPeriodResponse;

import java.util.ArrayList;
import java.util.List;

public class RestaurantControllerMapper {

    public RestaurantControllerMapper() {
    }

    public static RestaurantResponse toResponse(Restaurant restaurant, User owner, List<WorkingPeriod> workingPeriod) {
        List<WorkingPeriodResponse> workingPeriodResponses = new ArrayList<>();
        workingPeriod.forEach(wp -> {
            workingPeriodResponses.add(toWorkingPeriodResponse(wp));
        });
        return new RestaurantResponse(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getCnpj(),
                restaurant.getType(),
                toUserResponse(owner),
                restaurant.getLastUpdatedAt(),
                workingPeriodResponses
        );
    }

    public static UserResponse toUserResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getLogin(),
                user.getUserTypeId(),
                null,
                user.getLastUpdatedAt()
        );
    }

    public static WorkingPeriodResponse toWorkingPeriodResponse(WorkingPeriod workingPeriod) {
        return new WorkingPeriodResponse(
                workingPeriod.getDay(),
                workingPeriod.getOpenTime(),
                workingPeriod.getCloseTime()
        );
    }
}
