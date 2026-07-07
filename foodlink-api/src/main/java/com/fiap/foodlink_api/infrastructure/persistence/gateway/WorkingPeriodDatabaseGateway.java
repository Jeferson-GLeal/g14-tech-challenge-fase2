package com.fiap.foodlink_api.infrastructure.persistence.gateway;

import com.fiap.foodlink_api.domain.entity.WorkingPeriod;
import com.fiap.foodlink_api.domain.gateway.WorkingPeriodGateway;
import com.fiap.foodlink_api.infrastructure.persistence.entity.WorkingPeriodJpaEntity;
import com.fiap.foodlink_api.infrastructure.persistence.mapper.WorkingPeriodPersistenceMapper;
import com.fiap.foodlink_api.infrastructure.persistence.repository.WorkingPeriodJpaRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class WorkingPeriodDatabaseGateway implements WorkingPeriodGateway {

    private final WorkingPeriodJpaRepository workingPeriodJpaRepository;

    public WorkingPeriodDatabaseGateway( WorkingPeriodJpaRepository workingPeriodJpaRepository) {
        this.workingPeriodJpaRepository = workingPeriodJpaRepository;
    }

    @Override
    public boolean existsByRestaurantId(UUID restaurantId) {
        return workingPeriodJpaRepository.existsByRestaurantId(restaurantId);
    }

    @Override
    public List<WorkingPeriod> findWorkingPeriodByRestaurantId(UUID restaurantId) {
        return workingPeriodJpaRepository.findByRestaurantId(restaurantId)
                .stream()
                .map(WorkingPeriodPersistenceMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public WorkingPeriod save(WorkingPeriodJpaEntity workingPeriod) {
        return WorkingPeriodPersistenceMapper.toDomain(workingPeriodJpaRepository.save(workingPeriod));
    }

    @Override
    public List<WorkingPeriod> findByRestaurantId(UUID id) {
        return workingPeriodJpaRepository.findByRestaurantId(id)
                .stream()
                .map(WorkingPeriodPersistenceMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteAll(UUID id) {
        workingPeriodJpaRepository.deleteAllByRestaurantId(id);
    }
}
