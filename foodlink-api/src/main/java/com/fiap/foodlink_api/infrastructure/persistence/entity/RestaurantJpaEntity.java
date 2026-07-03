package com.fiap.foodlink_api.infrastructure.persistence.entity;

import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "restaurantes")
public class RestaurantJpaEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "nome", nullable = false)
    private String name;

    @Column(name = "cnpj", nullable = false, unique = true)
    private String cnpj;

    @Column(name = "tipo_cozinha", nullable = false)
    private String type;

    @Column(name = "dono_restaurante_id", nullable = false)
    private UUID owner;

    @Column(name = "endereco_id", nullable = false)
    private UUID addressId;

    @Column(name = "data_ultima_alteracao", nullable = false)
    private OffsetDateTime lastUpdatedAt;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurante_id", referencedColumnName = "id", insertable = false, updatable = false)
    private List<WorkingPeriodJpaEntity> workingPeriods;

    protected RestaurantJpaEntity() {
    }

    public RestaurantJpaEntity(UUID id, String name, String cnpj, String type, UUID owner, UUID addressId, OffsetDateTime lastUpdatedAt) {
        this.id = id;
        this.name = name;
        this.cnpj = cnpj;
        this.type = type;
        this.owner = owner;
        this.addressId = addressId;
        this.lastUpdatedAt = lastUpdatedAt;
    }

    public RestaurantJpaEntity(String name, String cnpj, String type, UUID owner, UUID addressId, OffsetDateTime lastUpdatedAt) {
        this.name = name;
        this.cnpj = cnpj;
        this.type = type;
        this.owner = owner;
        this.addressId = addressId;
        this.lastUpdatedAt = lastUpdatedAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public UUID getOwner() {
        return owner;
    }

    public void setOwner(UUID ownerId) {
        this.owner = ownerId;
    }

    public UUID getAddressId() {
        return addressId;
    }

    public void setAddressId(UUID addressId) {
        this.addressId = addressId;
    }

    public OffsetDateTime getLastUpdatedAt() {
        return lastUpdatedAt;
    }

    public void setLastUpdatedAt(OffsetDateTime lastUpdatedAt) {
        this.lastUpdatedAt = lastUpdatedAt;
    }

    public List<WorkingPeriodJpaEntity> getWorkingPeriods() {
        return workingPeriods;
    }
}
