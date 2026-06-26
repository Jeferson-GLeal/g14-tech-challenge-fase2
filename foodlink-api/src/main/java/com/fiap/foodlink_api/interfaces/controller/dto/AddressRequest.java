package com.fiap.foodlink_api.interfaces.controller.dto;

public record AddressRequest(
		String street,
		String number,
		String complement,
		String district,
		String city,
		String state,
		String zipCode
) {
}
