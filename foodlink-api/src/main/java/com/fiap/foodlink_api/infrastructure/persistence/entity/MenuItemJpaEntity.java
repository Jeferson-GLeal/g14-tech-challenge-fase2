package com.fiap.foodlink_api.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "itens_cardapio")
public class MenuItemJpaEntity {

	@Id
	@GeneratedValue
	private UUID id;

	@Column(name = "nome", nullable = false)
	private String name;

	@Column(name = "descricao", nullable = false, length = 400)
	private String description;

	@Column(name = "preco", nullable = false, precision = 10, scale = 2)
	private BigDecimal price;

	@Column(name = "restaurante_id", nullable = false)
	private UUID restaurantId;

	@Column(name = "caminho_foto", nullable = false)
	private String photoPath;

	@Column(name = "disponivel_apenas_no_restaurante", nullable = false)
	private boolean availableOnlyAtRestaurant;

	@Column(name = "data_ultima_alteracao", nullable = false)
	private OffsetDateTime lastUpdatedAt;

	protected MenuItemJpaEntity() {
	}

	public MenuItemJpaEntity(
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
		this.name = name;
		this.description = description;
		this.price = price;
		this.restaurantId = restaurantId;
		this.photoPath = photoPath;
		this.availableOnlyAtRestaurant = availableOnlyAtRestaurant;
		this.lastUpdatedAt = lastUpdatedAt;
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
}
