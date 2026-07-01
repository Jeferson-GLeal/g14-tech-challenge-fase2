package com.fiap.foodlink_api.infrastructure.persistence.gateway;

import com.fiap.foodlink_api.domain.entity.MenuItem;
import com.fiap.foodlink_api.domain.gateway.MenuItemGateway;
import com.fiap.foodlink_api.infrastructure.persistence.mapper.MenuItemPersistenceMapper;
import com.fiap.foodlink_api.infrastructure.persistence.repository.MenuItemJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class MenuItemDatabaseGateway implements MenuItemGateway {

	private final MenuItemJpaRepository menuItemJpaRepository;

	public MenuItemDatabaseGateway(MenuItemJpaRepository menuItemJpaRepository) {
		this.menuItemJpaRepository = menuItemJpaRepository;
	}

	@Override
	public MenuItem save(MenuItem menuItem) {
		return MenuItemPersistenceMapper.toDomain(
				menuItemJpaRepository.save(MenuItemPersistenceMapper.toEntity(menuItem))
		);
	}

	@Override
	public Optional<MenuItem> findById(UUID id) {
		return menuItemJpaRepository.findById(id)
				.map(MenuItemPersistenceMapper::toDomain);
	}

	@Override
	public List<MenuItem> findAll() {
		return menuItemJpaRepository.findAll().stream()
				.map(MenuItemPersistenceMapper::toDomain)
				.toList();
	}

	@Override
	public List<MenuItem> findByRestaurantId(UUID restaurantId) {
		return menuItemJpaRepository.findByRestaurantId(restaurantId).stream()
				.map(MenuItemPersistenceMapper::toDomain)
				.toList();
	}

	@Override
	public boolean existsById(UUID id) {
		return menuItemJpaRepository.existsById(id);
	}

	@Override
	public void deleteById(UUID id) {
		menuItemJpaRepository.deleteById(id);
	}
}
