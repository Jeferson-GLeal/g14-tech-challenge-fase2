package com.fiap.foodlink_api.application.usecase.address;

import com.fiap.foodlink_api.domain.exception.AddressNotFoundException;
import com.fiap.foodlink_api.domain.gateway.AddressGateway;

import java.util.UUID;

public class DeleteAddressUseCase {

	private final AddressGateway addressGateway;

	public DeleteAddressUseCase(AddressGateway addressGateway) {
		this.addressGateway = addressGateway;
	}

	public void execute(UUID id) {
		if (!addressGateway.existsById(id)) {
			throw new AddressNotFoundException(id);
		}

		addressGateway.deleteById(id);
	}
}
