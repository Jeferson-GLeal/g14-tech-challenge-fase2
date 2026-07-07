package com.fiap.foodlink_api.domain.entity;

import com.fiap.foodlink_api.domain.exception.DomainException;

import java.util.Objects;
import java.util.UUID;

public class UserType {

	private static final int MAX_NAME_LENGTH = 255;

	private final UUID id;
	private String name;
	private UserTypeCode code;

	public UserType(UUID id, String name, UserTypeCode code) {
		this.id = id;
		setName(name);
		setCode(code);
	}

	public static UserType create(String name, UserTypeCode code) {
		return new UserType(null, name, code);
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

	public void rename(String name) {
		setName(name);
	}

	public void update(String name, UserTypeCode code) {
		setName(name);
		setCode(code);
	}

	private void setName(String name) {
		String normalizedName = normalizeName(name);

		if (normalizedName.isBlank()) {
			throw new DomainException("Nome do tipo de usuario e obrigatorio.");
		}

		if (normalizedName.length() > MAX_NAME_LENGTH) {
			throw new DomainException("Nome do tipo de usuario deve ter no maximo 255 caracteres.");
		}

		this.name = normalizedName;
	}

	private void setCode(UserTypeCode code) {
		this.code = Objects.requireNonNull(code, "Codigo do tipo de usuario e obrigatorio.");
	}

	private String normalizeName(String name) {
		return Objects.requireNonNullElse(name, "").trim();
	}

	public boolean isDono() {
		return this.code == UserTypeCode.DONO_RESTAURANTE;
	}

	public void requireDono() {
		if (!isDono()) {
			throw new DomainException("Usuario nao e dono do restaurante.");
		}
	}
}
