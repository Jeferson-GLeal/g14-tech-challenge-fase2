package com.fiap.foodlink_api.infrastructure.persistence.repository;

import com.fiap.foodlink_api.infrastructure.persistence.entity.RestaurantJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RestaurantJpaRepository extends JpaRepository<RestaurantJpaEntity, UUID> {

    boolean existsByCnpj(String cnpj);
    boolean existsById(UUID id);
    boolean existsByOwner(UUID owner);

}
