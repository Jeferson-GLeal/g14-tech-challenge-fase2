package com.fiap.foodlink_api.infrastructure.persistence.mapper;

import com.fiap.foodlink_api.domain.entity.Address;
import com.fiap.foodlink_api.infrastructure.persistence.entity.AddressJpaEntity;

public class AddressPersistenceMapper {

	private AddressPersistenceMapper() {
	}

	public static Address toDomain(AddressJpaEntity entity) {
		return new Address(
				entity.getId(),
				entity.getStreet(),
				entity.getNumber(),
				entity.getComplement(),
				entity.getDistrict(),
				entity.getCity(),
				entity.getState(),
				entity.getZipCode(),
				entity.getLastUpdatedAt()
		);
	}

	public static AddressJpaEntity toEntity(Address address) {
		return new AddressJpaEntity(
				address.getId(),
				address.getStreet(),
				address.getNumber(),
				address.getComplement(),
				address.getDistrict(),
				address.getCity(),
				address.getState(),
				address.getZipCode(),
				address.getLastUpdatedAt()
		);
	}
}
