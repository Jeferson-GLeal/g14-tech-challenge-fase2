package com.fiap.foodlink_api.domain.gateway;

import com.fiap.foodlink_api.domain.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserGateway {

	User save(User user);

	Optional<User> findById(UUID id);

	Optional<User> findByEmail(String email);

	Optional<User> findByLogin(String login);

	List<User> findAll();

	boolean existsById(UUID id);

	boolean existsByEmail(String email);

	boolean existsByLogin(String login);

	boolean existsByUserTypeId(UUID userTypeId);

	void deleteById(UUID id);
}
