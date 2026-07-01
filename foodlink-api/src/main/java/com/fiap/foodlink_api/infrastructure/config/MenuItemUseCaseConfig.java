package com.fiap.foodlink_api.infrastructure.config;

import com.fiap.foodlink_api.application.usecase.menuitem.CreateMenuItemUseCase;
import com.fiap.foodlink_api.application.usecase.menuitem.DeleteMenuItemUseCase;
import com.fiap.foodlink_api.application.usecase.menuitem.GetMenuItemByIdUseCase;
import com.fiap.foodlink_api.application.usecase.menuitem.ListMenuItemsByRestaurantUseCase;
import com.fiap.foodlink_api.application.usecase.menuitem.UpdateMenuItemUseCase;
import com.fiap.foodlink_api.domain.gateway.MenuItemGateway;
import com.fiap.foodlink_api.domain.gateway.RestaurantGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MenuItemUseCaseConfig {

	@Bean
	public CreateMenuItemUseCase createMenuItemUseCase(
			MenuItemGateway menuItemGateway,
			RestaurantGateway restaurantGateway
	) {
		return new CreateMenuItemUseCase(menuItemGateway, restaurantGateway);
	}

	@Bean
	public GetMenuItemByIdUseCase getMenuItemByIdUseCase(
			MenuItemGateway menuItemGateway,
			RestaurantGateway restaurantGateway
	) {
		return new GetMenuItemByIdUseCase(menuItemGateway, restaurantGateway);
	}

	@Bean
	public ListMenuItemsByRestaurantUseCase listMenuItemsByRestaurantUseCase(
			MenuItemGateway menuItemGateway,
			RestaurantGateway restaurantGateway
	) {
		return new ListMenuItemsByRestaurantUseCase(menuItemGateway, restaurantGateway);
	}

	@Bean
	public UpdateMenuItemUseCase updateMenuItemUseCase(
			MenuItemGateway menuItemGateway,
			RestaurantGateway restaurantGateway
	) {
		return new UpdateMenuItemUseCase(menuItemGateway, restaurantGateway);
	}

	@Bean
	public DeleteMenuItemUseCase deleteMenuItemUseCase(
			MenuItemGateway menuItemGateway,
			RestaurantGateway restaurantGateway
	) {
		return new DeleteMenuItemUseCase(menuItemGateway, restaurantGateway);
	}
}
