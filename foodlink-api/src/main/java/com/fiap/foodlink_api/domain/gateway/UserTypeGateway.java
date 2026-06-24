package com.fiap.foodlink_api.domain.gateway;

import com.fiap.foodlink_api.domain.entity.UserType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserTypeGateway {

	UserType save(UserType userType);

	Optional<UserType> findById(UUID id);

	Optional<UserType> findByName(String name);

	List<UserType> findAll();

	boolean existsById(UUID id);

	boolean existsByName(String name);

	void deleteById(UUID id);
}
