package com.fiap.foodlink_api.domain.exception;

import java.util.UUID;

public class AddressNotFoundException extends DomainException {

	public AddressNotFoundException(UUID id) {
		super("Endereco nao encontrado: " + id);
	}
}
