package com.fiap.foodlink_api.application.usecase.restaurants;

import com.fiap.foodlink_api.domain.entity.Restaurant;
import com.fiap.foodlink_api.domain.entity.User;
import com.fiap.foodlink_api.domain.entity.UserType;
import com.fiap.foodlink_api.domain.entity.UserTypeCode;
import com.fiap.foodlink_api.domain.entity.WorkingPeriod;
import com.fiap.foodlink_api.domain.exception.RestaurantAlreadyExistsException;
import com.fiap.foodlink_api.domain.exception.RestaurantNotFoundException;
import com.fiap.foodlink_api.domain.exception.UserNotFoundException;
import com.fiap.foodlink_api.domain.exception.UserTypeNotFoundException;
import com.fiap.foodlink_api.domain.gateway.RestaurantGateway;
import com.fiap.foodlink_api.domain.gateway.UserGateway;
import com.fiap.foodlink_api.domain.gateway.UserTypeGateway;
import com.fiap.foodlink_api.domain.gateway.WorkingPeriodGateway;
import com.fiap.foodlink_api.domain.entity.DaysOfWeekEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Casos de uso de restaurante")
class RestaurantUseCaseTest {

	@Test
	@DisplayName("Deve criar restaurante com dados validos")
	void deveCriarRestauranteComDadosValidos() {
		RestaurantGateway restaurantGateway = mock(RestaurantGateway.class);
		UserGateway userGateway = mock(UserGateway.class);
		UserTypeGateway userTypeGateway = mock(UserTypeGateway.class);
		UUID restaurantId = UUID.randomUUID();
		UUID ownerId = UUID.randomUUID();
		UUID userTypeId = UUID.randomUUID();
		UUID addressId = UUID.randomUUID();

		User owner = criarUsuario(ownerId, userTypeId);
		UserType userType = criarUserTypeDono(userTypeId);
		Restaurant savedRestaurant = criarRestaurante(restaurantId, ownerId, addressId);

		when(userGateway.findById(ownerId)).thenReturn(Optional.of(owner));
		when(userTypeGateway.findById(userTypeId)).thenReturn(Optional.of(userType));
		when(restaurantGateway.existsByCnpj("12.345.678/0001-99")).thenReturn(false);
		when(restaurantGateway.save(any(Restaurant.class))).thenReturn(savedRestaurant);

		CreateRestaurantUseCase useCase = new CreateRestaurantUseCase(restaurantGateway, userGateway, userTypeGateway);
		Restaurant restaurant = useCase.execute("Pizzaria Italiana", "12.345.678/0001-99", "Italiana", ownerId, addressId);

		assertEquals(restaurantId, restaurant.getId());
		assertEquals("Pizzaria Italiana", restaurant.getName());
		verify(userGateway).findById(ownerId);
		verify(userTypeGateway).findById(userTypeId);
		verify(restaurantGateway).existsByCnpj("12.345.678/0001-99");
		verify(restaurantGateway).save(any(Restaurant.class));
	}

	@Test
	@DisplayName("Deve lancar excecao ao criar restaurante com dono inexistente")
	void deveLancarExcecaoAoCriarRestauranteComDonoInexistente() {
		RestaurantGateway restaurantGateway = mock(RestaurantGateway.class);
		UserGateway userGateway = mock(UserGateway.class);
		UserTypeGateway userTypeGateway = mock(UserTypeGateway.class);
		UUID ownerId = UUID.randomUUID();
		UUID addressId = UUID.randomUUID();

		when(userGateway.findById(ownerId)).thenReturn(Optional.empty());

		CreateRestaurantUseCase useCase = new CreateRestaurantUseCase(restaurantGateway, userGateway, userTypeGateway);
		UserNotFoundException exception = assertThrows(
				UserNotFoundException.class,
				() -> useCase.execute("Pizzaria Italiana", "12.345.678/0001-99", "Italiana", ownerId, addressId)
		);

		assertEquals("Usuario nao encontrado: " + ownerId, exception.getMessage());
		verify(restaurantGateway, never()).save(any(Restaurant.class));
	}

