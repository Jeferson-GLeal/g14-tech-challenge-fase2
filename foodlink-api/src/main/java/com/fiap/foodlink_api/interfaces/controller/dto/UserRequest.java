package com.fiap.foodlink_api.interfaces.controller.dto;

import java.util.UUID;

public record UserRequest(
		String name,
		String email,
		String login,
		String password,
		UUID userTypeId,
		AddressRequest address
) {
}
