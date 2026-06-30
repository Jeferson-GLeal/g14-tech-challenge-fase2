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

    public static RestaurantJpaEntity toEntity(RestaurantRequest restaurantRequest) {
        return new RestaurantJpaEntity(
                restaurantRequest.name(),
                restaurantRequest.cnpj(),
                restaurantRequest.type(),
                restaurantRequest.ownerId(),
                OffsetDateTime.now()
        );
    }
}



