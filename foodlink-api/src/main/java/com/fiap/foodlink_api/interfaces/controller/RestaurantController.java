package com.fiap.foodlink_api.interfaces.controller;

import com.fiap.foodlink_api.application.usecase.restaurants.*;
import com.fiap.foodlink_api.application.usecase.address.CreateAddressUseCase;
import com.fiap.foodlink_api.application.usecase.address.GetAddressByIdUseCase;
import com.fiap.foodlink_api.application.usecase.user.GetUserByIdUseCase;
import com.fiap.foodlink_api.application.usecase.workingperiod.CreateWorkingPeriodUseCase;
import com.fiap.foodlink_api.application.usecase.workingperiod.GetWorkingPeriodByIdUseCase;
import com.fiap.foodlink_api.domain.entity.Address;
import com.fiap.foodlink_api.domain.entity.Restaurant;
import com.fiap.foodlink_api.domain.entity.User;
import com.fiap.foodlink_api.domain.entity.WorkingPeriod;
import com.fiap.foodlink_api.domain.exception.DomainException;
import com.fiap.foodlink_api.interfaces.controller.dto.AddressRequest;
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
    private final CreateAddressUseCase createAddressUseCase;
    private final GetAddressByIdUseCase getAddressByIdUseCase;

    public RestaurantController(
            ListRestaurantsUseCase listRestaurantsUseCase,
            GetUserByIdUseCase getUserByIdUseCase,
            GetWorkingPeriodByIdUseCase getWorkingPeriodByIdUseCase,
            CreateRestaurantUseCase createRestaurantUseCase,
            CreateWorkingPeriodUseCase createWorkingPeriodUseCase,
            CreateAddressUseCase createAddressUseCase,
            GetAddressByIdUseCase getAddressByIdUseCase,
            GetRestaurantByIdUseCase getRestaurantByIdUseCase,
            DeleteRestauranteByIdUseCase deleteRestauranteByIdUseCase,
            UpdateRestaurantByIdUseCase updateRestaurantByIdUseCase
    ) {
        this.listRestaurantsUseCase = listRestaurantsUseCase;
        this.getUserByIdUseCase = getUserByIdUseCase;
        this.getWorkingPeriodByIdUseCase = getWorkingPeriodByIdUseCase;
        this.createRestaurantUseCase = createRestaurantUseCase;
        this.createWorkingPeriodUseCase = createWorkingPeriodUseCase;
        this.createAddressUseCase = createAddressUseCase;
        this.getAddressByIdUseCase = getAddressByIdUseCase;
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
                        getAddressByIdUseCase.execute(restaurant.getAddressId()),
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
                getAddressByIdUseCase.execute(restaurant.getAddressId()),
                getWorkingPeriodByIdUseCase.execute(restaurant.getId())
        );
        return ResponseEntity.ok(restaurantResponse);
    }

    @PostMapping
    @Operation(summary = "Cria um novo restaurante com horário de funcionamento e dono")
    public ResponseEntity<RestaurantResponse> create(@RequestBody RestaurantRequest request){
        Address address = createAddress(request.address());
        Restaurant restaurant = createRestaurantUseCase.execute(
                request.name(),
                request.cnpj(),
                request.type(),
                request.ownerId(),
                address.getId()
        );
        List<WorkingPeriod> workingPeriodList = createWorkingPeriodUseCase.execute(request.period(), restaurant.getId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(RestaurantControllerMapper.toResponse(
                        restaurant,
                        getUserByIdUseCase.execute(restaurant.getOwnerId()),
                        address,
                        workingPeriodList
                ));
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
        List<WorkingPeriod> workingPeriodList = WorkingPeriodControllerMapper.toWorkingPeriod(request.period());
        Restaurant updatedRestaurant = updateRestaurantByIdUseCase.execute(
                id,
                request.name(),
                request.cnpj(),
                request.type(),
                request.ownerId(),
                workingPeriodList
        );
        User owner = getUserByIdUseCase.execute(updatedRestaurant.getOwnerId());
        Address address = getAddressByIdUseCase.execute(updatedRestaurant.getAddressId());
        return ResponseEntity.ok(RestaurantControllerMapper.toResponse(updatedRestaurant, owner, address, workingPeriodList));
    }

    private Address createAddress(AddressRequest request) {
        if (request == null) {
            throw new DomainException("Endereco do restaurante e obrigatorio.");
        }

        return createAddressUseCase.execute(
                request.street(),
                request.number(),
                request.complement(),
                request.district(),
                request.city(),
                request.state(),
                request.zipCode()
        );
    }
}
