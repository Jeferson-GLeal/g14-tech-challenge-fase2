package com.fiap.foodlink_api.infrastructure.persistence.gateway;

import com.fiap.foodlink_api.domain.entity.User;
import com.fiap.foodlink_api.domain.gateway.UserGateway;
import com.fiap.foodlink_api.infrastructure.persistence.mapper.UserPersistenceMapper;
import com.fiap.foodlink_api.infrastructure.persistence.repository.UserJpaRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class UserDatabaseGateway implements UserGateway {

	private final UserJpaRepository userJpaRepository;

	public UserDatabaseGateway(UserJpaRepository userJpaRepository) {
		this.userJpaRepository = userJpaRepository;
	}

	@Override
	@Transactional
	public User save(User user) {
		return UserPersistenceMapper.toDomain(
				userJpaRepository.save(UserPersistenceMapper.toEntity(user))
		);
	}

	@Override
	public Optional<User> findById(UUID id) {
		return userJpaRepository.findById(id)
				.map(UserPersistenceMapper::toDomain);
	}

	@Override
	public Optional<User> findByEmail(String email) {
		return userJpaRepository.findByEmail(email)
				.map(UserPersistenceMapper::toDomain);
	}

	@Override
	public Optional<User> findByLogin(String login) {
		return userJpaRepository.findByLogin(login)
				.map(UserPersistenceMapper::toDomain);
	}

	@Override
	public List<User> findAll() {
		return userJpaRepository.findAll().stream()
				.map(UserPersistenceMapper::toDomain)
				.toList();
	}

	@Override
	public boolean existsById(UUID id) {
		return userJpaRepository.existsById(id);
	}

	@Override
	public boolean existsByEmail(String email) {
		return userJpaRepository.existsByEmail(email);
	}

	@Override
	public boolean existsByLogin(String login) {
		return userJpaRepository.existsByLogin(login);
	}

	@Override
	public boolean existsByUserTypeId(UUID userTypeId) {
		return userJpaRepository.existsByUserTypeId(userTypeId);
	}

	@Override
	@Transactional
	public void deleteById(UUID id) {
		userJpaRepository.deleteById(id);
	}
}
