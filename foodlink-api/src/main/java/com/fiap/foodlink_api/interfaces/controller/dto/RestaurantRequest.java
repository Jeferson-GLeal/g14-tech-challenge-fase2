package com.fiap.foodlink_api.interfaces.controller.dto;

import java.util.UUID;

public record RestaurantRequest(
        String cnpj,
        String name,
        String type,
        UUID ownerId,
        AddressRequest address,
        WorkingPeriodRequest period) {
}
