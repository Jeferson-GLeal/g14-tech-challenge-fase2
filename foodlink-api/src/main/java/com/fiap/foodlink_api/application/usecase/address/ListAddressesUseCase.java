package com.fiap.foodlink_api.application.usecase.address;

import com.fiap.foodlink_api.domain.entity.Address;
import com.fiap.foodlink_api.domain.gateway.AddressGateway;

import java.util.List;

public class ListAddressesUseCase {

	private final AddressGateway addressGateway;

	public ListAddressesUseCase(AddressGateway addressGateway) {
		this.addressGateway = addressGateway;
	}

	public List<Address> execute() {
		return addressGateway.findAll();
	}
}
