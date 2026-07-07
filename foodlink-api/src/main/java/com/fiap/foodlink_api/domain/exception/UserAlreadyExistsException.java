package com.fiap.foodlink_api.domain.exception;

public class UserAlreadyExistsException extends DomainException {

	public UserAlreadyExistsException(String field, String value) {
		super("Usuario ja cadastrado com " + field + ": " + value);
	}
}
