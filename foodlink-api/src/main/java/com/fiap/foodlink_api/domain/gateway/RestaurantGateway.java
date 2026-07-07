package com.fiap.foodlink_api.domain.gateway;

import com.fiap.foodlink_api.domain.entity.Restaurant;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RestaurantGateway {
    List<Restaurant> findAll();
    Restaurant save(Restaurant restaurant);
    Optional<Restaurant> findById(UUID id);
    boolean existsById(UUID id);
    boolean existsByCnpj(String cnpj);
    boolean existsByOwnerId(UUID ownerId);
    void deleteById(UUID id);
}
