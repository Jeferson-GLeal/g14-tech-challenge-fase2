package com.fiap.foodlink_api.infrastructure.persistence.mapper;

import com.fiap.foodlink_api.domain.entity.Restaurant;
import com.fiap.foodlink_api.infrastructure.persistence.entity.RestaurantJpaEntity;
import com.fiap.foodlink_api.interfaces.controller.dto.RestaurantRequest;

import java.time.OffsetDateTime;

public class RestaurantPersistenceMapper {

    public RestaurantPersistenceMapper() {
    }

    public static Restaurant toDomain(RestaurantJpaEntity entity) {
        return new Restaurant(
                entity.getId(),
                entity.getName(),
                entity.getCnpj(),
                entity.getType(),
                entity.getLastUpdatedAt(),
                entity.getOwner()
        );
    }

    public static RestaurantJpaEntity toEntity(Restaurant restaurant) {
        return new RestaurantJpaEntity(
                restaurant.getName(),
                restaurant.getCnpj(),
                restaurant.getType(),
                restaurant.getOwnerId(),
                OffsetDateTime.now()
        );
    }

    public static Restaurant fromRequestToDomain(RestaurantRequest restaurantRequest) {
        return new Restaurant(restaurantRequest.name(),
                restaurantRequest.cnpj(),
                restaurantRequest.type(),
                OffsetDateTime.now(),
                restaurantRequest.ownerId());
    }
}

