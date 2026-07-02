package com.fiap.foodlink_api.interfaces.controller.dto;

import com.fiap.foodlink_api.domain.entity.UserTypeCode;

public record UserTypeRequest(String name, UserTypeCode code) {
}
