package com.fiap.foodlink_api.infrastructure.persistence.gateway;

import com.fiap.foodlink_api.domain.entity.Address;
import com.fiap.foodlink_api.domain.gateway.AddressGateway;
import com.fiap.foodlink_api.infrastructure.persistence.mapper.AddressPersistenceMapper;
import com.fiap.foodlink_api.infrastructure.persistence.repository.AddressJpaRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class AddressDatabaseGateway implements AddressGateway {

	private final AddressJpaRepository addressJpaRepository;

	public AddressDatabaseGateway(AddressJpaRepository addressJpaRepository) {
		this.addressJpaRepository = addressJpaRepository;
	}

	@Override
	@Transactional
	public Address save(Address address) {
		return AddressPersistenceMapper.toDomain(
				addressJpaRepository.save(AddressPersistenceMapper.toEntity(address))
		);
	}

	@Override
	public Optional<Address> findById(UUID id) {
		return addressJpaRepository.findById(id)
				.map(AddressPersistenceMapper::toDomain);
	}

	@Override
	public List<Address> findAll() {
		return addressJpaRepository.findAll().stream()
				.map(AddressPersistenceMapper::toDomain)
				.toList();
	}

	@Override
	public boolean existsById(UUID id) {
		return addressJpaRepository.existsById(id);
	}

	@Override
	@Transactional
	public void deleteById(UUID id) {
		addressJpaRepository.deleteById(id);
	}
}
