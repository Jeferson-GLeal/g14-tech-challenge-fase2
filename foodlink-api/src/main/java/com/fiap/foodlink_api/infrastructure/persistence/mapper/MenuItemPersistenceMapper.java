package com.fiap.foodlink_api.infrastructure.persistence.mapper;

import com.fiap.foodlink_api.domain.entity.MenuItem;
import com.fiap.foodlink_api.infrastructure.persistence.entity.MenuItemJpaEntity;

public final class MenuItemPersistenceMapper {

	private MenuItemPersistenceMapper() {
	}

	public static MenuItem toDomain(MenuItemJpaEntity entity) {
		return new MenuItem(
				entity.getId(),
				entity.getName(),
				entity.getDescription(),
				entity.getPrice(),
				entity.getRestaurantId(),
				entity.getPhotoPath(),
				entity.isAvailableOnlyAtRestaurant(),
				entity.getLastUpdatedAt()
		);
	}

	public static MenuItemJpaEntity toEntity(MenuItem menuItem) {
		return new MenuItemJpaEntity(
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
