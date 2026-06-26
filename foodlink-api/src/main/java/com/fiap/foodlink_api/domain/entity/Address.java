package com.fiap.foodlink_api.domain.entity;

import com.fiap.foodlink_api.domain.exception.DomainException;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

public class Address {

	private static final int MAX_TEXT_LENGTH = 255;
	private static final int MAX_NUMBER_LENGTH = 50;
	private static final int MAX_UF_LENGTH = 10;
	private static final int MAX_ZIP_CODE_LENGTH = 20;

	private final UUID id;
	private String street;
	private String number;
	private String complement;
	private String district;
	private String city;
	private String state;
	private String zipCode;
	private OffsetDateTime lastUpdatedAt;

	public Address(
			UUID id,
			String street,
			String number,
			String complement,
			String district,
			String city,
			String state,
			String zipCode,
			OffsetDateTime lastUpdatedAt
	) {
		this.id = id;
		setStreet(street);
		setNumber(number);
		setComplement(complement);
		setDistrict(district);
		setCity(city);
		setState(state);
		setZipCode(zipCode);
		setLastUpdatedAt(lastUpdatedAt);
	}

	public static Address create(
			String street,
			String number,
			String complement,
			String district,
			String city,
			String state,
			String zipCode
	) {
		return new Address(null, street, number, complement, district, city, state, zipCode, OffsetDateTime.now());
	}

	public UUID getId() {
		return id;
	}

	public String getStreet() {
		return street;
	}

	public String getNumber() {
		return number;
	}

	public String getComplement() {
		return complement;
	}

	public String getDistrict() {
		return district;
	}

	public String getCity() {
		return city;
	}

	public String getState() {
		return state;
	}

	public String getZipCode() {
		return zipCode;
	}

	public OffsetDateTime getLastUpdatedAt() {
		return lastUpdatedAt;
	}

	public void update(
			String street,
			String number,
			String complement,
			String district,
			String city,
			String state,
			String zipCode
	) {
		setStreet(street);
		setNumber(number);
		setComplement(complement);
		setDistrict(district);
		setCity(city);
		setState(state);
		setZipCode(zipCode);
		touch();
	}

	private void setStreet(String street) {
		this.street = requireText(street, MAX_TEXT_LENGTH, "Logradouro do endereco e obrigatório.");
	}

	private void setNumber(String number) {
		this.number = requireText(number, MAX_NUMBER_LENGTH, "Numero do endereco e obrigatório.");
	}

	private void setComplement(String complement) {
		this.complement = normalizeOptionalText(complement, MAX_TEXT_LENGTH);
	}

	private void setDistrict(String district) {
		this.district = requireText(district, MAX_TEXT_LENGTH, "Bairro do endereco e obrigatório.");
	}

	private void setCity(String city) {
		this.city = requireText(city, MAX_TEXT_LENGTH, "Cidade do endereco e obrigatória.");
	}

	private void setState(String state) {
		this.state = requireText(state, MAX_UF_LENGTH, "UF do endereco e obrigatoria.").toUpperCase();
	}

	private void setZipCode(String zipCode) {
		this.zipCode = requireText(zipCode, MAX_ZIP_CODE_LENGTH, "CEP do endereco e obrigatório.");
	}

	private void setLastUpdatedAt(OffsetDateTime lastUpdatedAt) {
		this.lastUpdatedAt = Objects.requireNonNullElseGet(lastUpdatedAt, OffsetDateTime::now);
	}

	private void touch() {
		this.lastUpdatedAt = OffsetDateTime.now();
	}

	private String requireText(String value, int maxLength, String message) {
		String normalizedValue = Objects.requireNonNullElse(value, "").trim();

		if (normalizedValue.isBlank()) {
			throw new DomainException(message);
		}

		validateMaxLength(normalizedValue, maxLength);
		return normalizedValue;
	}

	private String normalizeOptionalText(String value, int maxLength) {
		String normalizedValue = Objects.requireNonNullElse(value, "").trim();

		if (normalizedValue.isBlank()) {
			return null;
		}

		validateMaxLength(normalizedValue, maxLength);
		return normalizedValue;
	}

	private void validateMaxLength(String value, int maxLength) {
		if (value.length() > maxLength) {
			throw new DomainException("Campo de endereco deve ter no máximo " + maxLength + " caracteres.");
		}
	}
}
