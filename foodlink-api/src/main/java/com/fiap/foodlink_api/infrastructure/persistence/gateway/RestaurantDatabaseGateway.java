package com.fiap.foodlink_api.infrastructure.persistence.gateway;

import com.fiap.foodlink_api.domain.entity.Restaurant;
import com.fiap.foodlink_api.domain.entity.User;
import com.fiap.foodlink_api.domain.exception.RestaurantAlreadyExistsException;
import com.fiap.foodlink_api.domain.exception.RestaurantNotFoundException;
import com.fiap.foodlink_api.domain.gateway.RestaurantGateway;
import com.fiap.foodlink_api.infrastructure.persistence.entity.RestaurantJpaEntity;
import com.fiap.foodlink_api.infrastructure.persistence.mapper.RestaurantPersistenceMapper;
import com.fiap.foodlink_api.infrastructure.persistence.repository.RestaurantJpaRepository;
import com.fiap.foodlink_api.interfaces.controller.dto.RestaurantRequest;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class RestaurantDatabaseGateway implements RestaurantGateway {

    private final RestaurantJpaRepository restaurantJpaRepository;

    public RestaurantDatabaseGateway(RestaurantJpaRepository restaurantJpaRepository) {
        this.restaurantJpaRepository = restaurantJpaRepository;
    }

    @Override
    public List<Restaurant> findAll() {
        return restaurantJpaRepository.findAll().stream()
                .map(RestaurantPersistenceMapper::toDomain)
                .toList();
    }


    @Override
    public Restaurant create(RestaurantRequest restaurantRequest) {
        boolean existsRestaurant = restaurantJpaRepository.existsByCnpj(restaurantRequest.cnpj());
        if (existsRestaurant) {
            throw new RestaurantAlreadyExistsException("cnpj", restaurantRequest.cnpj());
        }
        RestaurantJpaEntity savedRestaurant =
                restaurantJpaRepository.save(RestaurantPersistenceMapper.toEntity(restaurantRequest));
        return RestaurantPersistenceMapper.toDomain(savedRestaurant);
    }

    @Override
    public Restaurant findById(UUID id) {
        Optional<RestaurantJpaEntity> restaurantJpaEntity = restaurantJpaRepository.findById(id);
        if(restaurantJpaEntity.isEmpty()){
            throw new RestaurantNotFoundException(id);
        }
        return RestaurantPersistenceMapper.toDomain(restaurantJpaEntity.get());
    }

    @Override
    public boolean existsById(UUID id) {
        return restaurantJpaRepository.existsById(id);
    }

    @Override
    public void deleteById(UUID id) {
        restaurantJpaRepository.deleteById(id);
    }

    @Override
    public Restaurant update(RestaurantRequest request, Restaurant restaurant, User user) {
        RestaurantJpaEntity savedRestaurant = restaurantJpaRepository.findById(restaurant.getId()).get();
        savedRestaurant.setCnpj(request.cnpj());
        savedRestaurant.setName(request.name());
        savedRestaurant.setType(request.type());
        savedRestaurant.setOwner(user.getId());
        savedRestaurant.setLastUpdatedAt(OffsetDateTime.now());

        return RestaurantPersistenceMapper.toDomain(savedRestaurant);
    }
}
