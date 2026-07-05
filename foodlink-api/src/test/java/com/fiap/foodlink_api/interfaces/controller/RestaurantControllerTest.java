package com.fiap.foodlink_api.interfaces.controller;

import com.fiap.foodlink_api.application.usecase.address.CreateAddressUseCase;
import com.fiap.foodlink_api.application.usecase.address.GetAddressByIdUseCase;
import com.fiap.foodlink_api.application.usecase.address.UpdateAddressUseCase;
import com.fiap.foodlink_api.application.usecase.restaurants.*;
import com.fiap.foodlink_api.application.usecase.user.GetUserByIdUseCase;
import com.fiap.foodlink_api.application.usecase.workingperiod.CreateWorkingPeriodUseCase;
import com.fiap.foodlink_api.application.usecase.workingperiod.GetWorkingPeriodByIdUseCase;
import com.fiap.foodlink_api.domain.entity.Address;
import com.fiap.foodlink_api.domain.entity.Restaurant;
import com.fiap.foodlink_api.domain.entity.User;
import com.fiap.foodlink_api.domain.entity.WorkingPeriod;
import com.fiap.foodlink_api.domain.exception.DomainException;
import com.fiap.foodlink_api.infrastructure.persistence.enums.DaysOfWeekEnum;
import com.fiap.foodlink_api.interfaces.controller.dto.AddressRequest;
import com.fiap.foodlink_api.interfaces.controller.dto.RestaurantRequest;
import com.fiap.foodlink_api.interfaces.controller.dto.RestaurantResponse;
import com.fiap.foodlink_api.interfaces.controller.dto.WorkingPeriodRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Interface de restaurante")
class RestaurantControllerTest {

	@Test
	@DisplayName("Deve criar restaurante com endereco e horario de funcionamento")
	void deveCriarRestauranteComEnderecoEHorarioDeFuncionamento() {
		CreateAddressUseCase createAddressUseCase = mock(CreateAddressUseCase.class);
		CreateRestaurantUseCase createRestaurantUseCase = mock(CreateRestaurantUseCase.class);
		CreateWorkingPeriodUseCase createWorkingPeriodUseCase = mock(CreateWorkingPeriodUseCase.class);
		GetUserByIdUseCase getUserByIdUseCase = mock(GetUserByIdUseCase.class);
		RestaurantController controller = criarController(
				createRestaurantUseCase,
				createAddressUseCase,
				createWorkingPeriodUseCase,
				getUserByIdUseCase
		);
		UUID restaurantId = UUID.randomUUID();
		UUID ownerId = UUID.randomUUID();
		UUID addressId = UUID.randomUUID();
		Address address = criarEndereco(addressId);
		Restaurant restaurant = criarRestaurante(restaurantId, ownerId, addressId);
		User owner = criarUsuario(ownerId);
		List<WorkingPeriod> workingPeriods = List.of(criarHorarioDeFuncionamento());
		RestaurantRequest request = criarRestaurantRequest(ownerId);

		when(createAddressUseCase.execute("Rua dos Restaurantes", "123", "Sala 10", "Centro", "Sao Paulo", "SP", "01001000"))
				.thenReturn(address);
		when(createRestaurantUseCase.execute("Pizzaria Italiana", "12.345.678/0001-99", "Italiana", ownerId, addressId))
				.thenReturn(restaurant);
		when(createWorkingPeriodUseCase.execute(request.period(), restaurantId))
				.thenReturn(workingPeriods);
		when(getUserByIdUseCase.execute(ownerId)).thenReturn(owner);

		ResponseEntity<RestaurantResponse> response = controller.create(request);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(restaurantId, response.getBody().id());
		assertEquals("Pizzaria Italiana", response.getBody().name());
		assertEquals(addressId, response.getBody().address().id());
		verify(createAddressUseCase).execute("Rua dos Restaurantes", "123", "Sala 10", "Centro", "Sao Paulo", "SP", "01001000");
		verify(createRestaurantUseCase).execute("Pizzaria Italiana", "12.345.678/0001-99", "Italiana", ownerId, addressId);
		verify(createWorkingPeriodUseCase).execute(request.period(), restaurantId);
		verify(getUserByIdUseCase).execute(ownerId);
	}

