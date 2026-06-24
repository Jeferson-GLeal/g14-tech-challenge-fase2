package com.fiap.foodlink_api.application.usecase.usertype;

import com.fiap.foodlink_api.domain.entity.UserType;
import com.fiap.foodlink_api.domain.gateway.UserTypeGateway;

import java.util.List;

public class ListUserTypesUseCase {

	private final UserTypeGateway userTypeGateway;

	public ListUserTypesUseCase(UserTypeGateway userTypeGateway) {
		this.userTypeGateway = userTypeGateway;
	}

	public List<UserType> execute() {
		return userTypeGateway.findAll();
	}
}
