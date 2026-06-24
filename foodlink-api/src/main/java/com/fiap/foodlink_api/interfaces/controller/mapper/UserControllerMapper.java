package com.fiap.foodlink_api.interfaces.controller.mapper;

import com.fiap.foodlink_api.domain.entity.Address;
import com.fiap.foodlink_api.domain.entity.User;
import com.fiap.foodlink_api.interfaces.controller.dto.AddressResponse;
import com.fiap.foodlink_api.interfaces.controller.dto.UserResponse;

public final class UserControllerMapper {

	private UserControllerMapper() {
	}

	public static UserResponse toResponse(User user, Address address) {
		return new UserResponse(
				user.getId(),
				user.getName(),
				user.getEmail(),
				user.getLogin(),
				user.getUserTypeId(),
				toAddressResponse(address),
				user.getLastUpdatedAt()
		);
	}

	private static AddressResponse toAddressResponse(Address address) {
		return new AddressResponse(
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
