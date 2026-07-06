package com.fiap.foodlink_api.application.usecase.workingperiod;

import com.fiap.foodlink_api.domain.entity.WorkingPeriod;
import com.fiap.foodlink_api.domain.exception.WorkingPeriodNotFoundException;
import com.fiap.foodlink_api.domain.exception.WorkingPeriorAlreadyExistsException;
import com.fiap.foodlink_api.domain.gateway.WorkingPeriodGateway;
import com.fiap.foodlink_api.infrastructure.persistence.entity.WorkingPeriodJpaEntity;
import com.fiap.foodlink_api.domain.entity.DaysOfWeekEnum;
import com.fiap.foodlink_api.interfaces.controller.dto.WorkingPeriodRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Casos de uso de horario de funcionamento")
class WorkingPeriodUseCaseTest {

	@Test
	@DisplayName("Deve criar horario de funcionamento para restaurante")
	void deveCriarHorarioDeFuncionamentoParaRestaurante() {
		WorkingPeriodGateway workingPeriodGateway = mock(WorkingPeriodGateway.class);
		UUID restaurantId = UUID.randomUUID();
		WorkingPeriodRequest request = criarWorkingPeriodRequest();

		when(workingPeriodGateway.existsByRestaurantId(restaurantId)).thenReturn(false);
		when(workingPeriodGateway.save(any(WorkingPeriodJpaEntity.class)))
				.thenAnswer(invocation -> {
					WorkingPeriodJpaEntity entity = invocation.getArgument(0);
					return new WorkingPeriod(entity.getDay(), entity.getOpenTime(), entity.getCloseTime());
				});

		CreateWorkingPeriodUseCase useCase = new CreateWorkingPeriodUseCase(workingPeriodGateway);
		List<WorkingPeriod> workingPeriods = useCase.execute(request, restaurantId);

		assertEquals(3, workingPeriods.size());
		verify(workingPeriodGateway).existsByRestaurantId(restaurantId);
		verify(workingPeriodGateway, times(3)).save(any(WorkingPeriodJpaEntity.class));
	}

	@Test
	@DisplayName("Deve lancar excecao ao criar horario para restaurante que ja possui horarios")
	void deveLancarExcecaoAoCriarHorarioParaRestauranteComHorarioExistente() {
		WorkingPeriodGateway workingPeriodGateway = mock(WorkingPeriodGateway.class);
		UUID restaurantId = UUID.randomUUID();
		WorkingPeriodRequest request = criarWorkingPeriodRequest();

		when(workingPeriodGateway.existsByRestaurantId(restaurantId)).thenReturn(true);

		CreateWorkingPeriodUseCase useCase = new CreateWorkingPeriodUseCase(workingPeriodGateway);
		WorkingPeriorAlreadyExistsException exception = assertThrows(
				WorkingPeriorAlreadyExistsException.class,
				() -> useCase.execute(request, restaurantId)
		);

		assertEquals("Horário de funcionamento já cadastrado para o restaurante: " + restaurantId, exception.getMessage());
		verify(workingPeriodGateway, never()).save(any(WorkingPeriodJpaEntity.class));
	}

	@Test
	@DisplayName("Deve criar horario para cada dia da semana informado")
	void deveCriarHorarioParaCadaDiaSemanaInformado() {
		WorkingPeriodGateway workingPeriodGateway = mock(WorkingPeriodGateway.class);
		UUID restaurantId = UUID.randomUUID();
		WorkingPeriodRequest request = new WorkingPeriodRequest(
				List.of(DaysOfWeekEnum.SEGUNDA, DaysOfWeekEnum.TERCA),
				LocalTime.of(9, 0),
				LocalTime.of(23, 0)
		);

		when(workingPeriodGateway.existsByRestaurantId(restaurantId)).thenReturn(false);
		when(workingPeriodGateway.save(any(WorkingPeriodJpaEntity.class)))
				.thenAnswer(invocation -> {
					WorkingPeriodJpaEntity entity = invocation.getArgument(0);
					return new WorkingPeriod(entity.getDay(), entity.getOpenTime(), entity.getCloseTime());
				});

		CreateWorkingPeriodUseCase useCase = new CreateWorkingPeriodUseCase(workingPeriodGateway);
		List<WorkingPeriod> workingPeriods = useCase.execute(request, restaurantId);

		assertEquals(2, workingPeriods.size());
		verify(workingPeriodGateway, times(2)).save(any(WorkingPeriodJpaEntity.class));
	}

	@Test
	@DisplayName("Deve buscar horarios de funcionamento por restaurante")
	void deveBuscarHorariosDeFuncionamentoPorRestaurante() {
		WorkingPeriodGateway workingPeriodGateway = mock(WorkingPeriodGateway.class);
		UUID restaurantId = UUID.randomUUID();
		List<WorkingPeriod> workingPeriods = List.of(
				new WorkingPeriod(DaysOfWeekEnum.SEGUNDA, LocalTime.of(10, 0), LocalTime.of(22, 0)),
				new WorkingPeriod(DaysOfWeekEnum.TERCA, LocalTime.of(10, 0), LocalTime.of(22, 0))
		);

		when(workingPeriodGateway.findWorkingPeriodByRestaurantId(restaurantId)).thenReturn(workingPeriods);

		GetWorkingPeriodByIdUseCase useCase = new GetWorkingPeriodByIdUseCase(workingPeriodGateway);
		List<WorkingPeriod> result = useCase.execute(restaurantId);

		assertEquals(2, result.size());
		assertEquals(DaysOfWeekEnum.SEGUNDA, result.get(0).getDay());
		assertEquals(DaysOfWeekEnum.TERCA, result.get(1).getDay());
		verify(workingPeriodGateway).findWorkingPeriodByRestaurantId(restaurantId);
	}

	@Test
	@DisplayName("Deve lancar excecao ao buscar horarios de restaurante que nao possui horarios")
	void deveLancarExcecaoAoBuscarHorariosDeRestauranteSemHorarios() {
		WorkingPeriodGateway workingPeriodGateway = mock(WorkingPeriodGateway.class);
		UUID restaurantId = UUID.randomUUID();

		when(workingPeriodGateway.findWorkingPeriodByRestaurantId(restaurantId)).thenReturn(List.of());

		GetWorkingPeriodByIdUseCase useCase = new GetWorkingPeriodByIdUseCase(workingPeriodGateway);
		WorkingPeriodNotFoundException exception = assertThrows(
				WorkingPeriodNotFoundException.class,
				() -> useCase.execute(restaurantId)
		);

		assertEquals("Horário de funcionamento não encontrado para o restaurante: " + restaurantId, exception.getMessage());
		verify(workingPeriodGateway).findWorkingPeriodByRestaurantId(restaurantId);
	}

	private WorkingPeriodRequest criarWorkingPeriodRequest() {
		return new WorkingPeriodRequest(
				List.of(DaysOfWeekEnum.SEGUNDA, DaysOfWeekEnum.TERCA, DaysOfWeekEnum.QUARTA),
				LocalTime.of(10, 0),
				LocalTime.of(22, 0)
		);
	}
}
