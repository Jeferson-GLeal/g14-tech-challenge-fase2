package com.fiap.foodlink_api.domain.gateway;

import com.fiap.foodlink_api.domain.entity.Restaurant;
import com.fiap.foodlink_api.domain.entity.User;
import com.fiap.foodlink_api.interfaces.controller.dto.RestaurantRequest;

import java.util.List;
import java.util.UUID;

public interface RestaurantGateway {

    List<Restaurant> findAll();
    Restaurant create(RestaurantRequest restaurant);
    Restaurant findById(UUID id);
    boolean existsById(UUID id);
    void deleteById(UUID id);
    Restaurant update(RestaurantRequest request, Restaurant restaurant, User user);
}
