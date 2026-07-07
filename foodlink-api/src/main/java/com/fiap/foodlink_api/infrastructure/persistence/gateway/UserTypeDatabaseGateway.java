package com.fiap.foodlink_api.infrastructure.persistence.gateway;

import com.fiap.foodlink_api.domain.entity.UserType;
import com.fiap.foodlink_api.domain.gateway.UserTypeGateway;
import com.fiap.foodlink_api.infrastructure.persistence.mapper.UserTypePersistenceMapper;
import com.fiap.foodlink_api.infrastructure.persistence.repository.UserTypeJpaRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class UserTypeDatabaseGateway implements UserTypeGateway {

	private final UserTypeJpaRepository userTypeJpaRepository;

	public UserTypeDatabaseGateway(UserTypeJpaRepository userTypeJpaRepository) {
		this.userTypeJpaRepository = userTypeJpaRepository;
	}

	@Override
	@Transactional
	public UserType save(UserType userType) {
		return UserTypePersistenceMapper.toDomain(
				userTypeJpaRepository.save(UserTypePersistenceMapper.toEntity(userType))
		);
	}

	@Override
	public Optional<UserType> findById(UUID id) {
		return userTypeJpaRepository.findById(id)
				.map(UserTypePersistenceMapper::toDomain);
	}

	@Override
	public Optional<UserType> findByName(String name) {
		return userTypeJpaRepository.findByName(name)
				.map(UserTypePersistenceMapper::toDomain);
	}

	@Override
	public List<UserType> findAll() {
		return userTypeJpaRepository.findAll().stream()
				.map(UserTypePersistenceMapper::toDomain)
				.toList();
	}

	@Override
	public boolean existsById(UUID id) {
		return userTypeJpaRepository.existsById(id);
	}

	@Override
	public boolean existsByName(String name) {
		return userTypeJpaRepository.existsByName(name);
	}

	@Override
	@Transactional
	public void deleteById(UUID id) {
		userTypeJpaRepository.deleteById(id);
	}
}
