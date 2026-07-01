package com.fiap.foodlink_api.interfaces.controller.dto;

import java.math.BigDecimal;

public record MenuItemRequest(
		String name,
		String description,
		BigDecimal price,
		String photoPath,
		boolean availableOnlyAtRestaurant
) {
}
