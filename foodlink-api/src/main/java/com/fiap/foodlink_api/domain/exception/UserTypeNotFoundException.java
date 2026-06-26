package com.fiap.foodlink_api.domain.exception;

import java.util.UUID;

public class UserTypeNotFoundException extends DomainException {

	public UserTypeNotFoundException(UUID id) {
		super("Tipo de usuario nao encontrado: " + id);
	}
}
