package com.fiap.foodlink_api.infrastructure.persistence.entity;

import com.fiap.foodlink_api.domain.entity.DaysOfWeekEnum;
import jakarta.persistence.*;

import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "horarios_funcionamento_restaurante")
public class WorkingPeriodJpaEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "restaurante_id", nullable = false)
    private UUID restaurantId;

    @Enumerated(EnumType.STRING)
    @Column(name = "dias_semana", nullable = false)
    private DaysOfWeekEnum day;

    @Column(name = "hora_abertura", nullable = false)
    private LocalTime openTime;

    @Column(name = "hora_encerramento", nullable = false)
    private LocalTime closeTime;

    protected WorkingPeriodJpaEntity() {
    }

    public WorkingPeriodJpaEntity(UUID id, UUID restaurantId, DaysOfWeekEnum day, LocalTime openTime, LocalTime closeTime) {
        this.id = id;
        this.restaurantId = restaurantId;
        this.day = day;
        this.openTime = openTime;
        this.closeTime = closeTime;
    }

    public WorkingPeriodJpaEntity(UUID restaurantId, DaysOfWeekEnum day, LocalTime openTime, LocalTime closeTime) {
        this.restaurantId = restaurantId;
        this.day = day;
        this.openTime = openTime;
        this.closeTime = closeTime;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(UUID restaurantId) {
        this.restaurantId = restaurantId;
    }

    public DaysOfWeekEnum getDay() {
        return day;
    }

    public void setDay(DaysOfWeekEnum day) {
        this.day = day;
    }

    public LocalTime getOpenTime() {
        return openTime;
    }

    public void setOpenTime(LocalTime openTime) {
        this.openTime = openTime;
    }

    public LocalTime getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(LocalTime closeTime) {
        this.closeTime = closeTime;
    }
}
