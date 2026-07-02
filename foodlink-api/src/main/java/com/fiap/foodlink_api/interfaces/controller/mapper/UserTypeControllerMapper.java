package com.fiap.foodlink_api.interfaces.controller.mapper;

import com.fiap.foodlink_api.domain.entity.UserType;
import com.fiap.foodlink_api.interfaces.controller.dto.UserTypeResponse;

public class UserTypeControllerMapper {

	private UserTypeControllerMapper() {
	}

	public static UserTypeResponse toResponse(UserType userType) {
		return new UserTypeResponse(userType.getId(), userType.getName(), userType.getCode());
	}
}
