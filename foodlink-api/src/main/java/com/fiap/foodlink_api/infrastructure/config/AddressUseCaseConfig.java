package com.fiap.foodlink_api.infrastructure.config;

import com.fiap.foodlink_api.application.usecase.address.CreateAddressUseCase;
import com.fiap.foodlink_api.application.usecase.address.DeleteAddressUseCase;
import com.fiap.foodlink_api.application.usecase.address.GetAddressByIdUseCase;
import com.fiap.foodlink_api.application.usecase.address.ListAddressesUseCase;
import com.fiap.foodlink_api.application.usecase.address.UpdateAddressUseCase;
import com.fiap.foodlink_api.domain.gateway.AddressGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AddressUseCaseConfig {

	@Bean
	public CreateAddressUseCase createAddressUseCase(AddressGateway addressGateway) {
		return new CreateAddressUseCase(addressGateway);
	}

	@Bean
	public GetAddressByIdUseCase getAddressByIdUseCase(AddressGateway addressGateway) {
		return new GetAddressByIdUseCase(addressGateway);
	}

	@Bean
	public ListAddressesUseCase listAddressesUseCase(AddressGateway addressGateway) {
		return new ListAddressesUseCase(addressGateway);
	}

	@Bean
	public UpdateAddressUseCase updateAddressUseCase(AddressGateway addressGateway) {
		return new UpdateAddressUseCase(addressGateway);
	}

	@Bean
	public DeleteAddressUseCase deleteAddressUseCase(AddressGateway addressGateway) {
		return new DeleteAddressUseCase(addressGateway);
	}
}
