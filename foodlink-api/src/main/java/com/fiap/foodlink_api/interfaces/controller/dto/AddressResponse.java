package com.fiap.foodlink_api.interfaces.controller.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record AddressResponse(
		UUID id,
		String street,
		String number,
		String complement,
		String district,
		String city,
		String state,
		String zipCode,
		OffsetDateTime lastUpdatedAt
) {
}
