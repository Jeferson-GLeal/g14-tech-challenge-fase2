package com.fiap.foodlink_api.interfaces.controller;

import com.fiap.foodlink_api.application.usecase.menuitem.CreateMenuItemUseCase;
import com.fiap.foodlink_api.application.usecase.menuitem.DeleteMenuItemUseCase;
import com.fiap.foodlink_api.application.usecase.menuitem.GetMenuItemByIdUseCase;
import com.fiap.foodlink_api.application.usecase.menuitem.ListMenuItemsByRestaurantUseCase;
import com.fiap.foodlink_api.application.usecase.menuitem.UpdateMenuItemUseCase;
import com.fiap.foodlink_api.domain.entity.MenuItem;
import com.fiap.foodlink_api.interfaces.controller.dto.MenuItemRequest;
import com.fiap.foodlink_api.interfaces.controller.dto.MenuItemResponse;
import com.fiap.foodlink_api.interfaces.controller.mapper.MenuItemControllerMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/menu-items")
@Tag(name = "Itens do cardapio", description = "Operacoes para gerenciamento de itens do cardapio")
public class MenuItemController {

	private final CreateMenuItemUseCase createMenuItemUseCase;
	private final GetMenuItemByIdUseCase getMenuItemByIdUseCase;
	private final ListMenuItemsByRestaurantUseCase listMenuItemsByRestaurantUseCase;
	private final UpdateMenuItemUseCase updateMenuItemUseCase;
	private final DeleteMenuItemUseCase deleteMenuItemUseCase;

	public MenuItemController(
			CreateMenuItemUseCase createMenuItemUseCase,
			GetMenuItemByIdUseCase getMenuItemByIdUseCase,
			ListMenuItemsByRestaurantUseCase listMenuItemsByRestaurantUseCase,
			UpdateMenuItemUseCase updateMenuItemUseCase,
			DeleteMenuItemUseCase deleteMenuItemUseCase
	) {
		this.createMenuItemUseCase = createMenuItemUseCase;
		this.getMenuItemByIdUseCase = getMenuItemByIdUseCase;
		this.listMenuItemsByRestaurantUseCase = listMenuItemsByRestaurantUseCase;
		this.updateMenuItemUseCase = updateMenuItemUseCase;
		this.deleteMenuItemUseCase = deleteMenuItemUseCase;
	}

	@PostMapping("/{restaurantId}")
	@Operation(summary = "Cria um item do cardapio para um restaurante")
	public ResponseEntity<MenuItemResponse> create(
			@PathVariable UUID restaurantId,
			@RequestBody MenuItemRequest request
	) {
		MenuItem menuItem = createMenuItemUseCase.execute(
				request.name(),
				request.description(),
				request.price(),
				restaurantId,
				request.photoPath(),
				request.availableOnlyAtRestaurant()
		);

		return ResponseEntity.status(HttpStatus.CREATED)
				.body(MenuItemControllerMapper.toResponse(menuItem));
	}

	@GetMapping("/{restaurantId}/{id}")
	@Operation(summary = "Busca um item do cardapio por restaurante e identificador")
	public ResponseEntity<MenuItemResponse> findById(@PathVariable UUID restaurantId, @PathVariable UUID id) {
		MenuItem menuItem = getMenuItemByIdUseCase.execute(restaurantId, id);
		return ResponseEntity.ok(MenuItemControllerMapper.toResponse(menuItem));
	}

	@GetMapping("/{restaurantId}")
	@Operation(summary = "Lista itens do cardapio por restaurante")
	public ResponseEntity<List<MenuItemResponse>> findByRestaurant(@PathVariable UUID restaurantId) {
		List<MenuItemResponse> menuItems = listMenuItemsByRestaurantUseCase.execute(restaurantId).stream()
				.map(MenuItemControllerMapper::toResponse)
				.toList();

		return ResponseEntity.ok(menuItems);
	}

	@PutMapping("/{restaurantId}/{id}")
	@Operation(summary = "Atualiza um item do cardapio")
	public ResponseEntity<MenuItemResponse> update(
			@PathVariable UUID restaurantId,
			@PathVariable UUID id,
			@RequestBody MenuItemRequest request
	) {
		MenuItem menuItem = updateMenuItemUseCase.execute(
				restaurantId,
				id,
				request.name(),
				request.description(),
				request.price(),
				request.photoPath(),
				request.availableOnlyAtRestaurant()
		);

		return ResponseEntity.ok(MenuItemControllerMapper.toResponse(menuItem));
	}

	@DeleteMapping("/{restaurantId}/{id}")
	@Operation(summary = "Remove um item do cardapio")
	public ResponseEntity<Void> delete(@PathVariable UUID restaurantId, @PathVariable UUID id) {
		deleteMenuItemUseCase.execute(restaurantId, id);
		return ResponseEntity.noContent().build();
	}
}
