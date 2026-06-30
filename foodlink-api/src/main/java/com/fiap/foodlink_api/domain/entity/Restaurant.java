package com.fiap.foodlink_api.domain.entity;

import java.time.OffsetDateTime;
import java.util.UUID;

public class Restaurant {

    private final UUID id;
    private String name;
    private String cnpj;
    private String type;
    private OffsetDateTime lastUpdatedAt;
    private UUID ownerId;

    public Restaurant(UUID id, String name, String cnpj, String type, OffsetDateTime lastUpdatedAt, UUID ownerId) {
        this.id = id;
        this.name = name;
        this.cnpj = cnpj;
        this.type = type;
        this.lastUpdatedAt = lastUpdatedAt;
        this.ownerId = ownerId;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public OffsetDateTime getLastUpdatedAt() {
        return lastUpdatedAt;
    }

    public void setLastUpdatedAt(OffsetDateTime lastUpdatedAt) {
        this.lastUpdatedAt = lastUpdatedAt;
    }

    public UUID getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(UUID ownerId) {
        this.ownerId = ownerId;
    }
}
