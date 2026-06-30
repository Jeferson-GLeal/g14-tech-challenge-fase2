package com.fiap.foodlink_api.domain.exception;

import java.util.UUID;

public class MenuItemNotFoundException extends DomainException {

	public MenuItemNotFoundException(UUID id) {
		super("Item do cardapio nao encontrado: " + id);
	}
}
