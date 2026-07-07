package com.fiap.foodlink_api.interfaces.controller.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record MenuItemResponse(
		UUID id,
		String name,
		String description,
		BigDecimal price,
		UUID restaurantId,
		String photoPath,
		boolean availableOnlyAtRestaurant,
		OffsetDateTime lastUpdatedAt
) {
}
