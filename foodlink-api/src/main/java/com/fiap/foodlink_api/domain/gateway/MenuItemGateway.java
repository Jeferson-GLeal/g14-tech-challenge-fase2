package com.fiap.foodlink_api.domain.gateway;

import com.fiap.foodlink_api.domain.entity.MenuItem;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MenuItemGateway {

	MenuItem save(MenuItem menuItem);

	Optional<MenuItem> findById(UUID id);

	List<MenuItem> findAll();

	List<MenuItem> findByRestaurantId(UUID restaurantId);

	boolean existsById(UUID id);

	void deleteById(UUID id);
}
