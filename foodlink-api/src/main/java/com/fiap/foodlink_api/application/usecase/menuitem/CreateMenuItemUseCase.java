package com.fiap.foodlink_api.application.usecase.menuitem;

import com.fiap.foodlink_api.domain.entity.MenuItem;
import com.fiap.foodlink_api.domain.exception.RestaurantNotFoundException;
import com.fiap.foodlink_api.domain.gateway.MenuItemGateway;
import com.fiap.foodlink_api.domain.gateway.RestaurantGateway;

import java.math.BigDecimal;
import java.util.UUID;

public class CreateMenuItemUseCase {

	private final MenuItemGateway menuItemGateway;
	private final RestaurantGateway restaurantGateway;

	public CreateMenuItemUseCase(MenuItemGateway menuItemGateway, RestaurantGateway restaurantGateway) {
		this.menuItemGateway = menuItemGateway;
		this.restaurantGateway = restaurantGateway;
	}

	public MenuItem execute(
			String name,
			String description,
			BigDecimal price,
			UUID restaurantId,
			String photoPath,
			boolean availableOnlyAtRestaurant
	) {
		MenuItem menuItem = MenuItem.create(
				name,
				description,
				price,
				restaurantId,
				photoPath,
				availableOnlyAtRestaurant
		);

		validateRestaurant(menuItem.getRestaurantId());
		return menuItemGateway.save(menuItem);
	}

	private void validateRestaurant(UUID restaurantId) {
		if (!restaurantGateway.existsById(restaurantId)) {
			throw new RestaurantNotFoundException(restaurantId);
		}
	}
}
