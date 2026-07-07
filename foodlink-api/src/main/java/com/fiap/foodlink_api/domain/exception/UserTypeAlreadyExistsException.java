package com.fiap.foodlink_api.domain.exception;

public class UserTypeAlreadyExistsException extends DomainException {

	public UserTypeAlreadyExistsException(String name) {
		super("Tipo de usuario ja cadastrado: " + name);
	}
}
