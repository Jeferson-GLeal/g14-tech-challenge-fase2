package com.fiap.foodlink_api.interfaces.controller.mapper;

import com.fiap.foodlink_api.domain.entity.Address;
import com.fiap.foodlink_api.domain.entity.Restaurant;
import com.fiap.foodlink_api.domain.entity.User;
import com.fiap.foodlink_api.domain.entity.WorkingPeriod;
import com.fiap.foodlink_api.interfaces.controller.dto.AddressResponse;
import com.fiap.foodlink_api.interfaces.controller.dto.RestaurantResponse;
import com.fiap.foodlink_api.interfaces.controller.dto.WorkingPeriodResponse;

import java.util.ArrayList;
import java.util.List;

public class RestaurantControllerMapper {

    public RestaurantControllerMapper() {
    }

    public static RestaurantResponse toResponse(
            Restaurant restaurant,
            User owner,
            Address address,
            List<WorkingPeriod> workingPeriod
    ) {
        List<WorkingPeriodResponse> workingPeriodResponses = new ArrayList<>();
        workingPeriod.forEach(wp -> {
            workingPeriodResponses.add(WorkingPeriodControllerMapper.toWorkingPeriodResponse(wp));
        });
        return new RestaurantResponse(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getCnpj(),
                restaurant.getType(),
                owner.getId(),
                owner.getName(),
                toAddressResponse(address),
                restaurant.getLastUpdatedAt(),
                workingPeriodResponses
        );
    }

    private static AddressResponse toAddressResponse(Address address) {
        return new AddressResponse(
                address.getId(),
                address.getStreet(),
                address.getNumber(),
                address.getComplement(),
                address.getDistrict(),
                address.getCity(),
                address.getState(),
                address.getZipCode(),
                address.getLastUpdatedAt()
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
