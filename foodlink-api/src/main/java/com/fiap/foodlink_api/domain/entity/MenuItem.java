package com.fiap.foodlink_api.domain.entity;

import com.fiap.foodlink_api.domain.exception.DomainException;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

public class MenuItem {

	private static final int MAX_TEXT_LENGTH = 255;
	private static final int MAX_DESCRIPTION_LENGTH = 400;

	private final UUID id;
	private String name;
	private String description;
	private BigDecimal price;
	private UUID restaurantId;
	private String photoPath;
	private boolean availableOnlyAtRestaurant;
	private OffsetDateTime lastUpdatedAt;

	public MenuItem(
			UUID id,
			String name,
			String description,
			BigDecimal price,
			UUID restaurantId,
			String photoPath,
			boolean availableOnlyAtRestaurant,
			OffsetDateTime lastUpdatedAt
	) {
		this.id = id;
		setName(name);
		setDescription(description);
		setPrice(price);
		setRestaurantId(restaurantId);
		setPhotoPath(photoPath);
		setAvailableOnlyAtRestaurant(availableOnlyAtRestaurant);
		setLastUpdatedAt(lastUpdatedAt);
	}

	public static MenuItem create(
			String name,
			String description,
			BigDecimal price,
			UUID restaurantId,
			String photoPath,
			boolean availableOnlyAtRestaurant
	) {
		return new MenuItem(
				null,
				name,
				description,
				price,
				restaurantId,
				photoPath,
				availableOnlyAtRestaurant,
				OffsetDateTime.now()
		);
	}

	public UUID getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public UUID getRestaurantId() {
		return restaurantId;
	}

	public String getPhotoPath() {
		return photoPath;
	}

	public boolean isAvailableOnlyAtRestaurant() {
		return availableOnlyAtRestaurant;
	}

	public OffsetDateTime getLastUpdatedAt() {
		return lastUpdatedAt;
	}

	public void update(
			String name,
			String description,
			BigDecimal price,
			UUID restaurantId,
			String photoPath,
			boolean availableOnlyAtRestaurant
	) {
		setName(name);
		setDescription(description);
		setPrice(price);
		setRestaurantId(restaurantId);
		setPhotoPath(photoPath);
		setAvailableOnlyAtRestaurant(availableOnlyAtRestaurant);
		touch();
	}

	private void setName(String name) {
		this.name = requireText(name, MAX_TEXT_LENGTH, "Nome do item do cardapio e obrigatorio.");
	}

	private void setDescription(String description) {
		this.description = requireText(
				description,
				MAX_DESCRIPTION_LENGTH,
				"Descricao do item do cardapio e obrigatoria."
		);
	}

	private void setPrice(BigDecimal price) {
		BigDecimal normalizedPrice = Objects.requireNonNull(price, "Preco do item do cardapio e obrigatorio.");

		if (normalizedPrice.compareTo(BigDecimal.ZERO) <= 0) {
			throw new DomainException("Preco do item do cardapio deve ser maior que zero.");
		}

		this.price = normalizedPrice;
	}

	private void setRestaurantId(UUID restaurantId) {
		this.restaurantId = Objects.requireNonNull(restaurantId, "Restaurante do item do cardapio e obrigatorio.");
	}

	private void setPhotoPath(String photoPath) {
		this.photoPath = requireText(photoPath, MAX_TEXT_LENGTH, "Caminho da foto do item do cardapio e obrigatorio.");
	}

	private void setAvailableOnlyAtRestaurant(boolean availableOnlyAtRestaurant) {
		this.availableOnlyAtRestaurant = availableOnlyAtRestaurant;
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

		if (normalizedValue.length() > maxLength) {
			throw new DomainException("Campo de item do cardapio deve ter no maximo " + maxLength + " caracteres.");
		}

		return normalizedValue;
	}
}
