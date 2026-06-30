package com.fiap.foodlink_api.interfaces.controller.dto;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record RestaurantResponse (UUID id,
                                  String name,
                                  String cnpj,
                                  String type,
                                  UserResponse owner,
                                  OffsetDateTime lastUpdatedAt,
                                  List<WorkingPeriodResponse> period){
}
