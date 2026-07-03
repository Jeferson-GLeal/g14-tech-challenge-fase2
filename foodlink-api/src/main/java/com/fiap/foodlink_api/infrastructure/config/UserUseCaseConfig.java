package com.fiap.foodlink_api.infrastructure.config;

import com.fiap.foodlink_api.application.usecase.user.ChangeUserPasswordUseCase;
import com.fiap.foodlink_api.application.usecase.user.CreateUserUseCase;
import com.fiap.foodlink_api.application.usecase.user.DeleteUserUseCase;
import com.fiap.foodlink_api.application.usecase.user.GetUserByIdUseCase;
import com.fiap.foodlink_api.application.usecase.user.ListUsersUseCase;
import com.fiap.foodlink_api.application.usecase.user.UpdateUserUseCase;
import com.fiap.foodlink_api.domain.gateway.AddressGateway;
import com.fiap.foodlink_api.domain.gateway.RestaurantGateway;
import com.fiap.foodlink_api.domain.gateway.UserGateway;
import com.fiap.foodlink_api.domain.gateway.UserTypeGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserUseCaseConfig {

	@Bean
	public CreateUserUseCase createUserUseCase(
			UserGateway userGateway,
			UserTypeGateway userTypeGateway,
			AddressGateway addressGateway
	) {
		return new CreateUserUseCase(userGateway, userTypeGateway, addressGateway);
	}

	@Bean
	public GetUserByIdUseCase getUserByIdUseCase(UserGateway userGateway) {
		return new GetUserByIdUseCase(userGateway);
	}

	@Bean
	public ListUsersUseCase listUsersUseCase(UserGateway userGateway) {
		return new ListUsersUseCase(userGateway);
	}

	@Bean
	public UpdateUserUseCase updateUserUseCase(
			UserGateway userGateway,
			UserTypeGateway userTypeGateway,
			AddressGateway addressGateway
	) {
		return new UpdateUserUseCase(userGateway, userTypeGateway, addressGateway);
	}

	@Bean
	public ChangeUserPasswordUseCase changeUserPasswordUseCase(UserGateway userGateway) {
		return new ChangeUserPasswordUseCase(userGateway);
	}

	@Bean
	public DeleteUserUseCase deleteUserUseCase(UserGateway userGateway, RestaurantGateway restaurantGateway) {
		return new DeleteUserUseCase(userGateway, restaurantGateway);
	}
}
