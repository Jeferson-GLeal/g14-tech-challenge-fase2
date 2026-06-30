package com.fiap.foodlink_api.domain.exception;

import java.util.UUID;

public class WorkingPeriorAlreadyExistsException extends DomainException {

	public WorkingPeriorAlreadyExistsException(UUID id) {
		super("Horário de funcionamento já cadastrado para o restaurante: " + id);
	}
}
