package com.fiap.foodlink_api.application.usecase.address;

import com.fiap.foodlink_api.domain.entity.Address;
import com.fiap.foodlink_api.domain.gateway.AddressGateway;

public class CreateAddressUseCase {

	private final AddressGateway addressGateway;

	public CreateAddressUseCase(AddressGateway addressGateway) {
		this.addressGateway = addressGateway;
	}

	public Address execute(
			String street,
			String number,
			String complement,
			String district,
			String city,
			String state,
			String zipCode
	) {
		Address address = Address.create(street, number, complement, district, city, state, zipCode);
		return addressGateway.save(address);
	}
}
