package com.fiap.foodlink_api.interfaces.controller.dto;

import com.fiap.foodlink_api.domain.entity.UserTypeCode;

import java.util.UUID;

public record UserTypeResponse(UUID id, String name, UserTypeCode code) {
}
