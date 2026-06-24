package com.fiap.foodlink_api.interfaces.controller.dto;

import java.time.Instant;

public record ErrorResponse(String message, int status, Instant timestamp) {
}