	@Test
	@DisplayName("Deve buscar restaurante por identificador com endereco e horarios")
	void deveBuscarRestaurantePorIdentificadorComEnderecoEHorarios() {
		GetRestaurantByIdUseCase getRestaurantByIdUseCase = mock(GetRestaurantByIdUseCase.class);
		GetAddressByIdUseCase getAddressByIdUseCase = mock(GetAddressByIdUseCase.class);
		GetUserByIdUseCase getUserByIdUseCase = mock(GetUserByIdUseCase.class);
		GetWorkingPeriodByIdUseCase getWorkingPeriodByIdUseCase = mock(GetWorkingPeriodByIdUseCase.class);
		RestaurantController controller = criarController(
				getRestaurantByIdUseCase,
				getAddressByIdUseCase,
				getUserByIdUseCase,
				getWorkingPeriodByIdUseCase
		);
		UUID restaurantId = UUID.randomUUID();
		UUID ownerId = UUID.randomUUID();
		UUID addressId = UUID.randomUUID();
		Restaurant restaurant = criarRestaurante(restaurantId, ownerId, addressId);
		Address address = criarEndereco(addressId);
		User owner = criarUsuario(ownerId);
		List<WorkingPeriod> workingPeriods = List.of(criarHorarioDeFuncionamento());

		when(getRestaurantByIdUseCase.execute(restaurantId)).thenReturn(restaurant);
		when(getAddressByIdUseCase.execute(addressId)).thenReturn(address);
		when(getUserByIdUseCase.execute(ownerId)).thenReturn(owner);
		when(getWorkingPeriodByIdUseCase.execute(restaurantId)).thenReturn(workingPeriods);

		ResponseEntity<RestaurantResponse> response = controller.findById(restaurantId);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(restaurantId, response.getBody().id());
		assertEquals("Pizzaria Italiana", response.getBody().name());
		assertEquals(addressId, response.getBody().address().id());
		verify(getRestaurantByIdUseCase).execute(restaurantId);
		verify(getAddressByIdUseCase).execute(addressId);
		verify(getUserByIdUseCase).execute(ownerId);
		verify(getWorkingPeriodByIdUseCase).execute(restaurantId);
	}

	@Test
	@DisplayName("Deve listar restaurantes com endereco e horarios")
	void deveListarRestaurantesComEnderecoEHorarios() {
		ListRestaurantsUseCase listRestaurantsUseCase = mock(ListRestaurantsUseCase.class);
		GetAddressByIdUseCase getAddressByIdUseCase = mock(GetAddressByIdUseCase.class);
		GetUserByIdUseCase getUserByIdUseCase = mock(GetUserByIdUseCase.class);
		GetWorkingPeriodByIdUseCase getWorkingPeriodByIdUseCase = mock(GetWorkingPeriodByIdUseCase.class);
		RestaurantController controller = criarController(
				listRestaurantsUseCase,
				getAddressByIdUseCase,
				getUserByIdUseCase,
				getWorkingPeriodByIdUseCase
		);
		UUID firstRestaurantId = UUID.randomUUID();
		UUID secondRestaurantId = UUID.randomUUID();
		UUID firstOwnerId = UUID.randomUUID();
		UUID secondOwnerId = UUID.randomUUID();
		UUID firstAddressId = UUID.randomUUID();
		UUID secondAddressId = UUID.randomUUID();
		Restaurant firstRestaurant = criarRestaurante(firstRestaurantId, firstOwnerId, firstAddressId);
		Restaurant secondRestaurant = criarRestaurante(secondRestaurantId, secondOwnerId, secondAddressId);
		List<WorkingPeriod> workingPeriods = List.of(criarHorarioDeFuncionamento());

		when(listRestaurantsUseCase.execute()).thenReturn(List.of(firstRestaurant, secondRestaurant));
		when(getAddressByIdUseCase.execute(firstAddressId)).thenReturn(criarEndereco(firstAddressId));
		when(getAddressByIdUseCase.execute(secondAddressId)).thenReturn(criarEndereco(secondAddressId));
		when(getUserByIdUseCase.execute(firstOwnerId)).thenReturn(criarUsuario(firstOwnerId));
		when(getUserByIdUseCase.execute(secondOwnerId)).thenReturn(criarUsuario(secondOwnerId));
		when(getWorkingPeriodByIdUseCase.execute(firstRestaurantId)).thenReturn(workingPeriods);
		when(getWorkingPeriodByIdUseCase.execute(secondRestaurantId)).thenReturn(workingPeriods);

		ResponseEntity<List<RestaurantResponse>> response = controller.findAll();

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(2, response.getBody().size());
		assertEquals(firstAddressId, response.getBody().get(0).address().id());
		assertEquals(secondAddressId, response.getBody().get(1).address().id());
		verify(listRestaurantsUseCase).execute();
		verify(getAddressByIdUseCase).execute(firstAddressId);
		verify(getAddressByIdUseCase).execute(secondAddressId);
		verify(getUserByIdUseCase).execute(firstOwnerId);
		verify(getUserByIdUseCase).execute(secondOwnerId);
	}

