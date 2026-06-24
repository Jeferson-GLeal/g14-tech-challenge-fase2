package com.fiap.foodlink_api.domain.exception;

import java.util.UUID;

public class UserNotFoundException extends DomainException {

	public UserNotFoundException(UUID id) {
		super("Usuario nao encontrado: " + id);
	}
}
