package com.fiap.foodlink_api.domain.exception;

import java.util.UUID;

public class WorkingPeriodNotFoundException extends DomainException {

	public WorkingPeriodNotFoundException(UUID id) {
		super("Horário de funcionamento não encontrado para o restaurante: " + id);
	}
}