	@Test
	@DisplayName("Deve atualizar restaurante")
	void deveAtualizarRestaurante() {
		UpdateRestaurantByIdUseCase updateRestaurantByIdUseCase = mock(UpdateRestaurantByIdUseCase.class);
		GetRestaurantByIdUseCase getRestaurantByIdUseCase = mock(GetRestaurantByIdUseCase.class);
		UpdateAddressUseCase updateAddressUseCase = mock(UpdateAddressUseCase.class);
		GetAddressByIdUseCase getAddressByIdUseCase = mock(GetAddressByIdUseCase.class);
		GetUserByIdUseCase getUserByIdUseCase = mock(GetUserByIdUseCase.class);
		RestaurantController controller = criarController(
				updateRestaurantByIdUseCase,
				getRestaurantByIdUseCase,
				updateAddressUseCase,
				getAddressByIdUseCase,
				getUserByIdUseCase
		);
		UUID restaurantId = UUID.randomUUID();
		UUID ownerId = UUID.randomUUID();
		UUID addressId = UUID.randomUUID();
		Restaurant restaurant = criarRestaurante(restaurantId, ownerId, addressId);
		Address address = criarEndereco(addressId);
		User owner = criarUsuario(ownerId);
		RestaurantRequest request = criarRestaurantRequest(ownerId);

		when(getRestaurantByIdUseCase.execute(restaurantId)).thenReturn(restaurant);
		when(updateAddressUseCase.execute(addressId, "Rua dos Restaurantes", "123", "Sala 10", "Centro", "Sao Paulo", "SP", "01001000"))
				.thenReturn(address);
		when(updateRestaurantByIdUseCase.execute(
				eq(restaurantId),
				eq("Pizzaria Italiana"),
				eq("12.345.678/0001-99"),
				eq("Italiana"),
				eq(ownerId),
				any()
		)).thenReturn(restaurant);
		when(getUserByIdUseCase.execute(ownerId)).thenReturn(owner);

		ResponseEntity<RestaurantResponse> response = controller.update(request, restaurantId);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(restaurantId, response.getBody().id());
		assertEquals(addressId, response.getBody().address().id());
		verify(getRestaurantByIdUseCase).execute(restaurantId);
		verify(updateAddressUseCase).execute(addressId, "Rua dos Restaurantes", "123", "Sala 10", "Centro", "Sao Paulo", "SP", "01001000");
		verify(updateRestaurantByIdUseCase).execute(
				eq(restaurantId),
				eq("Pizzaria Italiana"),
				eq("12.345.678/0001-99"),
				eq("Italiana"),
				eq(ownerId),
				any()
		);
		verify(getUserByIdUseCase).execute(ownerId);
	}

