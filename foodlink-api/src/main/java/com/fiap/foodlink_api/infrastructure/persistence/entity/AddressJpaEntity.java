package com.fiap.foodlink_api.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "enderecos")
public class AddressJpaEntity {

	@Id
	@GeneratedValue
	private UUID id;

	@Column(name = "logradouro", nullable = false)
	private String street;

	@Column(name = "numero", nullable = false, length = 50)
	private String number;

	@Column(name = "complemento")
	private String complement;

	@Column(name = "bairro", nullable = false)
	private String district;

	@Column(name = "cidade", nullable = false)
	private String city;

	@Column(name = "uf", nullable = false, length = 10)
	private String state;

	@Column(name = "cep", nullable = false, length = 20)
	private String zipCode;

	@Column(name = "data_ultima_alteracao", nullable = false)
	private OffsetDateTime lastUpdatedAt;

	protected AddressJpaEntity() {
	}

	public AddressJpaEntity(
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
		this.street = street;
		this.number = number;
		this.complement = complement;
		this.district = district;
		this.city = city;
		this.state = state;
		this.zipCode = zipCode;
		this.lastUpdatedAt = lastUpdatedAt;
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
}
