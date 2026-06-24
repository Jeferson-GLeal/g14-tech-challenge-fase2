package com.fiap.foodlink_api.domain.gateway;

import com.fiap.foodlink_api.domain.entity.Address;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AddressGateway {

	Address save(Address address);

	Optional<Address> findById(UUID id);

	List<Address> findAll();

	boolean existsById(UUID id);

	void deleteById(UUID id);
}
