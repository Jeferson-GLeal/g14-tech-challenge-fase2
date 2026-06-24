package com.fiap.foodlink_api.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "usuarios")
public class UserJpaEntity {

	@Id
	@GeneratedValue
	private UUID id;

	@Column(name = "nome", nullable = false)
	private String name;

	@Column(name = "email", nullable = false, unique = true)
	private String email;

	@Column(name = "login", nullable = false, unique = true)
	private String login;

	@Column(name = "senha", nullable = false)
	private String password;

	@Column(name = "tipo_usuario_id", nullable = false)
	private UUID userTypeId;

	@Column(name = "endereco_id", nullable = false)
	private UUID addressId;

	@Column(name = "data_ultima_alteracao", nullable = false)
	private OffsetDateTime lastUpdatedAt;

	protected UserJpaEntity() {
	}

	public UserJpaEntity(
			UUID id,
			String name,
			String email,
			String login,
			String password,
			UUID userTypeId,
			UUID addressId,
			OffsetDateTime lastUpdatedAt
	) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.login = login;
		this.password = password;
		this.userTypeId = userTypeId;
		this.addressId = addressId;
		this.lastUpdatedAt = lastUpdatedAt;
	}

	public UUID getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public String getLogin() {
		return login;
	}

	public String getPassword() {
		return password;
	}

	public UUID getUserTypeId() {
		return userTypeId;
	}

	public UUID getAddressId() {
		return addressId;
	}

	public OffsetDateTime getLastUpdatedAt() {
		return lastUpdatedAt;
	}
}
