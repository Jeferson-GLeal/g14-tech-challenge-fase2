package com.fiap.foodlink_api.application.usecase.address;

import com.fiap.foodlink_api.domain.entity.Address;
import com.fiap.foodlink_api.domain.exception.AddressNotFoundException;
import com.fiap.foodlink_api.domain.gateway.AddressGateway;

import java.util.UUID;

public class UpdateAddressUseCase {

	private final AddressGateway addressGateway;

	public UpdateAddressUseCase(AddressGateway addressGateway) {
		this.addressGateway = addressGateway;
	}

	public Address execute(
			UUID id,
			String street,
			String number,
			String complement,
			String district,
			String city,
			String state,
			String zipCode
	) {
		Address address = addressGateway.findById(id)
				.orElseThrow(() -> new AddressNotFoundException(id));

		address.update(street, number, complement, district, city, state, zipCode);
		return addressGateway.save(address);
	}
}
