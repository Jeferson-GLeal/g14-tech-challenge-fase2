package com.fiap.foodlink_api.infrastructure.persistence.gateway;

import com.fiap.foodlink_api.domain.entity.Restaurant;
import com.fiap.foodlink_api.domain.gateway.RestaurantGateway;
import com.fiap.foodlink_api.infrastructure.persistence.mapper.RestaurantPersistenceMapper;
import com.fiap.foodlink_api.infrastructure.persistence.repository.RestaurantJpaRepository;
import org.springframework.stereotype.Component;

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
    public Restaurant save(Restaurant restaurant) {
        return RestaurantPersistenceMapper.toDomain(
                restaurantJpaRepository.save(RestaurantPersistenceMapper.toEntity(restaurant)));
    }

    @Override
    public boolean existsByCnpj(String cnpj){
        return restaurantJpaRepository.existsByCnpj(cnpj);
    }

    @Override
    public Optional<Restaurant> findById(UUID id) {
        return restaurantJpaRepository.findById(id).map(RestaurantPersistenceMapper::toDomain);
    }

    @Override
    public boolean existsById(UUID id) {
        return restaurantJpaRepository.existsById(id);
    }

    @Override
    public void deleteById(UUID id) {
        restaurantJpaRepository.deleteById(id);
    }
}
