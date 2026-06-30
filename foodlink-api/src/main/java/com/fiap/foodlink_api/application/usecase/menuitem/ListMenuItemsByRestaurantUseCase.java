package com.fiap.foodlink_api.application.usecase.menuitem;

import com.fiap.foodlink_api.domain.entity.MenuItem;
import com.fiap.foodlink_api.domain.exception.RestaurantNotFoundException;
import com.fiap.foodlink_api.domain.gateway.MenuItemGateway;
import com.fiap.foodlink_api.domain.gateway.RestaurantGateway;

import java.util.List;
import java.util.UUID;

public class ListMenuItemsByRestaurantUseCase {

	private final MenuItemGateway menuItemGateway;
	private final RestaurantGateway restaurantGateway;

	public ListMenuItemsByRestaurantUseCase(MenuItemGateway menuItemGateway, RestaurantGateway restaurantGateway) {
		this.menuItemGateway = menuItemGateway;
		this.restaurantGateway = restaurantGateway;
	}

	public List<MenuItem> execute(UUID restaurantId) {
		if (!restaurantGateway.existsById(restaurantId)) {
			throw new RestaurantNotFoundException(restaurantId);
		}

		return menuItemGateway.findByRestaurantId(restaurantId);
	}
}
