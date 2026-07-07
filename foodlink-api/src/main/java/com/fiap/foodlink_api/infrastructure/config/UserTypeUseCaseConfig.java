package com.fiap.foodlink_api.infrastructure.config;

import com.fiap.foodlink_api.application.usecase.usertype.CreateUserTypeUseCase;
import com.fiap.foodlink_api.application.usecase.usertype.DeleteUserTypeUseCase;
import com.fiap.foodlink_api.application.usecase.usertype.GetUserTypeByIdUseCase;
import com.fiap.foodlink_api.application.usecase.usertype.ListUserTypesUseCase;
import com.fiap.foodlink_api.application.usecase.usertype.UpdateUserTypeUseCase;
import com.fiap.foodlink_api.domain.gateway.UserGateway;
import com.fiap.foodlink_api.domain.gateway.UserTypeGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserTypeUseCaseConfig {

	@Bean
	public CreateUserTypeUseCase createUserTypeUseCase(UserTypeGateway userTypeGateway) {
		return new CreateUserTypeUseCase(userTypeGateway);
	}

	@Bean
	public GetUserTypeByIdUseCase getUserTypeByIdUseCase(UserTypeGateway userTypeGateway) {
		return new GetUserTypeByIdUseCase(userTypeGateway);
	}

	@Bean
	public ListUserTypesUseCase listUserTypesUseCase(UserTypeGateway userTypeGateway) {
		return new ListUserTypesUseCase(userTypeGateway);
	}

	@Bean
	public UpdateUserTypeUseCase updateUserTypeUseCase(UserTypeGateway userTypeGateway, UserGateway userGateway) {
		return new UpdateUserTypeUseCase(userTypeGateway, userGateway);
	}

	@Bean
	public DeleteUserTypeUseCase deleteUserTypeUseCase(UserTypeGateway userTypeGateway, UserGateway userGateway) {
		return new DeleteUserTypeUseCase(userTypeGateway, userGateway);
	}
}
