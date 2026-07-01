package com.fiap.foodlink_api.interfaces.controller;

import com.fiap.foodlink_api.application.usecase.restaurants.*;
import com.fiap.foodlink_api.application.usecase.user.GetUserByIdUseCase;
import com.fiap.foodlink_api.application.usecase.workingperiod.CreateWorkingPeriodUseCase;
import com.fiap.foodlink_api.application.usecase.workingperiod.GetWorkingPeriodByIdUseCase;
import com.fiap.foodlink_api.domain.entity.Restaurant;
import com.fiap.foodlink_api.domain.entity.User;
import com.fiap.foodlink_api.domain.entity.WorkingPeriod;
import com.fiap.foodlink_api.interfaces.controller.dto.RestaurantRequest;
import com.fiap.foodlink_api.interfaces.controller.dto.RestaurantResponse;
import com.fiap.foodlink_api.interfaces.controller.mapper.RestaurantControllerMapper;
import com.fiap.foodlink_api.interfaces.controller.mapper.WorkingPeriodControllerMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/restaurants")
@Tag(name = "Restaurantes", description = "Operacoes para gerenciamento de restaurantes")
public class RestaurantController {

    private final ListRestaurantsUseCase listRestaurantsUseCase;
    private final CreateRestaurantUseCase createRestaurantUseCase;
    private final GetRestaurantByIdUseCase getRestaurantByIdUseCase;
    private final DeleteRestauranteByIdUseCase deleteRestauranteByIdUseCase;
    private final UpdateRestaurantByIdUseCase updateRestaurantByIdUseCase;
    private final GetUserByIdUseCase getUserByIdUseCase;
    private final GetWorkingPeriodByIdUseCase getWorkingPeriodByIdUseCase;
    private final CreateWorkingPeriodUseCase createWorkingPeriodUseCase;

    public RestaurantController(
            ListRestaurantsUseCase listRestaurantsUseCase,
            GetUserByIdUseCase getUserByIdUseCase,
            GetWorkingPeriodByIdUseCase getWorkingPeriodByIdUseCase,
            CreateRestaurantUseCase createRestaurantUseCase,
            CreateWorkingPeriodUseCase createWorkingPeriodUseCase,
            GetRestaurantByIdUseCase getRestaurantByIdUseCase,
            DeleteRestauranteByIdUseCase deleteRestauranteByIdUseCase,
            UpdateRestaurantByIdUseCase updateRestaurantByIdUseCase
    ) {
        this.listRestaurantsUseCase = listRestaurantsUseCase;
        this.getUserByIdUseCase = getUserByIdUseCase;
        this.getWorkingPeriodByIdUseCase = getWorkingPeriodByIdUseCase;
        this.createRestaurantUseCase = createRestaurantUseCase;
        this.createWorkingPeriodUseCase = createWorkingPeriodUseCase;
        this.getRestaurantByIdUseCase = getRestaurantByIdUseCase;
        this.deleteRestauranteByIdUseCase = deleteRestauranteByIdUseCase;
        this.updateRestaurantByIdUseCase = updateRestaurantByIdUseCase;
    }

    @GetMapping
    @Operation(summary = "Lista restaurantes")
    public ResponseEntity<List<RestaurantResponse>> findAll() {
        List<RestaurantResponse> restaurants = listRestaurantsUseCase.execute().stream().map(
                restaurant -> RestaurantControllerMapper.toResponse(
                        restaurant,
                        getUserByIdUseCase.execute(restaurant.getOwnerId()),
                        getWorkingPeriodByIdUseCase.execute(restaurant.getId())
                )
        ).toList();
        return ResponseEntity.ok(restaurants);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca restaurante por ID")
    public ResponseEntity<RestaurantResponse> findById(@PathVariable UUID id) {
        Restaurant restaurant = getRestaurantByIdUseCase.execute(id);
        RestaurantResponse restaurantResponse = RestaurantControllerMapper.toResponse(
                restaurant,
                getUserByIdUseCase.execute(restaurant.getOwnerId()),
                getWorkingPeriodByIdUseCase.execute(restaurant.getId())
        );
        return ResponseEntity.ok(restaurantResponse);
    }

    @PostMapping
    @Operation(summary = "Cria um novo restaurante com horário de funcionamento e dono")
    public ResponseEntity<RestaurantResponse> create(@RequestBody RestaurantRequest request){
        Restaurant restaurantResponse = createRestaurantUseCase.execute(request);
        List<WorkingPeriod> workingPeriodList= createWorkingPeriodUseCase.execute(request.period(), restaurantResponse.getId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(RestaurantControllerMapper.toResponse(restaurantResponse, getUserByIdUseCase.execute(restaurantResponse.getOwnerId()), workingPeriodList));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deleta um restaurante por ID")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        deleteRestauranteByIdUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza os dados do restaurante por ID")
    public ResponseEntity<RestaurantResponse> update(@RequestBody RestaurantRequest request, @PathVariable UUID id) {
        User user = getUserByIdUseCase.execute(request.ownerId());
        List<WorkingPeriod> workingPeriodList = WorkingPeriodControllerMapper.toWorkingPeriod(request.period());
        Restaurant updatedRestaurant = updateRestaurantByIdUseCase
                .execute(RestaurantControllerMapper.fromDtoToDomain(request), user, workingPeriodList, id);
        return ResponseEntity.ok(RestaurantControllerMapper.toResponse(updatedRestaurant, user, workingPeriodList));
    }
}
