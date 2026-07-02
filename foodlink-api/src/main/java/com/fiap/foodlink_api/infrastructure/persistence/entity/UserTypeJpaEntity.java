package com.fiap.foodlink_api.infrastructure.persistence.entity;

import com.fiap.foodlink_api.domain.entity.UserTypeCode;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "tipos_usuario")
public class UserTypeJpaEntity {

	@Id
	@GeneratedValue
	private UUID id;

	@Column(name = "nome", nullable = false, unique = true)
	private String name;

	@Enumerated(EnumType.STRING)
	@Column(name = "codigo", nullable = false, unique = true)
	private UserTypeCode code;

	protected UserTypeJpaEntity() {
	}

	public UserTypeJpaEntity(UUID id, String name, UserTypeCode code) {
		this.id = id;
		this.name = name;
		this.code = code;
	}

	public UUID getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public UserTypeCode getCode() {
		return code;
	}
}
