package com.fiap.foodlink_api.domain.entity;

import com.fiap.foodlink_api.domain.exception.DomainException;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

public class User {

	private static final int MAX_TEXT_LENGTH = 255;

	private final UUID id;
	private String name;
	private String email;
	private String login;
	private String password;
	private UUID userTypeId;
	private UUID addressId;
	private OffsetDateTime lastUpdatedAt;

	public User(
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
		setName(name);
		setEmail(email);
		setLogin(login);
		setPassword(password);
		setUserTypeId(userTypeId);
		setAddressId(addressId);
		setLastUpdatedAt(lastUpdatedAt);
	}

	public static User create(
			String name,
			String email,
			String login,
			String password,
			UUID userTypeId,
			UUID addressId
	) {
		return new User(null, name, email, login, password, userTypeId, addressId, OffsetDateTime.now());
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

	public void updateProfile(String name, String email, String login, UUID userTypeId, UUID addressId) {
		setName(name);
		setEmail(email);
		setLogin(login);
		setUserTypeId(userTypeId);
		setAddressId(addressId);
		touch();
	}

	public void changePassword(String password) {
		setPassword(password);
		touch();
	}

	private void setName(String name) {
		this.name = requireText(name, "Nome do usuario e obrigatorio.");
	}

	private void setEmail(String email) {
		String normalizedEmail = requireText(email, "Email do usuario e obrigatorio.").toLowerCase();

		if (!normalizedEmail.contains("@")) {
			throw new DomainException("Email do usuario deve ser valido.");
		}

		this.email = normalizedEmail;
	}

	private void setLogin(String login) {
		this.login = requireText(login, "Login do usuario e obrigatorio.");
	}

	private void setPassword(String password) {
		this.password = requireText(password, "Senha do usuario e obrigatoria.");
	}

	private void setUserTypeId(UUID userTypeId) {
		this.userTypeId = Objects.requireNonNull(userTypeId, "Tipo de usuario e obrigatorio.");
	}

	private void setAddressId(UUID addressId) {
		this.addressId = Objects.requireNonNull(addressId, "Endereco do usuario e obrigatorio.");
	}

	private void setLastUpdatedAt(OffsetDateTime lastUpdatedAt) {
		this.lastUpdatedAt = Objects.requireNonNullElseGet(lastUpdatedAt, OffsetDateTime::now);
	}

	private void touch() {
		this.lastUpdatedAt = OffsetDateTime.now();
	}

	private String requireText(String value, String message) {
		String normalizedValue = Objects.requireNonNullElse(value, "").trim();

		if (normalizedValue.isBlank()) {
			throw new DomainException(message);
		}

		if (normalizedValue.length() > MAX_TEXT_LENGTH) {
			throw new DomainException("Campo de usuario deve ter no maximo 255 caracteres.");
		}

		return normalizedValue;
	}
}
