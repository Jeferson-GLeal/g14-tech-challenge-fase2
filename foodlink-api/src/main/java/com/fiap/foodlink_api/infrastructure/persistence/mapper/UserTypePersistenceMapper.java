package com.fiap.foodlink_api.infrastructure.persistence.mapper;

import com.fiap.foodlink_api.domain.entity.UserType;
import com.fiap.foodlink_api.infrastructure.persistence.entity.UserTypeJpaEntity;

public class UserTypePersistenceMapper {

	private UserTypePersistenceMapper() {}

	public static UserType toDomain(UserTypeJpaEntity entity) {
		return new UserType(entity.getId(), entity.getName(), entity.getCode());
	}

	public static UserTypeJpaEntity toEntity(UserType userType) {
		return new UserTypeJpaEntity(userType.getId(), userType.getName(), userType.getCode());
	}
}
