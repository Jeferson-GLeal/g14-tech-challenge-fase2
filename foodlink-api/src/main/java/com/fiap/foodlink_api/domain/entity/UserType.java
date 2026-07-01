package com.fiap.foodlink_api.domain.entity;

import com.fiap.foodlink_api.domain.exception.DomainException;

import java.util.Objects;
import java.util.UUID;

public class UserType {

	private static final int MAX_NAME_LENGTH = 255;

	private final UUID id;
	private String name;

	public UserType(UUID id, String name) {
		this.id = id;
		setName(name);
	}

	public static UserType create(String name) {
		return new UserType(null, name);
	}

	public UUID getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void rename(String name) {
		setName(name);
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

	private String normalizeName(String name) {
		return Objects.requireNonNullElse(name, "").trim();
	}

	public void isDono(){
		if( !this.name.equals("Dono") ) {
			throw new DomainException("Usuario nao e dono do restaurante.");
		}
	}
}
