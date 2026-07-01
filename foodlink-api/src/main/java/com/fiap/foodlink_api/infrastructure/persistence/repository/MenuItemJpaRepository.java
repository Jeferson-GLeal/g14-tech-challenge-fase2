package com.fiap.foodlink_api.infrastructure.persistence.repository;

import com.fiap.foodlink_api.infrastructure.persistence.entity.MenuItemJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MenuItemJpaRepository extends JpaRepository<MenuItemJpaEntity, UUID> {

	List<MenuItemJpaEntity> findByRestaurantId(UUID restaurantId);
}
