package com.fiap.foodlink_api.infrastructure.persistence.repository;

import com.fiap.foodlink_api.infrastructure.persistence.entity.UserJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserJpaRepository extends JpaRepository<UserJpaEntity, UUID> {

	Optional<UserJpaEntity> findByEmail(String email);

	Optional<UserJpaEntity> findByLogin(String login);

	boolean existsByEmail(String email);

	boolean existsByLogin(String login);

	boolean existsByUserTypeId(UUID userTypeId);
}
