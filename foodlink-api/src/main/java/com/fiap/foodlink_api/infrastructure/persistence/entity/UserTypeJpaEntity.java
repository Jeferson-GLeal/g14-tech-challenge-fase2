package com.fiap.foodlink_api.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

	protected UserTypeJpaEntity() {
	}

	public UserTypeJpaEntity(UUID id, String name) {
		this.id = id;
		this.name = name;
	}

	public UUID getId() {
		return id;
	}

	public String getName() {
		return name;
	}
}
