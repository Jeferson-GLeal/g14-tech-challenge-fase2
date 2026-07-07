package com.fiap.foodlink_api.application.usecase.menuitem;

import com.fiap.foodlink_api.domain.entity.MenuItem;
import com.fiap.foodlink_api.domain.exception.MenuItemNotFoundException;
import com.fiap.foodlink_api.domain.exception.RestaurantNotFoundException;
import com.fiap.foodlink_api.domain.gateway.MenuItemGateway;
import com.fiap.foodlink_api.domain.gateway.RestaurantGateway;

import java.util.Objects;
import java.util.UUID;

public class DeleteMenuItemUseCase {

	private final MenuItemGateway menuItemGateway;
	private final RestaurantGateway restaurantGateway;

	public DeleteMenuItemUseCase(MenuItemGateway menuItemGateway, RestaurantGateway restaurantGateway) {
		this.menuItemGateway = menuItemGateway;
		this.restaurantGateway = restaurantGateway;
	}

	public void execute(UUID restaurantId, UUID id) {
		validateRestaurant(restaurantId);

		MenuItem menuItem = menuItemGateway.findById(id)
				.orElseThrow(() -> new MenuItemNotFoundException(id));

		if (!Objects.equals(menuItem.getRestaurantId(), restaurantId)) {
			throw new MenuItemNotFoundException(id);
		}

		menuItemGateway.deleteById(id);
	}

	private void validateRestaurant(UUID restaurantId) {
		if (!restaurantGateway.existsById(restaurantId)) {
			throw new RestaurantNotFoundException(restaurantId);
		}
	}
}
