package com.fiap.foodlink_api.domain.entity;

import com.fiap.foodlink_api.domain.exception.DomainException;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

public class Restaurant {

    private static final int MAX_TEXT_LENGTH = 255;
    private static final int MAX_CNPJ_LENGTH = 18;

    private final UUID id;
    private String name;
    private String cnpj;
    private String type;
    private UUID ownerId;
    private UUID addressId;
    private OffsetDateTime lastUpdatedAt;

    public Restaurant(
            UUID id,
            String name,
            String cnpj,
            String type,
            UUID ownerId,
            UUID addressId,
            OffsetDateTime lastUpdatedAt
    ) {
        this.id = id;
        setName(name);
        setCnpj(cnpj);
        setType(type);
        setOwnerId(ownerId);
        setAddressId(addressId);
        setLastUpdatedAt(lastUpdatedAt);
    }

    public static Restaurant create(String name, String cnpj, String type, UUID ownerId, UUID addressId) {
        return new Restaurant(null, name, cnpj, type, ownerId, addressId, OffsetDateTime.now());
    }

    public void update(String name, String cnpj, String type, UUID ownerId) {
        setName(name);
        setCnpj(cnpj);
        setType(type);
        setOwnerId(ownerId);
        touch();
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCnpj() {
        return cnpj;
    }

    public String getType() {
        return type;
    }

    public OffsetDateTime getLastUpdatedAt() {
        return lastUpdatedAt;
    }

    public UUID getOwnerId() {
        return ownerId;
    }

    public UUID getAddressId() {
        return addressId;
    }

    private void setName(String name) {
        this.name = requireText(name, MAX_TEXT_LENGTH, "Nome do restaurante e obrigatorio.");
    }

    private void setCnpj(String cnpj) {
        this.cnpj = requireText(cnpj, MAX_CNPJ_LENGTH, "CNPJ do restaurante e obrigatorio.");
    }

    private void setType(String type) {
        this.type = requireText(type, MAX_TEXT_LENGTH, "Tipo de cozinha do restaurante e obrigatorio.");
    }

    private void setOwnerId(UUID ownerId) {
        this.ownerId = Objects.requireNonNull(ownerId, "Dono do restaurante e obrigatorio.");
    }

    private void setAddressId(UUID addressId) {
        this.addressId = Objects.requireNonNull(addressId, "Endereco do restaurante e obrigatorio.");
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
            throw new DomainException("Campo do restaurante deve ter no maximo " + maxLength + " caracteres.");
        }

        return normalizedValue;
    }
}