	@Test
	@DisplayName("Deve lancar excecao ao criar restaurante com tipo de usuario inexistente")
	void deveLancarExcecaoAoCriarRestauranteComTipoUsuarioInexistente() {
		RestaurantGateway restaurantGateway = mock(RestaurantGateway.class);
		UserGateway userGateway = mock(UserGateway.class);
		UserTypeGateway userTypeGateway = mock(UserTypeGateway.class);
		UUID ownerId = UUID.randomUUID();
		UUID userTypeId = UUID.randomUUID();
		UUID addressId = UUID.randomUUID();

		User owner = criarUsuario(ownerId, userTypeId);
		when(userGateway.findById(ownerId)).thenReturn(Optional.of(owner));
		when(userTypeGateway.findById(userTypeId)).thenReturn(Optional.empty());

		CreateRestaurantUseCase useCase = new CreateRestaurantUseCase(restaurantGateway, userGateway, userTypeGateway);
		UserTypeNotFoundException exception = assertThrows(
				UserTypeNotFoundException.class,
				() -> useCase.execute("Pizzaria Italiana", "12.345.678/0001-99", "Italiana", ownerId, addressId)
		);

		assertEquals("Tipo de usuario nao encontrado: " + userTypeId, exception.getMessage());
		verify(restaurantGateway, never()).save(any(Restaurant.class));
	}

	@Test
	@DisplayName("Deve lancar excecao ao criar restaurante com cnpj ja cadastrado")
	void deveLancarExcecaoAoCriarRestauranteComCnpjJaCadastrado() {
		RestaurantGateway restaurantGateway = mock(RestaurantGateway.class);
		UserGateway userGateway = mock(UserGateway.class);
		UserTypeGateway userTypeGateway = mock(UserTypeGateway.class);
		UUID ownerId = UUID.randomUUID();
		UUID userTypeId = UUID.randomUUID();
		UUID addressId = UUID.randomUUID();

		User owner = criarUsuario(ownerId, userTypeId);
		UserType userType = criarUserTypeDono(userTypeId);

		when(userGateway.findById(ownerId)).thenReturn(Optional.of(owner));
		when(userTypeGateway.findById(userTypeId)).thenReturn(Optional.of(userType));
		when(restaurantGateway.existsByCnpj("12.345.678/0001-99")).thenReturn(true);

		CreateRestaurantUseCase useCase = new CreateRestaurantUseCase(restaurantGateway, userGateway, userTypeGateway);
		RestaurantAlreadyExistsException exception = assertThrows(
				RestaurantAlreadyExistsException.class,
				() -> useCase.execute("Pizzaria Italiana", "12.345.678/0001-99", "Italiana", ownerId, addressId)
		);

		assertEquals("Restaurante ja cadastrado com cnpj: 12.345.678/0001-99", exception.getMessage());
		verify(restaurantGateway, never()).save(any(Restaurant.class));
	}

	@Test
	@DisplayName("Deve buscar restaurante por identificador")
	void deveBuscarRestaurantePorIdentificador() {
		RestaurantGateway restaurantGateway = mock(RestaurantGateway.class);
		UUID restaurantId = UUID.randomUUID();
		UUID ownerId = UUID.randomUUID();
		UUID addressId = UUID.randomUUID();
		Restaurant savedRestaurant = criarRestaurante(restaurantId, ownerId, addressId);

		when(restaurantGateway.findById(restaurantId)).thenReturn(Optional.of(savedRestaurant));

		GetRestaurantByIdUseCase useCase = new GetRestaurantByIdUseCase(restaurantGateway);
		Restaurant restaurant = useCase.execute(restaurantId);

		assertEquals(restaurantId, restaurant.getId());
		assertEquals("Pizzaria Italiana", restaurant.getName());
		verify(restaurantGateway).findById(restaurantId);
	}

