package com.fiap.foodlink_api.interfaces.controller.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record UserResponse(
		UUID id,
		String name,
		String email,
		String login,
		UUID userTypeId,
		AddressResponse address,
		OffsetDateTime lastUpdatedAt
) {
}
