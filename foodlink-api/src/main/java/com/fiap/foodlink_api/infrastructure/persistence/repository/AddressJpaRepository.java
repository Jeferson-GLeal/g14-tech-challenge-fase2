package com.fiap.foodlink_api.infrastructure.persistence.repository;

import com.fiap.foodlink_api.infrastructure.persistence.entity.AddressJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AddressJpaRepository extends JpaRepository<AddressJpaEntity, UUID> {
}