	@Test
	@DisplayName("Deve lancar excecao ao buscar restaurante inexistente")
	void deveLancarExcecaoAoBuscarRestauranteInexistente() {
		RestaurantGateway restaurantGateway = mock(RestaurantGateway.class);
		UUID restaurantId = UUID.randomUUID();

		when(restaurantGateway.findById(restaurantId)).thenReturn(Optional.empty());

		GetRestaurantByIdUseCase useCase = new GetRestaurantByIdUseCase(restaurantGateway);
		RestaurantNotFoundException exception = assertThrows(
				RestaurantNotFoundException.class,
				() -> useCase.execute(restaurantId)
		);

		assertEquals("Restaurante nao encontrado: " + restaurantId, exception.getMessage());
		verify(restaurantGateway).findById(restaurantId);
	}

	@Test
	@DisplayName("Deve listar restaurantes cadastrados")
	void deveListarRestaurantesCadastrados() {
		RestaurantGateway restaurantGateway = mock(RestaurantGateway.class);
		UUID ownerId = UUID.randomUUID();
		UUID addressId = UUID.randomUUID();
		List<Restaurant> restaurants = List.of(
				criarRestaurante(UUID.randomUUID(), ownerId, addressId),
				criarRestaurante(UUID.randomUUID(), ownerId, addressId)
		);

		when(restaurantGateway.findAll()).thenReturn(restaurants);

		ListRestaurantsUseCase useCase = new ListRestaurantsUseCase(restaurantGateway);
		List<Restaurant> result = useCase.execute();

		assertEquals(2, result.size());
		verify(restaurantGateway).findAll();
	}

	@Test
	@DisplayName("Deve remover restaurante cadastrado")
	void deveRemoverRestauranteCadastrado() {
		RestaurantGateway restaurantGateway = mock(RestaurantGateway.class);
		UUID restaurantId = UUID.randomUUID();

		when(restaurantGateway.existsById(restaurantId)).thenReturn(true);

		DeleteRestauranteByIdUseCase useCase = new DeleteRestauranteByIdUseCase(restaurantGateway);
		useCase.execute(restaurantId);

		verify(restaurantGateway).existsById(restaurantId);
		verify(restaurantGateway).deleteById(restaurantId);
	}

	@Test
	@DisplayName("Deve lancar excecao ao remover restaurante inexistente")
	void deveLancarExcecaoAoRemoverRestauranteInexistente() {
		RestaurantGateway restaurantGateway = mock(RestaurantGateway.class);
		UUID restaurantId = UUID.randomUUID();

		when(restaurantGateway.existsById(restaurantId)).thenReturn(false);

		DeleteRestauranteByIdUseCase useCase = new DeleteRestauranteByIdUseCase(restaurantGateway);
		RestaurantNotFoundException exception = assertThrows(
				RestaurantNotFoundException.class,
				() -> useCase.execute(restaurantId)
		);

		assertEquals("Restaurante nao encontrado: " + restaurantId, exception.getMessage());
		verify(restaurantGateway, never()).deleteById(restaurantId);
	}

