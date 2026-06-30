package com.fiap.foodlink_api.domain.exception;

import java.util.UUID;

public class RestaurantNotFoundException extends DomainException {

	public RestaurantNotFoundException(UUID id) {
		super("Restaurante nao encontrado: " + id);
	}
}
