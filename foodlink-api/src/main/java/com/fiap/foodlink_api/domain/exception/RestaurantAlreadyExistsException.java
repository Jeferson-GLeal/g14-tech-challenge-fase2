package com.fiap.foodlink_api.domain.exception;

public class RestaurantAlreadyExistsException extends DomainException {

	public RestaurantAlreadyExistsException(String field, String value) {
		super("Restaurante ja cadastrado com " + field + ": " + value);
	}
}
