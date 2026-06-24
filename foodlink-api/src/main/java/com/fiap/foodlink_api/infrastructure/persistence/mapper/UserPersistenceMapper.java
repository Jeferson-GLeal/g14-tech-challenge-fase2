package com.fiap.foodlink_api.infrastructure.persistence.mapper;

import com.fiap.foodlink_api.domain.entity.User;
import com.fiap.foodlink_api.infrastructure.persistence.entity.UserJpaEntity;

public class UserPersistenceMapper {

	private UserPersistenceMapper() {
	}

	public static User toDomain(UserJpaEntity entity) {
		return new User(
				entity.getId(),
				entity.getName(),
				entity.getEmail(),
				entity.getLogin(),
				entity.getPassword(),
				entity.getUserTypeId(),
				entity.getAddressId(),
				entity.getLastUpdatedAt()
		);
	}

	public static UserJpaEntity toEntity(User user) {
		return new UserJpaEntity(
				user.getId(),
				user.getName(),
				user.getEmail(),
				user.getLogin(),
				user.getPassword(),
				user.getUserTypeId(),
				user.getAddressId(),
				user.getLastUpdatedAt()
		);
	}
}