	@Test
	@DisplayName("Deve remover restaurante")
	void deveRemoverRestaurante() {
		DeleteRestauranteByIdUseCase deleteRestauranteByIdUseCase = mock(DeleteRestauranteByIdUseCase.class);
		RestaurantController controller = criarController(deleteRestauranteByIdUseCase);
		UUID restaurantId = UUID.randomUUID();

		ResponseEntity<Void> response = controller.delete(restaurantId);

		assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
		assertNull(response.getBody());
		verify(deleteRestauranteByIdUseCase).execute(restaurantId);
	}

	@Test
	@DisplayName("Deve lancar excecao quando endereco nao for informado na criacao")
	void deveLancarExcecaoQuandoEnderecoNaoForInformadoNaCriacao() {
		RestaurantController controller = criarController(
				mock(CreateRestaurantUseCase.class),
				mock(CreateAddressUseCase.class),
				mock(CreateWorkingPeriodUseCase.class),
				mock(GetUserByIdUseCase.class)
		);
		RestaurantRequest request = new RestaurantRequest(
				"12.345.678/0001-99",
				"Pizzaria Italiana",
				"Italiana",
				UUID.randomUUID(),
				null,
				criarWorkingPeriodRequest()
		);

		DomainException exception = assertThrows(DomainException.class, () -> controller.create(request));

		assertEquals("Endereco do restaurante e obrigatorio.", exception.getMessage());
	}

	private RestaurantController criarController(
			CreateRestaurantUseCase createRestaurantUseCase,
			CreateAddressUseCase createAddressUseCase,
			CreateWorkingPeriodUseCase createWorkingPeriodUseCase,
			GetUserByIdUseCase getUserByIdUseCase
	) {
		return new RestaurantController(
				mock(ListRestaurantsUseCase.class),
				getUserByIdUseCase,
				mock(GetWorkingPeriodByIdUseCase.class),
				createRestaurantUseCase,
				createWorkingPeriodUseCase,
				createAddressUseCase,
				mock(GetAddressByIdUseCase.class),
				mock(UpdateAddressUseCase.class),
				mock(GetRestaurantByIdUseCase.class),
				mock(DeleteRestauranteByIdUseCase.class),
				mock(UpdateRestaurantByIdUseCase.class)
		);
	}

	private RestaurantController criarController(
			GetRestaurantByIdUseCase getRestaurantByIdUseCase,
			GetAddressByIdUseCase getAddressByIdUseCase,
			GetUserByIdUseCase getUserByIdUseCase,
			GetWorkingPeriodByIdUseCase getWorkingPeriodByIdUseCase
	) {
		return new RestaurantController(
				mock(ListRestaurantsUseCase.class),
				getUserByIdUseCase,
				getWorkingPeriodByIdUseCase,
				mock(CreateRestaurantUseCase.class),
				mock(CreateWorkingPeriodUseCase.class),
				mock(CreateAddressUseCase.class),
				getAddressByIdUseCase,
				mock(UpdateAddressUseCase.class),
				getRestaurantByIdUseCase,
				mock(DeleteRestauranteByIdUseCase.class),
				mock(UpdateRestaurantByIdUseCase.class)
		);
	}

	private RestaurantController criarController(
			ListRestaurantsUseCase listRestaurantsUseCase,
			GetAddressByIdUseCase getAddressByIdUseCase,
			GetUserByIdUseCase getUserByIdUseCase,
			GetWorkingPeriodByIdUseCase getWorkingPeriodByIdUseCase
	) {
		return new RestaurantController(
				listRestaurantsUseCase,
				getUserByIdUseCase,
				getWorkingPeriodByIdUseCase,
				mock(CreateRestaurantUseCase.class),
				mock(CreateWorkingPeriodUseCase.class),
				mock(CreateAddressUseCase.class),
				getAddressByIdUseCase,
				mock(UpdateAddressUseCase.class),
				mock(GetRestaurantByIdUseCase.class),
				mock(DeleteRestauranteByIdUseCase.class),
				mock(UpdateRestaurantByIdUseCase.class)
		);
	}

