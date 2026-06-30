package com.fiap.foodlink_api.domain.exception;

import java.util.UUID;

public class UserTypeNotAllowedException extends DomainException {

	public UserTypeNotAllowedException(UUID id) {
		super("Tipo de usuario nao permitido: " + id);
	}
}
