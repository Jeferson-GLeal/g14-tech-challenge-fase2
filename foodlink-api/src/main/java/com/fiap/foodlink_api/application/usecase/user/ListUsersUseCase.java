package com.fiap.foodlink_api.application.usecase.user;

import com.fiap.foodlink_api.domain.entity.User;
import com.fiap.foodlink_api.domain.gateway.UserGateway;

import java.util.List;

public class ListUsersUseCase {

	private final UserGateway userGateway;

	public ListUsersUseCase(UserGateway userGateway) {
		this.userGateway = userGateway;
	}

	public List<User> execute() {
		return userGateway.findAll();
	}
}
