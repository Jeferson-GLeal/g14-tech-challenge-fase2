package com.fiap.foodlink_api.interfaces.controller.mapper;

import com.fiap.foodlink_api.domain.entity.MenuItem;
import com.fiap.foodlink_api.interfaces.controller.dto.MenuItemResponse;

public final class MenuItemControllerMapper {

	private MenuItemControllerMapper() {
	}

	public static MenuItemResponse toResponse(MenuItem menuItem) {
		return new MenuItemResponse(
				menuItem.getId(),
				menuItem.getName(),
				menuItem.getDescription(),
				menuItem.getPrice(),
				menuItem.getRestaurantId(),
				menuItem.getPhotoPath(),
				menuItem.isAvailableOnlyAtRestaurant(),
				menuItem.getLastUpdatedAt()
		);
	}
}
