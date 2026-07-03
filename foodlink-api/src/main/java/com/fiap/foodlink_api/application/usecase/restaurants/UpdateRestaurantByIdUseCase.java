package com.fiap.foodlink_api.application.usecase.restaurants;

import com.fiap.foodlink_api.domain.entity.Restaurant;
import com.fiap.foodlink_api.domain.entity.User;
import com.fiap.foodlink_api.domain.entity.UserType;
import com.fiap.foodlink_api.domain.entity.WorkingPeriod;
import com.fiap.foodlink_api.domain.exception.RestaurantNotFoundException;
import com.fiap.foodlink_api.domain.exception.UserNotFoundException;
import com.fiap.foodlink_api.domain.exception.UserTypeNotFoundException;
import com.fiap.foodlink_api.domain.gateway.RestaurantGateway;
import com.fiap.foodlink_api.domain.gateway.UserGateway;
import com.fiap.foodlink_api.domain.gateway.UserTypeGateway;
import com.fiap.foodlink_api.domain.gateway.WorkingPeriodGateway;
import com.fiap.foodlink_api.infrastructure.persistence.mapper.WorkingPeriodPersistenceMapper;

import java.util.List;
import java.util.UUID;

public class UpdateRestaurantByIdUseCase {

    private final RestaurantGateway restaurantGateway;
    private final WorkingPeriodGateway workingPeriodGateway;
    private final UserGateway userGateway;
    private final UserTypeGateway userTypeGateway;

    public UpdateRestaurantByIdUseCase(
            RestaurantGateway restaurantGateway,
            WorkingPeriodGateway workingPeriodGateway,
            UserGateway userGateway,
            UserTypeGateway userTypeGateway
    ) {
        this.restaurantGateway = restaurantGateway;
        this.workingPeriodGateway = workingPeriodGateway;
        this.userGateway = userGateway;
        this.userTypeGateway = userTypeGateway;
    }

    public Restaurant execute(
            UUID id,
            String name,
            String cnpj,
            String type,
            UUID ownerId,
            List<WorkingPeriod> workingPeriods
    ) {
        requireOwnerIsRestaurantOwner(ownerId);

        Restaurant savedRestaurant = restaurantGateway.findById(id)
                .orElseThrow(() -> new RestaurantNotFoundException(id));

        replaceWorkingPeriods(savedRestaurant.getId(), workingPeriods);
        savedRestaurant.update(name, cnpj, type, ownerId);

        return restaurantGateway.save(savedRestaurant);
    }

    private void replaceWorkingPeriods(UUID restaurantId, List<WorkingPeriod> workingPeriods) {
        workingPeriodGateway.deleteAll(restaurantId);
        workingPeriods.forEach(workingPeriod -> workingPeriodGateway.save(
                WorkingPeriodPersistenceMapper.toJpaEntity(
                        restaurantId,
                        workingPeriod.getDay(),
                        workingPeriod.getOpenTime(),
                        workingPeriod.getCloseTime()
                )
        ));
    }

    private void requireOwnerIsRestaurantOwner(UUID ownerId) {
        User owner = userGateway.findById(ownerId)
                .orElseThrow(() -> new UserNotFoundException(ownerId));

        UserType userType = userTypeGateway.findById(owner.getUserTypeId())
                .orElseThrow(() -> new UserTypeNotFoundException(owner.getUserTypeId()));

        userType.requireDono();
    }
}
