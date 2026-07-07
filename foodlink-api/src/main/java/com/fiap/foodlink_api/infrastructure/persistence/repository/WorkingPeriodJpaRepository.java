package com.fiap.foodlink_api.infrastructure.persistence.repository;

import com.fiap.foodlink_api.infrastructure.persistence.entity.WorkingPeriodJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface WorkingPeriodJpaRepository extends JpaRepository<WorkingPeriodJpaEntity, UUID> {

    @Query("SELECT wp FROM WorkingPeriodJpaEntity wp WHERE wp.restaurantId = :restaurantId")
    List<WorkingPeriodJpaEntity> findByRestaurantId(UUID restaurantId);

    boolean existsByRestaurantId(UUID restaurantId);

    @Modifying
    @Query("DELETE FROM WorkingPeriodJpaEntity wp WHERE wp.restaurantId = :restaurantId")
    void deleteAllByRestaurantId(UUID restaurantId);
}
