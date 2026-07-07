package com.fiap.foodlink_api.infrastructure.persistence.mapper;

import com.fiap.foodlink_api.domain.entity.Restaurant;
import com.fiap.foodlink_api.infrastructure.persistence.entity.RestaurantJpaEntity;

public final class RestaurantPersistenceMapper {

    private RestaurantPersistenceMapper() {
    }

    public static Restaurant toDomain(RestaurantJpaEntity entity) {
        return new Restaurant(
                entity.getId(),
                entity.getName(),
                entity.getCnpj(),
                entity.getType(),
                entity.getOwner(),
                entity.getAddressId(),
                entity.getLastUpdatedAt()
        );
    }

    public static RestaurantJpaEntity toEntity(Restaurant restaurant) {
        return new RestaurantJpaEntity(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getCnpj(),
                restaurant.getType(),
                restaurant.getOwnerId(),
                restaurant.getAddressId(),
                restaurant.getLastUpdatedAt()
        );
    }
}
