package com.fiap.foodlink_api.application.usecase.restaurants;

import com.fiap.foodlink_api.domain.entity.Restaurant;
import com.fiap.foodlink_api.domain.entity.User;
import com.fiap.foodlink_api.domain.entity.WorkingPeriod;
import com.fiap.foodlink_api.domain.exception.RestaurantNotFoundException;
import com.fiap.foodlink_api.domain.exception.UserTypeNotAllowedException;
import com.fiap.foodlink_api.domain.gateway.RestaurantGateway;
import com.fiap.foodlink_api.domain.gateway.WorkingPeriodGateway;
import com.fiap.foodlink_api.infrastructure.persistence.entity.RestaurantJpaEntity;
import com.fiap.foodlink_api.infrastructure.persistence.mapper.RestaurantPersistenceMapper;
import com.fiap.foodlink_api.infrastructure.persistence.mapper.WorkingPeriodPersistenceMapper;
import com.fiap.foodlink_api.interfaces.controller.dto.RestaurantRequest;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public class UpdateRestaurantByIdUseCase {

    private final RestaurantGateway restaurantGateway;
    private final WorkingPeriodGateway workingPeriodGateway;

    public UpdateRestaurantByIdUseCase(RestaurantGateway restaurantGateway, WorkingPeriodGateway workingPeriodGateway) {
        this.restaurantGateway = restaurantGateway;
        this.workingPeriodGateway = workingPeriodGateway;
    }

    public Restaurant execute(Restaurant restaurant, User user, List<WorkingPeriod> workingPeriods, UUID id) {
        validateUserType(user);

        Restaurant savedRestaurant = restaurantGateway.findById(id)
                .orElseThrow(() -> new RestaurantNotFoundException(restaurant.getId()));
        updateWorkingPeriods(savedRestaurant, workingPeriods);

        savedRestaurant.update(restaurant.getName(), restaurant.getCnpj(), restaurant.getType(), user.getId());

        return restaurantGateway.save(savedRestaurant);
    }

    private void updateWorkingPeriods(Restaurant savedRestaurant, List<WorkingPeriod> workingPeriods) {
        List<WorkingPeriod> existingWorkingPeriods = workingPeriodGateway.findByRestaurantId(savedRestaurant.getId());
        workingPeriodGateway.deleteAll(savedRestaurant.getId());
        existingWorkingPeriods.forEach(workingPeriod ->
                workingPeriodGateway.save(WorkingPeriodPersistenceMapper.toJpaEntity(
                        savedRestaurant.getId(),
                        workingPeriod.getDay(),
                        workingPeriod.getOpenTime(),
                        workingPeriod.getCloseTime())));
    }

    private void validateUserType(User user) {
        if(!user.getUserTypeId().equals(UUID.fromString("userTypeIdDonoRestaurante"))) {
            throw new UserTypeNotAllowedException(user.getId());
        }
    }
}