	private RestaurantController criarController(
			UpdateRestaurantByIdUseCase updateRestaurantByIdUseCase,
			GetRestaurantByIdUseCase getRestaurantByIdUseCase,
			UpdateAddressUseCase updateAddressUseCase,
			GetAddressByIdUseCase getAddressByIdUseCase,
			GetUserByIdUseCase getUserByIdUseCase
	) {
		return new RestaurantController(
				mock(ListRestaurantsUseCase.class),
				getUserByIdUseCase,
				mock(GetWorkingPeriodByIdUseCase.class),
				mock(CreateRestaurantUseCase.class),
				mock(CreateWorkingPeriodUseCase.class),
				mock(CreateAddressUseCase.class),
				getAddressByIdUseCase,
				updateAddressUseCase,
				getRestaurantByIdUseCase,
				mock(DeleteRestauranteByIdUseCase.class),
				updateRestaurantByIdUseCase
		);
	}

	private RestaurantController criarController(DeleteRestauranteByIdUseCase deleteRestauranteByIdUseCase) {
		return new RestaurantController(
				mock(ListRestaurantsUseCase.class),
				mock(GetUserByIdUseCase.class),
				mock(GetWorkingPeriodByIdUseCase.class),
				mock(CreateRestaurantUseCase.class),
				mock(CreateWorkingPeriodUseCase.class),
				mock(CreateAddressUseCase.class),
				mock(GetAddressByIdUseCase.class),
				mock(UpdateAddressUseCase.class),
				mock(GetRestaurantByIdUseCase.class),
				deleteRestauranteByIdUseCase,
				mock(UpdateRestaurantByIdUseCase.class)
		);
	}

	private RestaurantRequest criarRestaurantRequest(UUID ownerId) {
		return new RestaurantRequest(
				"12.345.678/0001-99",
				"Pizzaria Italiana",
				"Italiana",
				ownerId,
				new AddressRequest("Rua dos Restaurantes", "123", "Sala 10", "Centro", "Sao Paulo", "SP", "01001000"),
				criarWorkingPeriodRequest()
		);
	}

	private WorkingPeriodRequest criarWorkingPeriodRequest() {
		return new WorkingPeriodRequest(
				List.of(DaysOfWeekEnum.SEGUNDA, DaysOfWeekEnum.TERCA, DaysOfWeekEnum.QUARTA),
				LocalTime.of(10, 0),
				LocalTime.of(22, 0)
		);
	}

	private Restaurant criarRestaurante(UUID id, UUID ownerId, UUID addressId) {
		return new Restaurant(
				id,
				"Pizzaria Italiana",
				"12.345.678/0001-99",
				"Italiana",
				ownerId,
				addressId,
				OffsetDateTime.parse("2026-06-24T01:00:00-03:00")
		);
	}

	private Address criarEndereco(UUID id) {
		return new Address(
				id,
				"Rua dos Restaurantes",
				"123",
				"Sala 10",
				"Centro",
				"Sao Paulo",
				"SP",
				"01001000",
				OffsetDateTime.parse("2026-06-24T01:00:00-03:00")
		);
	}

	private User criarUsuario(UUID id) {
		return new User(
				id,
				"Jose Silva",
				"jose@email.com",
				"jose",
				"senha123",
				UUID.randomUUID(),
				UUID.randomUUID(),
				OffsetDateTime.parse("2026-06-24T01:00:00-03:00")
		);
	}

	private WorkingPeriod criarHorarioDeFuncionamento() {
		return new WorkingPeriod(DaysOfWeekEnum.SEGUNDA, LocalTime.of(10, 0), LocalTime.of(22, 0));
	}
}
