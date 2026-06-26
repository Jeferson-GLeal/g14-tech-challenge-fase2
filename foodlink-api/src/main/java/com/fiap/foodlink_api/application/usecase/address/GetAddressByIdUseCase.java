package com.fiap.foodlink_api.application.usecase.address;

import com.fiap.foodlink_api.domain.entity.Address;
import com.fiap.foodlink_api.domain.exception.AddressNotFoundException;
import com.fiap.foodlink_api.domain.gateway.AddressGateway;

import java.util.UUID;

public class GetAddressByIdUseCase {

	private final AddressGateway addressGateway;

	public GetAddressByIdUseCase(AddressGateway addressGateway) {
		this.addressGateway = addressGateway;
	}

	public Address execute(UUID id) {
		return addressGateway.findById(id)
				.orElseThrow(() -> new AddressNotFoundException(id));
	}
}