	@Test
	@DisplayName("Deve atualizar restaurante")
	void deveAtualizarRestaurante() {
		RestaurantGateway restaurantGateway = mock(RestaurantGateway.class);
		UserGateway userGateway = mock(UserGateway.class);
		UserTypeGateway userTypeGateway = mock(UserTypeGateway.class);
		WorkingPeriodGateway workingPeriodGateway = mock(WorkingPeriodGateway.class);

		UUID restaurantId = UUID.randomUUID();
		UUID ownerId = UUID.randomUUID();
		UUID userTypeId = UUID.randomUUID();
		UUID addressId = UUID.randomUUID();

		User owner = criarUsuario(ownerId, userTypeId);
		UserType userType = criarUserTypeDono(userTypeId);
		Restaurant savedRestaurant = criarRestaurante(restaurantId, ownerId, addressId);
		List<WorkingPeriod> workingPeriods = List.of(criarHorarioDeFuncionamento());

		when(userGateway.findById(ownerId)).thenReturn(Optional.of(owner));
		when(userTypeGateway.findById(userTypeId)).thenReturn(Optional.of(userType));
		when(restaurantGateway.findById(restaurantId)).thenReturn(Optional.of(savedRestaurant));
		when(restaurantGateway.save(any(Restaurant.class))).thenReturn(savedRestaurant);

		UpdateRestaurantByIdUseCase useCase = new UpdateRestaurantByIdUseCase(
				restaurantGateway,
				workingPeriodGateway,
				userGateway,
				userTypeGateway
		);
		Restaurant restaurant = useCase.execute(restaurantId, "Churrascaria Gourmet", "98.765.432/0001-11", "Brasileira", ownerId, workingPeriods);

		assertEquals(restaurantId, restaurant.getId());
		verify(userGateway).findById(ownerId);
		verify(userTypeGateway).findById(userTypeId);
		verify(restaurantGateway).findById(restaurantId);
		verify(workingPeriodGateway).deleteAll(restaurantId);
		verify(restaurantGateway).save(any(Restaurant.class));
	}

	@Test
	@DisplayName("Deve lancar excecao ao atualizar restaurante inexistente")
	void deveLancarExcecaoAoAtualizarRestauranteInexistente() {
		RestaurantGateway restaurantGateway = mock(RestaurantGateway.class);
		UserGateway userGateway = mock(UserGateway.class);
		UserTypeGateway userTypeGateway = mock(UserTypeGateway.class);
		WorkingPeriodGateway workingPeriodGateway = mock(WorkingPeriodGateway.class);

		UUID restaurantId = UUID.randomUUID();
		UUID ownerId = UUID.randomUUID();
		UUID userTypeId = UUID.randomUUID();

		User owner = criarUsuario(ownerId, userTypeId);
		UserType userType = criarUserTypeDono(userTypeId);
		List<WorkingPeriod> workingPeriods = List.of(criarHorarioDeFuncionamento());

		when(userGateway.findById(ownerId)).thenReturn(Optional.of(owner));
		when(userTypeGateway.findById(userTypeId)).thenReturn(Optional.of(userType));
		when(restaurantGateway.findById(restaurantId)).thenReturn(Optional.empty());

		UpdateRestaurantByIdUseCase useCase = new UpdateRestaurantByIdUseCase(
				restaurantGateway,
				workingPeriodGateway,
				userGateway,
				userTypeGateway
		);
		RestaurantNotFoundException exception = assertThrows(
				RestaurantNotFoundException.class,
				() -> useCase.execute(restaurantId, "Churrascaria Gourmet", "98.765.432/0001-11", "Brasileira", ownerId, workingPeriods)
		);

		assertEquals("Restaurante nao encontrado: " + restaurantId, exception.getMessage());
		verify(restaurantGateway, never()).save(any(Restaurant.class));
	}

	private Restaurant criarRestaurante(UUID id, UUID ownerId, UUID addressId) {
		return new Restaurant(
				id,
				"Pizzaria Italiana",
				"12.345.678/0001-99",
				"Italiana",
				ownerId,
				addressId,
				OffsetDateTime.now()
		);
	}

	private User criarUsuario(UUID id, UUID userTypeId) {
		return new User(
				id,
				"Jose Silva",
				"jose@email.com",
				"jose",
				"senha123",
				userTypeId,
				UUID.randomUUID(),
				OffsetDateTime.now()
		);
	}

	private UserType criarUserTypeDono(UUID id) {
		return new UserType(id, "Dono", UserTypeCode.DONO_RESTAURANTE);
	}

	private WorkingPeriod criarHorarioDeFuncionamento() {
		return new WorkingPeriod(DaysOfWeekEnum.SEGUNDA, LocalTime.of(10, 0), LocalTime.of(22, 0));
	}
}
