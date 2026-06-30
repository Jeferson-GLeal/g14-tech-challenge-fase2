package com.fiap.foodlink_api.infrastructure.persistence.gateway;

import com.fiap.foodlink_api.domain.entity.WorkingPeriod;
import com.fiap.foodlink_api.domain.exception.WorkingPeriodNotFoundException;
import com.fiap.foodlink_api.domain.exception.WorkingPeriorAlreadyExistsException;
import com.fiap.foodlink_api.domain.gateway.WorkingPeriodGateway;
import com.fiap.foodlink_api.infrastructure.persistence.entity.WorkingPeriodJpaEntity;
import com.fiap.foodlink_api.infrastructure.persistence.mapper.WorkingPeriodPersistenceMapper;
import com.fiap.foodlink_api.infrastructure.persistence.repository.WorkingPeriodJpaRepository;
import com.fiap.foodlink_api.interfaces.controller.dto.WorkingPeriodRequest;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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
    public List<WorkingPeriod> findWorkingPeriodByRestaurantId(UUID restaurantId) {
        List<WorkingPeriodJpaEntity> workingPeriodJpaEntities =  workingPeriodJpaRepository.findByRestaurantId(restaurantId);
        if(workingPeriodJpaEntities.isEmpty()){
            throw new WorkingPeriodNotFoundException(restaurantId);
        }
        return workingPeriodJpaEntities.stream().map(WorkingPeriodPersistenceMapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<WorkingPeriod> createWorkingPeriod(WorkingPeriodRequest request, UUID restaurantId) {
        validateIfWorkingPeriodAlreadyExists(restaurantId);
        List<WorkingPeriod> workingPeriodList = new ArrayList<>();
        createOnDatabaseAndPopulateWorkingPeriodList(request, restaurantId, workingPeriodList);
        return workingPeriodList;
    }

    private void createOnDatabaseAndPopulateWorkingPeriodList(WorkingPeriodRequest request, UUID restaurantId, List<WorkingPeriod> workingPeriodList) {
        request.day().forEach(day -> {
            WorkingPeriodJpaEntity workingPeriodJpaEntity =
                    WorkingPeriodPersistenceMapper.toJpaEntity(restaurantId, day, request.openTime(), request.closeTime());
            workingPeriodList.add(WorkingPeriodPersistenceMapper.toDomain(
                    workingPeriodJpaRepository.save(workingPeriodJpaEntity)));
        });
    }

    private void validateIfWorkingPeriodAlreadyExists(UUID restaurantId) {
        boolean existedWorkingPeriod = workingPeriodJpaRepository.existsByRestaurantId(restaurantId);
        if (existedWorkingPeriod) {
            throw new WorkingPeriorAlreadyExistsException(restaurantId);
        }
    }


}
