package com.fiap.foodlink_api.infrastructure.persistence.repository;

import com.fiap.foodlink_api.infrastructure.persistence.entity.UserTypeJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserTypeJpaRepository extends JpaRepository<UserTypeJpaEntity, UUID> {

	Optional<UserTypeJpaEntity> findByName(String name);

	boolean existsByName(String name);
}
